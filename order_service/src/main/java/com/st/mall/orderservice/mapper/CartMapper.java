package com.st.mall.orderservice.mapper;

import com.st.mall.common.bean.Cart;
import org.apache.ibatis.annotations.Param;

public interface CartMapper {
    int insert(Cart cart);
    int update(Cart cart);
    //根据用户id和根据商品id查询
    Cart selectByUserIdAndGoodsId(@Param("userId") Integer userId, @Param("goodsId")Integer goodsId);
    Cart selectById(Integer id);
}
