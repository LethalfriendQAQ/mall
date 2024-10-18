package com.st.mall.orderservice.mapper;

import com.st.mall.common.bean.Order;

import java.util.List;

public interface OrderMapper {
    int insert(Order order);

    int delete(String  id);

    int update(Order order);
    int updateStatus(Order order);

    List<Order> selectByCondition(Order condition);
    List<Order> selectByUserId(Integer userId);

    Order selectById(String id);
}
