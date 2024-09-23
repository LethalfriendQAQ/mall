package com.st.mall.userservice.controller;

import com.st.mall.common.bean.RespBean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/hello")
    public RespBean hello() {
        return RespBean.ok("hello");
    }
}
