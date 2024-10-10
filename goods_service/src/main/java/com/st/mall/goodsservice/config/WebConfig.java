package com.st.mall.goodsservice.config;

import com.st.mall.common.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Component("goodsServiceWebConfig")
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private JwtInterceptor jwtInterceptor;
    @Value("${st.picDir: D:/situ/code/Project02/pic/}")
    private String picDir;
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/category/pic/**", "/goods/pic/**")
                .addResourceLocations("file:" + picDir);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/goods/**", "/category/**")
                .excludePathPatterns(
                        "/category/allParent",
                        "/category/pic/**",
                        "/category/search",
                        "/goods/pic/**",
                        "/goods/search"
                );
    }
}
