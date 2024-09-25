package com.st.mall.common.service;

import com.github.pagehelper.PageInfo;
import com.st.mall.common.bean.Admin;
import com.st.mall.common.exception.StException;

import java.util.List;


public interface AdminService {
    //添加
    boolean insert(Admin admin) throws StException;
    //修改
    boolean update(Admin admin) throws StException;

    boolean delete(Integer id);
    Admin selectById(Integer id);
    //条件搜索
    PageInfo<Admin> selectByCondition(Admin condition, Integer pageNum, Integer pageSize);
    //查询所有
    List<Admin> selectAll();
}
