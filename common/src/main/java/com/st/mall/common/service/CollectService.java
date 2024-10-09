package com.st.mall.common.service;

import com.github.pagehelper.PageInfo;
import com.st.mall.common.bean.Collect;
import com.st.mall.common.exception.StException;

public interface CollectService {
    boolean insert(Collect collect) throws StException;
    //删除 - 只能删除自己的收藏
    boolean delete(Integer id, Integer userId) throws StException;

    Collect selectById(Integer id);

    Collect selectByGoodsIdAndUserId(Integer goodsId, Integer userId);

    PageInfo<Collect> selectByPage(Integer pageNum, Integer pageSize, Integer userId);
}
