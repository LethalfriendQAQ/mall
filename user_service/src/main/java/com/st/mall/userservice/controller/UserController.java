package com.st.mall.userservice.controller;

import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageInfo;
import com.st.mall.common.bean.RespBean;
import com.st.mall.common.bean.User;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.UserService;
import com.st.mall.common.utils.JwtUtil;
import com.st.mall.common.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserService userService;

    @GetMapping("/captcha")
    public RespBean captcha() {
        //创建验证码的对象
        LineCaptcha captcha = new LineCaptcha(120, 32, 5, 10);
        //生成唯一的key - 雪花算法
        String key = IdUtil.getSnowflakeNextIdStr();
        //将key和验证码的文本保存在Redis中
        redisUtil.set(key, captcha.getCode(), 300);
        //将验证码图片装换成Base64编码形式
        String imageBase64Data = captcha.getImageBase64Data();
        //将key和Base64编码保存在map中
        HashMap<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("imageBase64Data", imageBase64Data);
        //将key和Base64图片返回
        return RespBean.ok("生成验证码成功", map);

    }

    @PostMapping("/login")
    public RespBean login(String username, String password, String key, String captchaInput) throws StException {
        //判断验证码是否正确 - 从Redis中查询验证码进行判断
        String  captcha = (String)redisUtil.get(key);
        if (captcha == null || !captcha.equalsIgnoreCase(captchaInput)) {
            throw new StException("验证码错误");
        }
        //登录
        User user = userService.login(username, password);
        //生成token
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("role", "user");
        String jwtStr = JwtUtil.generateJwt(map);
        //将token返回给用户
        return RespBean.ok("登录成功", jwtStr);
    }

    @GetMapping("/info")
    public RespBean getInfo(@RequestHeader("token") String token) {
        //解析token
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        //获取用户的id - 根据id查询用户的信息
        Integer id = (Integer) map.get("id");
        User user = userService.selectById(id);
        //返回
        return RespBean.ok("",user);
    }

    @PostMapping("/reg")
    public RespBean reg(String username, String password, String key, String captchaInput) throws StException {
        //判断验证码是否正确
        String captcha = (String) redisUtil.get(key);
        if (captcha == null || !captcha.equalsIgnoreCase(captchaInput)) {
            throw new StException("验证码错误");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.insert(user);
        return RespBean.ok("注册成功");
    }
    //添加
    @PostMapping
    public RespBean insert(@RequestBody User user) throws StException {
        userService.insert(user);
        return RespBean.ok("添加成功");
    }
    //修改
    @PutMapping
    public RespBean update(@RequestBody User user) throws StException {
        userService.update(user);
        return RespBean.ok("修改成功");
    }

    @DeleteMapping("/{id}")
    public RespBean delete(@PathVariable("id") Integer id) throws StException {
        userService.delete(id);
        return RespBean.ok("删除成功");
    }
    //搜索
    @GetMapping
    public RespBean selectByPage(User condition, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 5 : pageSize;

        PageInfo<User> pageInfo = userService.selectByCondition(condition, pageNum, pageSize);
        return RespBean.ok("查询成功", pageInfo);
    }
    //根据id查询
    @GetMapping("/{id}")
    public RespBean selectById(@PathVariable("id") Integer id) {
        User user = userService.selectById(id);
        return RespBean.ok("", user);
    }

    //修改密码
    @PutMapping("/changePassword")
    public RespBean changePassword(String oldPwd, String newPwd, String newPwd1, @RequestHeader("token") String token) throws StException {
        if (!newPwd.equals(newPwd1)) {
            throw new StException("两次输入的新密码不一致");
        }
        //解析token
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        //获取用户id - 根据id查询用户信息
        Integer id = (Integer) map.get("id");
        userService.changePassword(oldPwd, newPwd, id);
        return RespBean.ok("修改成功");
    }


    @PutMapping("/changePayPassword")
    public RespBean changePayPassword(String oldPwd, String newPwd, String newPwd1, @RequestHeader("token") String token) throws StException {
        if (!newPwd.equals(newPwd1)) {
            throw new StException("两次输入的新密码不一致");
        }
        //解析token
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        //获取用户id - 根据id查询用户信息
        Integer id = (Integer) map.get("id");
        userService.changePayPassword(oldPwd, newPwd, id);
        return RespBean.ok("修改成功");
    }
    @PutMapping("/setPayPwd")
    public RespBean setPayPwd(String password, String payPassword, @RequestHeader("token") String token) throws StException {
        //解析token
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        //获取用户id - 根据id查询用户信息
        Integer id = (Integer) map.get("id");
        User user = new User();
        user.setId(id);
        user.setPassword(password);
        user.setPayPassword(payPassword);
        userService.byPwdUpdate(user);
        return RespBean.ok("设置成功");
    }

    @PutMapping("/setRealname")
    public RespBean setRealname(String password, String realname, String idCard, @RequestHeader("token") String token) throws StException {
        //解析token
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        //获取用户id - 根据id查询用户信息
        Integer id = (Integer) map.get("id");
        User user = new User();
        user.setId(id);
        user.setPassword(password);
        user.setRealname(realname);
        user.setIdCard(idCard);
        userService.byPwdUpdate(user);
        return RespBean.ok("设置成功");
    }

    @PutMapping("/recharge")
    public RespBean recharge(String password, BigDecimal money, @RequestHeader("token") String token) throws StException {
        //解析token
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        //获取用户id - 根据id查询用户信息
        Integer id = (Integer) map.get("id");
        User user = new User();
        user.setId(id);
        user.setPassword(password);
        user.setMoney(money);
        userService.byPwdUpdate(user);
        return RespBean.ok("设置成功");
    }
    @PutMapping("/cancelAccount")
    public RespBean cancelAccount(String password, @RequestHeader("token") String token) throws StException {
        //解析token
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        //获取用户id - 根据id查询用户信息
        Integer id = (Integer) map.get("id");
        User user = new User();
        user.setId(id);
        user.setPassword(password);
        userService.cancelAccount(user);
        return RespBean.ok("设置成功");
    }
}
