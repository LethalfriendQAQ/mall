package com.st.mall.orderservice.service;

import com.st.mall.common.bean.Cart;
import com.st.mall.common.bean.Goods;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.CartService;
import com.st.mall.common.service.GoodsService;
import com.st.mall.orderservice.mapper.CartMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;
    @DubboReference
    private GoodsService goodsService;


    @Override
    public boolean insertOrUpdate(Cart cart) {
        //查询购物车中是否存在该记录 1. 存在 - 累加 2. 不存在 - 添加
        Cart  c = cartMapper.selectByUserIdAndGoodsId(cart.getUserId(), cart.getGoodsId());
        if (c == null) {
            //添加
            cart.setCount(1);
            return cartMapper.insert(cart) == 1;
        } else {
            //修改 - 累加
            c.setCount(c.getCount() + 1);
            return cartMapper.update(c) == 1;
        }
    }

    @Override
    public boolean delete(Integer id, Integer userId) throws StException {
        //判断购物车是否存在
        Cart cart = cartMapper.selectById(id);
        if (cart == null) {
            throw new StException("该购物车不存在！");
        }
        //判断被删除的购物车是否属于当前的用户
        if (!cart.getUserId().equals(userId)) {
            throw new StException("该购物车属于其他用户！");
        }
        //删除
        return cartMapper.delete(id) == 1;
    }

    @Override
    public boolean deleteByIds(List<Integer> ids, Integer userId) throws StException {
        for (Integer id : ids) {
            //判断购物车是否存在
            Cart cart = cartMapper.selectById(id);
            if (cart == null) {
                throw new StException("该购物车不存在！");
            }
            //判断被删除的购物车是否属于当前的用户
            if (!cart.getUserId().equals(userId)) {
                throw new StException("该购物车属于其他用户！");
            }
        }

        return cartMapper.deleteByIds(ids) == ids.size();
    }

    @Override
    public boolean update(Cart cart, Integer userId) throws StException {
        //判断购物车是否存在
        Cart c = cartMapper.selectById(cart.getId());
        if (c == null) {
            throw new StException("该购物车记录不存在无法修改");
        }
        if (!c.getUserId().equals(userId)) {
            throw new StException("该购物车属于其他用户！");
        }
        //判断库存是否充足
        Goods goods = goodsService.selectById(c.getGoodsId());
        if (cart.getCount() != null && cart.getCount() > goods.getCount()) {
            //当商品数量超过库存，就设置购物车数量为库存
            cart.setCount(goods.getCount());
        }
        return cartMapper.update(cart) == 1;
    }

    @Override
    public List<Cart> search(Cart condition) {
        List<Cart> carts = cartMapper.selectByCondition(condition);
        //调用goods_service查询购物车对应的商品
        carts.stream()
                .forEach(cart -> {
                    Goods goods = goodsService.selectById(cart.getGoodsId());
                    cart.setGoods(goods);
                });
        return carts;
    }

    @Override
    public Cart selectById(Integer id) {
        return cartMapper.selectById(id);
    }
}
