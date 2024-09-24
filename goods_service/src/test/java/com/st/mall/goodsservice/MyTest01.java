package com.st.mall.goodsservice;

import com.st.mall.common.bean.Goods;
import com.st.mall.common.bean.GoodsPic;
import com.st.mall.goodsservice.mapper.GoodsMapper;
import com.st.mall.goodsservice.mapper.GoodsPicMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
public class MyTest01 {
    @Autowired
    private GoodsPicMapper goodsPicMapper;
    @Autowired
    private GoodsMapper goodsMapper;

    @Test
    public void test11() {
        System.out.println(goodsMapper.selectById(1));
    }
    @Test
    public void test12() {
        Goods condition = new Goods();
        condition.setName("华为");
        goodsMapper.selectByCondition(condition)
                .stream()
                .forEach(System.out::println);
    }
    @Test
    public void test1() {
        goodsPicMapper.selectByGoodsId(4)
                .stream()
                .forEach(System.out::println);
    }

    //@Test
    //public void test2() {
    //    goodsPicMapper.delete()
    //}
    @Test
    public void test3() {
        ArrayList<GoodsPic> goodsPics = new ArrayList<>();
        goodsPics.add(new GoodsPic(null, "1111", 1000));
        goodsPics.add(new GoodsPic(null, "1112", 1000));
        goodsPics.add(new GoodsPic(null, "1113", 1000));
        goodsPics.add(new GoodsPic(null, "1114", 1000));
        goodsPicMapper.insert(goodsPics);
    }
}
