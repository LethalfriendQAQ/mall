package com.st.mall.goodsservice;

import com.st.mall.common.config.WhiteListConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MyTest03 {
    @Autowired
    private WhiteListConfig whiteListConfig;

    @Test
    public void test1(){
        whiteListConfig.getRules()
                .stream()
                .forEach(System.out::println);
    }

    @Test
    public void test2(){
        String path1 = "/goods/1";
        String path2 = "/goods/2";
        String path3 = "/goods";

        //要求：以/goods开始
        String reg = "^/goods.*";
        System.out.println(path1.matches(reg));
        System.out.println(path2.matches(reg));
        System.out.println(path3.matches(reg));
    }
    @Test
    public void test3(){
        String reg = "^/category/[\\\\d]+";
        System.out.println("/category/1".matches(reg));
        System.out.println("/category/11".matches(reg));
        System.out.println("/category/111".matches(reg));
    }
}
