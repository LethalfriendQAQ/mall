package com.st.mall.goodsservice;

import com.st.mall.goodsservice.mapper.CategoryMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GoodsServiceApplicationTests {
    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    public void test1() {
        System.out.println(categoryMapper.selectById(1));
    }

}
