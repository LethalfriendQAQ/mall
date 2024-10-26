package com.st.mall.goodsservice;

import com.github.pagehelper.PageInfo;
import com.st.mall.common.bean.Category;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.CategoryService;
import com.st.mall.goodsservice.mapper.CategoryMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class GoodsServiceApplicationTests {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryService categoryService;

    @Test
    public void test11() {
        List<Map<String, Object>> goodsCountByCategory = categoryMapper.getGoodsCountByCategory();
        for (Map<String, Object> categoryInfo : goodsCountByCategory) {
            // 提取每个父分类的信息
            String parentCategoryName = (String) categoryInfo.get("parent_category_name");
            Long goodsCount = (Long) categoryInfo.get("goods_count");

            // 输出每个父分类的商品数量信息
            System.out.println("父分类名称: " + parentCategoryName);
            System.out.println("商品数量: " + goodsCount);
            System.out.println("-----------------------");
        }

    }



    @Test
    public void test1() {
        System.out.println(categoryMapper.selectById(1));
    }

    @Test
    public void test2() {
        Category category = new Category();
        category.setName("手机");
        PageInfo<Category> pageInfo = (PageInfo<Category>) categoryService.selectByCondition(category, 1, 5);
        pageInfo.getList()
                .stream()
                .forEach(System.out::println);
    }

    @Test
    public void test3() {
        Category category = new Category();
        category.setParentId(0);
        List<Category> categoryList = (List<Category>) categoryService.selectByCondition(category, null, null);
        categoryList.stream()
                .forEach(System.out::println);
    }

    //@Test
    //public void test4() throws StException {
    //    Category category = new Category(null, "1212", "111", "111", 0, 0, 0, null, null);
    //    categoryService.insert(category);
    //}
    @Test
    public void test5() throws StException {
        categoryService.delete(38);
    }
    //@Test
    //public void test6() throws StException {
    //    Category category = new Category(36, "手机", "111", "111", 0, 0, 0, null, null);
    //    categoryService.update(category);
    //}
}
