package com.st.mall.userservice.controller;

import com.github.pagehelper.PageInfo;
import com.st.mall.common.bean.RespBean;
import com.st.mall.common.bean.User;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
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
}
