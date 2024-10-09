package com.st.mall.common.service;

import com.st.mall.common.bean.Cart;
import com.st.mall.common.exception.StException;

public interface CartService {
    boolean insertOrUpdate(Cart cart);
    boolean update(Cart cart) throws StException;
}
