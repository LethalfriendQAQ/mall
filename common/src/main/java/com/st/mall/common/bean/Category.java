package com.st.mall.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//表示商品分类的实体类
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private Integer id;
    private String name;//名称
    private String dscp;//描述
    private String pic;//图片
    private Integer parentId;//父分类id
    private Integer recom;//是否推荐 0-不推荐， 1-推荐
    private Integer status;//是否上架 0-下架， 1-上架

    private Category parent;//父分类
    private List<Category> childList; //当前分类下所有的子分类
}
