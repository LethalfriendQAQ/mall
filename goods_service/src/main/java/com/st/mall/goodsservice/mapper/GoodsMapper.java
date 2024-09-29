package com.st.mall.goodsservice.mapper;

import com.st.mall.common.bean.Goods;

import java.util.List;

public interface GoodsMapper {
    int insert(Goods goods);

    int delete(Integer id);

    int update(Goods goods);

    List<Goods> selectByCondition(Goods condition);

    Goods selectById(Integer id);

    //根据分类的id查询商品 - 这个分类id可能是父分类的id也可能是子分类的id
    List<Goods> selectByCategoryId(Integer categoryId);
}
