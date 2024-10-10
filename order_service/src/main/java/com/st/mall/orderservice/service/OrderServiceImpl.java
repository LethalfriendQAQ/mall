package com.st.mall.orderservice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.st.mall.common.bean.Order;
import com.st.mall.common.bean.OrderDetail;
import com.st.mall.common.bean.User;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.OrderDetailService;
import com.st.mall.common.service.OrderService;
import com.st.mall.common.service.UserService;
import com.st.mall.orderservice.mapper.OrderMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailService orderDetailService;
    @DubboReference
    private UserService userService;
    @Override
    public void insert(Order order) throws StException {

    }

    @Override
    public void delete(Integer id) throws StException {

    }

    @Override
    public void update(Order order) throws StException {

    }

    @Override
    public Order selectById(String id) {
        Order order = orderMapper.selectById(id);
        List<OrderDetail> orderDetails = orderDetailService.selectByOrderId(id);
        User user = userService.selectById(order.getUserId());
        order.setUser(user);
        order.setOrderDetails(orderDetails);
        return order;
    }

    @Override
    public PageInfo<Order> selectByCondition(Order condition, Integer pageNum, Integer pageSize) {
        //设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        //查询
        List<Order> orders = orderMapper.selectByCondition(condition);
        //创建分页信息
        PageInfo<Order> pageInfo = new PageInfo<>(orders);
        return pageInfo;
    }
}
