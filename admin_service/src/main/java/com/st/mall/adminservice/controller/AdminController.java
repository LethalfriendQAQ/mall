package com.st.mall.adminservice.controller;

import com.github.pagehelper.PageInfo;
import com.st.mall.common.bean.Admin;
import com.st.mall.common.bean.RespBean;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

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
}
