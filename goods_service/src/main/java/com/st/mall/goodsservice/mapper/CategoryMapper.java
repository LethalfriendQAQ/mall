package com.st.mall.goodsservice.mapper;

import com.st.mall.common.bean.Category;

import java.util.List;
import java.util.Map;

public interface CategoryMapper {
    //添加
    int insert(Category category);

    //根据分类id删除
    int delete(Integer id);

    //根据分类id修改
    int update(Category category);

    //根据条件查询
    List<Category> selectByCondition(Category condition);

    //根据分类id查询
    Category selectById(Integer id);

    //根据父分类的id进行查询
    List<Category> selectByParentId(Integer parentId);
    List<Map<String, Object>> getGoodsCountByCategory();
}
