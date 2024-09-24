package com.st.mall.common.service;

import com.st.mall.common.bean.Category;
import com.st.mall.common.exception.StException;

import java.util.List;

public interface CategoryService {
    //添加
    boolean insert(Category category) throws StException;
    //删除
    boolean delete(Integer id) throws StException;
    //修改
    boolean update(Category category) throws StException;
    //条件搜索
    Object selectByCondition(Category condition, Integer pageNum, Integer pageSize);
    //根据id查询
    List<Category> selectByParentId(Integer parentId);
    Category selectById(Integer id);
}
