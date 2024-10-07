package com.st.mall.orderservice.mapper;

import com.st.mall.common.bean.OrderDetail;

import java.util.List;

public interface OrderDetailMapper {
    int insert(OrderDetail orderDetail);

    int delete(Integer id);

    int update(OrderDetail orderDetail);

    List<OrderDetail> selectByCondition(OrderDetail condition);

    OrderDetail selectById(Integer id);
    OrderDetail selectByOrderId(Integer id);
}
