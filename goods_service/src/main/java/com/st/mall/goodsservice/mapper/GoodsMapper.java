package com.st.mall.goodsservice.mapper;

import com.st.mall.common.bean.Goods;

import java.util.List;

public interface GoodsMapper {
    int insert(Goods goods);

    int delete(Integer id);

    int update(Goods goods);

    List<Goods> selectByCondition(Goods condition);

    Goods selectById(Integer id);
}
