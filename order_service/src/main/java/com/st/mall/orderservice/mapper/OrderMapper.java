package com.st.mall.orderservice.mapper;

import com.st.mall.common.bean.Order;

import java.util.List;

public interface OrderMapper {
    int insert(Order order);

    int delete(Integer id);

    int update(Order order);

    List<Order> selectByCondition(Order condition);
    List<Order> selectByUserId(Integer userId);

    Order selectById(Integer id);
}
