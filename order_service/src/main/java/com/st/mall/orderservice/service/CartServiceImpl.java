package com.st.mall.orderservice.service;

import com.st.mall.common.bean.Cart;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.CartService;
import com.st.mall.orderservice.mapper.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;

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
            return cartMapper.update(cart) == 1;
        }
    }

    @Override
    public boolean update(Cart cart) throws StException {
        //判断该购物车是否存在
        if (cartMapper.selectById(cart.getId()) == null) {
            throw new StException("该购物车记录不存在无法修改");
        }
        return cartMapper.update(cart) == 1;
    }
}
