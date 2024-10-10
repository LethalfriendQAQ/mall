package com.st.mall.orderservice.mapper;

import com.st.mall.common.bean.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int insert(Cart cart);
    int update(Cart cart);
    int delete(Integer id);
    //根据用户id和根据商品id查询
    Cart selectByUserIdAndGoodsId(@Param("userId") Integer userId, @Param("goodsId")Integer goodsId);
    Cart selectById(Integer id);

    List<Cart> selectByCondition(Cart condition);
}
