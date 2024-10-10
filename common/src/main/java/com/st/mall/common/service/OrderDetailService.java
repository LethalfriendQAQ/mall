package com.st.mall.common.service;

import com.st.mall.common.bean.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    OrderDetail selectById(Integer id);
    List<OrderDetail> selectByOrderId(String id);
}
