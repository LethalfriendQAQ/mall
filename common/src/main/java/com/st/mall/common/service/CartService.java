package com.st.mall.common.service;

import com.st.mall.common.bean.Cart;
import com.st.mall.common.exception.StException;

import java.util.List;

public interface CartService {
    boolean insertOrUpdate(Cart cart);
    boolean delete(Integer id, Integer userId) throws StException;
    boolean update(Cart cart, Integer userId) throws StException;
    List<Cart> search(Cart condition);

    Cart selectById(Integer id);
}
