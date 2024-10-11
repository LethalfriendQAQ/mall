package com.st.mall.userservice.controller;

import com.st.mall.common.bean.Addr;
import com.st.mall.common.bean.RespBean;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.AddrService;
import com.st.mall.common.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/addr")
public class AddController {
    @Autowired
    private AddrService addrService;

    @PostMapping
    public RespBean insert(@RequestBody Addr addr) {
        addrService.insert(addr);
        return RespBean.ok("添加成功");
    }

    //删除
    @DeleteMapping("/{id}")
    public RespBean delete(@PathVariable("id") Integer id, @RequestHeader("token") String token) throws StException {
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        Integer userId = (Integer) map.get("id");
        addrService.delete(id, userId);
        return RespBean.ok("删除成功");
    }

    @PutMapping
    public RespBean update(@RequestBody Addr addr, @RequestHeader("token") String token) throws StException {
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        Integer userId = (Integer) map.get("id");
        addrService.update(addr, userId);
        return RespBean.ok("修改成功");
    }

    @GetMapping
    public RespBean selectByUser(@RequestHeader("token") String token) {
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        Integer userId = (Integer) map.get("id");

        Addr condition = new Addr();
        condition.setUserId(userId);
        List<Addr> addrList = addrService.selectByCondition(condition);
        return RespBean.ok("查询成功", addrList);
    }

    @GetMapping("/{id}")
    public RespBean selectById(@PathVariable("id") Integer id) {
        Addr addr = addrService.selectById(id);
        return RespBean.ok("查询成功");
    }

}
