package com.st.mall.orderservice.mapper;

import cn.hutool.db.sql.Order;

import java.util.List;

public interface OrderMapper {
    int insert(Order order);

    int delete(Integer id);

    int update(Order order);

    List<Order> selectByCondition(Order condition);

    Order selectById(Integer id);
}
