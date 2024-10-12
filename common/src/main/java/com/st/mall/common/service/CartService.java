package com.st.mall.common.service;

import com.st.mall.common.bean.Cart;
import com.st.mall.common.exception.StException;

import java.util.List;

public interface CartService {
    boolean insertOrUpdate(Cart cart);
    boolean delete(Integer id, Integer userId) throws StException;
    boolean deleteByIds(List<Integer> ids, Integer userId) throws StException;
    boolean update(Cart cart, Integer userId) throws StException;
    List<Cart> search(Cart condition);
    List<Cart> selectByIds(Integer[] cardIds, Integer userId) throws StException;

    Cart selectById(Integer id);
}
