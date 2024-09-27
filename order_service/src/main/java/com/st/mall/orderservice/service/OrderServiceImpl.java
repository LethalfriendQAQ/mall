package com.st.mall.orderservice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.st.mall.common.bean.Order;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.OrderService;
import com.st.mall.orderservice.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
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
    public Order selectById(Integer id) {
        return orderMapper.selectById(id);
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
