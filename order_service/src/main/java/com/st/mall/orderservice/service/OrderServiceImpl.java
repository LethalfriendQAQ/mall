package com.st.mall.orderservice.service;

import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.st.mall.common.bean.*;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.*;
import com.st.mall.orderservice.mapper.CartMapper;
import com.st.mall.orderservice.mapper.OrderDetailMapper;
import com.st.mall.orderservice.mapper.OrderMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@DubboService
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private OrderDetailService orderDetailService;
    @DubboReference
    private UserService userService;
    @DubboReference
    private AddrService addrService;
    @DubboReference
    private GoodsService goodsService;
    @Override
    public void insert(OrderVo orderVo) throws StException {
        /**
         * 1. 生成订单
         *  1）生成订单id - 雪花算法生成
         *  2）获取地址对象
         *  3）设置地址详情
         *  4）添加订单
         * 2. 生成详情
         * 3. 删除购物车
         */
        //创建订单对象
        Order order = new Order();
        //生成订单id - 雪花算法生成
        String orderId = IdUtil.getSnowflakeNextIdStr();
        //设置订单id
        order.setId(orderId);

        //获取地址对象
        Addr addr = addrService.selectById(orderVo.getAddrId());
        //设置地址详情
        order.setAddrDetail(
                addr.getContact() + " " +
                addr.getPhone() + " " +
                addr.getProvince() +
                addr.getCity() +
                addr.getDistrict() +
                addr.getStreet() +
                addr.getAddress()
        );
        //添加订单
        order.setUserId(orderVo.getUserId());
        order.setPayType(0);
        order.setAddrId(orderVo.getAddrId());
        order.setStatus(0);
        orderMapper.insert(order);

        //生成详情 每个购物车的id对应一个商品详情
        for (Integer cartId : orderVo.getCartIds()) {
            //获取购物车 - 判断购物车是否合法
            Cart cart = cartMapper.selectById(cartId);
            if (cart == null) {
                throw new StException("购物车不存在无法生成订单");
            }
            if (!cart.getUserId().equals(orderVo.getUserId())) {
                throw new StException("购物车不合法无法生成订单");
            }
            //获取商品信息
            Goods goods = goodsService.selectById(cart.getGoodsId());
            //是否存在
            if (goods == null) {
                throw new StException("商品不存在无法生成订单");
            }
            //是否下架
            if (goods.getStatus().equals(0)) {
                throw new StException("商品下架无法生成订单");
            }
            //库存是否充足
            if (goods.getCount() < cart.getCount()) {
                throw new StException("商品库存不足无法生成订单");
            }
            //减去商品库存
            goods.setCount(goods.getCount() - cart.getCount());
            goodsService.update(goods);
            //添加详情
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setCount(cart.getCount());
            orderDetail.setPrice(goods.getPrice());
            orderDetail.setOrderId(orderId);
            orderDetail.setGoodsId(goods.getId());
            orderDetailMapper.insert(orderDetail);
        }
        //删除购物车
        cartMapper.deleteByIds(Arrays.asList(orderVo.getCartIds()));
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
    public List<Order> selectByUserId(Integer userId) {
        List<Order> orderList = orderMapper.selectByUserId(userId);
        orderList.stream()
                .forEach(order -> {
                    List<OrderDetail> orderDetails = orderDetailService.selectByOrderId(order.getId());
                    order.setOrderDetails(orderDetails);
                });
        return orderList;
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

    @Override
    public List<Order> selectByCondition(Order condition) {
        return orderMapper.selectByCondition(condition);
    }
}
