package com.st.mall.common.service;

import com.github.pagehelper.PageInfo;
import com.st.mall.common.bean.Goods;
import com.st.mall.common.exception.StException;

public interface GoodsService {
    //添加
    void insert(Goods goods) throws StException;

    //根据ID删除
    void delete(Integer id) throws StException;

    //根据ID修改
    void update(Goods goods);

    //根据ID查询
    Goods selectById(Integer id);

    //搜索 - 分页查询
    PageInfo<Goods> selectByConfition(Goods condition);
}
