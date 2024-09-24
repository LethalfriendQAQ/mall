package com.st.mall.goodsservice;

import com.st.mall.common.bean.Goods;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.GoodsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class MyTest02 {
    @Autowired
    private GoodsService goodsService;

    @Test
    public void test1() throws StException {
        Goods goods = new Goods(null, "qqqq",
                "aaaa", "aaa",
                new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"),
                "1", "1", 0, 0, 3, 0F, 1, null, null);
        goodsService.insert(goods);
    }
    @Test
    public void test2() throws StException {
        Goods goods = new Goods();
        goods.setId(506);
        goods.setColor("2");
        goodsService.update(goods);
    }
}
