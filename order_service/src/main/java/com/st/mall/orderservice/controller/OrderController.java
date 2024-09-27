package com.st.mall.orderservice.controller;

import com.github.pagehelper.PageInfo;
import com.st.mall.common.bean.Order;
import com.st.mall.common.bean.RespBean;
import com.st.mall.common.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @GetMapping
    public RespBean selectByCondition(Order condition, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 5 : pageSize;

        PageInfo<Order> pageInfo = orderService.selectByCondition(condition, pageNum, pageSize);
        return RespBean.ok("查询成功", pageInfo);
    }

    @GetMapping("/{id}")
    public RespBean selectById(@PathVariable("id") Integer id) {
        Order order = orderService.selectById(id);
        return RespBean.ok("", order);
    }
}
