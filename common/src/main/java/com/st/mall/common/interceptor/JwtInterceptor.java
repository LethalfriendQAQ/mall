package com.st.mall.common.interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.st.mall.common.bean.RespBean;
import com.st.mall.common.config.WhiteListConfig;
import com.st.mall.common.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private WhiteListConfig whiteListConfig;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object hander) throws IOException {
        //获取请求路径
        String path = request.getRequestURI();
        //获取请求方式
        String method = request.getMethod();
        //判断请求是否在白名单中 - 放行
        if (whiteListConfig.getRules() != null && whiteListConfig.getRules()
                .stream()
                .anyMatch(rule -> rule.getMethod().equalsIgnoreCase(method) && path.matches(rule.getRegPath()))) {
            return true;
        }


        //对OPTIONS请求放行，不然会出现跨域问题
        if("OPTIONS".equals(request.getMethod().toUpperCase())) {
            return true;
        }
        //获取token
        String token = request.getHeader("token");
        RespBean respBean = null;
        try {
            //解析JWT，如果出现问题会抛出异常
            Map<String, Object> map = JwtUtil.parseJwtToMap(token);

            //生成新的JWT
            String jwt = JwtUtil.generateJwt(map);
            //将jwt放入response的响应头中
            response.setHeader("token", jwt);
            response.setHeader("Access-Control-Expose-Headers", "token");
            return true;
        } catch (SignatureVerificationException e) {
            respBean = RespBean.error("无效签名");
        } catch (TokenExpiredException e) {
            respBean = RespBean.error("令牌超时");
        } catch (AlgorithmMismatchException e) {
            respBean = RespBean.error("算法不匹配");
        } catch (Exception e) {
            respBean = RespBean.error("令牌无效");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");
        objectMapper.writeValue(response.getOutputStream(), respBean);
        return false;
    }
}
