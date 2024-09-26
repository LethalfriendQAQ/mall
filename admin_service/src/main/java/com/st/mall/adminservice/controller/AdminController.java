package com.st.mall.adminservice.controller;

import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageInfo;
import com.st.mall.common.bean.Admin;
import com.st.mall.common.bean.RespBean;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.AdminService;
import com.st.mall.common.utils.JwtUtil;
import com.st.mall.common.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/captcha")
    public RespBean captcha() {
        //创建验证码的对象
        LineCaptcha captcha = new LineCaptcha(120, 38, 4, 10);
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
        Admin admin = adminService.login(username, password);
        //生成token
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", admin.getId());
        map.put("username", admin.getUsername());
        map.put("role", "admin");
        String jwtStr = JwtUtil.generateJwt(map);
        //将token返回给用户
        return RespBean.ok("登录成功", jwtStr);
    }

    //添加
    @PostMapping
    public RespBean insert(@RequestBody Admin admin) throws StException {
        adminService.insert(admin);
        return RespBean.ok("添加成功");
    }
    //修改
    @PutMapping
    public RespBean update(@RequestBody Admin admin) throws StException {
        adminService.update(admin);
        return RespBean.ok("修改成功");
    }

    @DeleteMapping("/{id}")
    public RespBean delete(@PathVariable("id") Integer id) {
        adminService.delete(id);
        return RespBean.ok("删除成功");
    }
    //搜索
    @GetMapping
    public RespBean selectByPage(Admin condition, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 5 : pageSize;

        PageInfo<Admin> pageInfo = adminService.selectByCondition(condition, pageNum, pageSize);
        return RespBean.ok("查询成功", pageInfo);
    }
    //根据id查询
    @GetMapping("/{id}")
    public RespBean selectById(@PathVariable("id") Integer id) {
        Admin admin = adminService.selectById(id);
        return RespBean.ok("", admin);
    }
    @GetMapping("/test")
    public RespBean test() {
        return RespBean.ok("hhhhh");
    }
}
