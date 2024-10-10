package com.st.mall.orderservice.service;

import com.st.mall.common.bean.Goods;
import com.st.mall.common.bean.OrderDetail;
import com.st.mall.common.service.GoodsService;
import com.st.mall.common.service.OrderDetailService;
import com.st.mall.orderservice.mapper.OrderDetailMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @DubboReference
    private GoodsService goodsService;
    @Override
    public OrderDetail selectById(Integer id) {
        OrderDetail orderDetail = orderDetailMapper.selectById(id);
        Goods goods = goodsService.selectById(orderDetail.getGoodsId());
        orderDetail.setGoods(goods);
        return orderDetail;
    }

    @Override
    public List<OrderDetail> selectByOrderId(String id) {
        List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(id);
        orderDetails.stream()
                .forEach(orderDetail -> {
                    Goods goods = goodsService.selectById(orderDetail.getGoodsId());
                    orderDetail.setGoods(goods);
                });
        return orderDetails;
    }
}
