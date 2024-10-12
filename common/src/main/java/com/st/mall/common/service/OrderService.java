package com.st.mall.common.service;

import com.github.pagehelper.PageInfo;
import com.st.mall.common.bean.Order;
import com.st.mall.common.exception.StException;

import java.util.List;

public interface OrderService {
    //添加
    void insert(Order order) throws StException;

    //根据ID删除
    void delete(Integer id) throws StException;

    //根据ID修改
    void update(Order order) throws StException;

    //根据ID查询
    Order selectById(String  id);
    List<Order> selectByUserId(Integer userId);

    //搜索 - 分页查询
    PageInfo<Order> selectByCondition(Order condition, Integer pageNum, Integer pageSize);
    List<Order> selectByCondition(Order condition);
}
