package com.st.mall.goodsservice;

import com.st.mall.common.bean.Goods;
import com.st.mall.common.bean.GoodsPic;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.GoodsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;

@SpringBootTest
public class MyTest02 {
    @Autowired
    private GoodsService goodsService;

    @Test
    public void test1() throws StException {
        Goods goods = new Goods(null, "qqqq",
                "aaaa", "aaa",
                new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"),
                "2", "1", 0, 0, 3, 0F, 1, null, null);
        goodsService.insert(goods);
    }
    @Test
    public void test2() throws StException {
        Goods goods = new Goods();
        goods.setId(506);
        goods.setColor("1");
        ArrayList<GoodsPic> goodsPicList = new ArrayList<>();
        goodsPicList.add(new GoodsPic(null, "AAA4", null));
        goodsPicList.add(new GoodsPic(null, "AAA5", null));
        goodsPicList.add(new GoodsPic(null, "AAA6", null));
        goods.setPicList(goodsPicList);
        goodsService.update(goods);
    }
}
