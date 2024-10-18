package com.st.mall.orderservice.controller;

import com.github.pagehelper.PageInfo;
import com.st.mall.common.bean.Order;
import com.st.mall.common.bean.OrderVo;
import com.st.mall.common.bean.RespBean;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.OrderService;
import com.st.mall.common.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    //addrId  cartIds[]
    @PostMapping
    public RespBean insert(@RequestBody OrderVo orderVo, @RequestHeader("token") String token) throws StException {
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        Integer userId = (Integer) map.get("id");

        orderVo.setUserId(userId);
        //添加订单
        orderService.insert(orderVo);
        return RespBean.ok("添加订单成功", orderVo);
    }
    @GetMapping
    public RespBean selectByCondition(Order condition, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 5 : pageSize;

        PageInfo<Order> pageInfo = orderService.selectByCondition(condition, pageNum, pageSize);
        return RespBean.ok("查询成功", pageInfo);
    }
    @GetMapping("/user")
    public RespBean selectByUser(Integer pageNum, Integer pageSize, @RequestHeader("token") String token) {
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        Integer userId = (Integer) map.get("id");

        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 5 : pageSize;

        Order condition = new Order();
        condition.setUserId(userId);

        PageInfo<Order> pageInfo = orderService.selectByCondition(condition, pageNum, pageSize);
        return RespBean.ok("查询成功", pageInfo);
    }

    @GetMapping("/{id}")
    public RespBean selectById(@PathVariable("id") String id, @RequestHeader("token") String token) throws StException {
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        Integer userId = (Integer) map.get("id");
        Order order = orderService.selectById(id, userId);
        return RespBean.ok("根据id查询订单成功", order);
    }
    @GetMapping("/selectByUserId")
    public RespBean selectByUserId(@RequestHeader("token") String token) {
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        Integer userId = (Integer) map.get("id");

        List<Order> orders = orderService.selectByUserId(userId);
        return RespBean.ok("查询成功", orders);
    }
    @GetMapping("/adminSelectById/{id}")
    public RespBean adminSelectById(@PathVariable("id") String id) throws StException {
        Order order = orderService.adminSelectById(id);
        return RespBean.ok("查询成功", order);
    }
    @PutMapping("/adminUpdateStatus")
    public RespBean adminUpdateStatus(@RequestParam String id, @RequestParam Integer status) throws StException {
        orderService.updateStatus(id, status);
        return RespBean.ok("支付成功");
    }


    @PostMapping("/pay")
    public RespBean pay(@RequestBody OrderVo orderVo, @RequestHeader("token") String token) throws StException {
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        Integer userId = (Integer) map.get("id");
        orderVo.setUserId(userId);
        orderService.pay(orderVo);
        return RespBean.ok("支付成功");
    }
}
