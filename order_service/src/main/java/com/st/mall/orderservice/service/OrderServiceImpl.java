package com.st.mall.orderservice.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
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

import java.math.BigDecimal;
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


    //生成订单
    //@GlobalTransactional(rollbackFor = Exception.class)
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

                addr.getProvince() +
                addr.getCity() +
                addr.getDistrict() +
                addr.getStreet() +
                addr.getAddress()+ " " +
                addr.getContact() + " " +
                addr.getPhone()
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

        orderVo.setOrderId(orderId);
    }

    @Override
    public void delete(Integer id) throws StException {

    }

    @Override
    public void update(Order order) throws StException {

    }

    @Override
    public Order selectById(String id, Integer userId) throws StException {
        Order order = orderMapper.selectById(id);
        //判断是否属于当前用户
        if (!order.getUserId().equals(userId)) {
            throw new StException("订单不属于当前用户无法支付");
        }
        List<OrderDetail> orderDetails = orderDetailService.selectByOrderId(id);
        User user = userService.selectById(order.getUserId());
        order.setUser(user);
        order.setOrderDetails(orderDetails);

        //查询订单详情对应的商品的信息
        for (OrderDetail orderDetail : orderDetails) {
            //使用dubbo调用goodsService查询
            goodsService.selectById(orderDetail.getGoodsId());
        }
        return order;
    }

    @Override
    public Order adminSelectById(String id) throws StException {
        Order order = orderMapper.selectById(id);
        List<OrderDetail> orderDetails = orderDetailService.selectByOrderId(id);
        User user = userService.selectById(order.getUserId());
        order.setUser(user);
        order.setOrderDetails(orderDetails);

        //查询订单详情对应的商品的信息
        for (OrderDetail orderDetail : orderDetails) {
            //使用dubbo调用goodsService查询
            goodsService.selectById(orderDetail.getGoodsId());
        }
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
        List<Order> orderList = orderMapper.selectByCondition(condition);

        orderList.stream()
                .forEach(order -> {
                    List<OrderDetail> orderDetails = orderDetailService.selectByOrderId(order.getId());
                    order.setOrderDetails(orderDetails);
                });
        //创建分页信息
        PageInfo<Order> pageInfo = new PageInfo<>(orderList);
        return pageInfo;
    }

    @Override
    public List<Order> selectByCondition(Order condition) {
        return orderMapper.selectByCondition(condition);
    }

    //支付
    //@GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public void pay(OrderVo orderVo) throws StException {
        //根据orderId查询订单
        Order order = orderMapper.selectById(orderVo.getOrderId());
        order.setOrderDetails(orderDetailMapper.selectByOrderId(order.getId()));
        //判断订单是否存在
        if (order == null) {
            throw new StException("订单不存在，无法支付");
        }
        //判断订单是否属于当前用户
        if (!order.getUserId().equals(orderVo.getUserId())) {
            throw new StException("订单不属于当前用户，无法支付");
        }
        //判断订单是否是未支付的状态 status.equals(0)
        if (!order.getStatus().equals(0)) {
            throw new StException("订单状态错误，无法支付");
        }


        //根据用户id查询用户
        User user = userService.selectById(orderVo.getUserId());
        //判断支付密码是否存在,支付密码是否正确
        String payPasswordSQl = user.getPayPassword();
        if (payPasswordSQl == null) {
            throw new StException("没有说设置支付密码，请设置之后再下单");
        }
        //对用户输入的密码进行加密
        String md5Pwd = SecureUtil.md5(SecureUtil.md5(orderVo.getPayPwd() + user.getSalt()));
        if (!md5Pwd.equals(payPasswordSQl)) {
            throw new StException("支付密码错误");
        }
        //计算当前订单的总金额
        BigDecimal sum = new BigDecimal("0");
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            BigDecimal count = new BigDecimal(orderDetail.getCount());
            BigDecimal price = orderDetail.getPrice();
            sum = sum.add(price.multiply(count));
        }
        //判断余额是否充足
        if (user.getMoney().compareTo(sum) < 0) {
            throw new StException("余额不足");
        }
        //扣除余额
        User u = new User();
        u.setId(user.getId());
        u.setMoney(user.getMoney().subtract(sum));
        userService.update(u);

        //设置status为1
        order.setStatus(1);
        //设置payType为0
        order.setPayType(0);
        //更新订单
        orderMapper.update(order);
    }
}
