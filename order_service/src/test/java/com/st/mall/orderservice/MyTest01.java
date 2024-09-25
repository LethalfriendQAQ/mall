package com.st.mall.orderservice;

import com.st.mall.orderservice.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MyTest01 {
    @Autowired
    private OrderMapper orderMapper;

    //@Test
    //public void test1() throws ParseException {
    //    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //    Order order = new Order(
    //            "11",
    //            1,
    //            sdf.parse("2024-05-08 10:01:01"),
    //            "11",
    //            1,
    //            1,
    //            "11",
    //            1
    //    );
    //    orderMapper.insert(order);
    //}
}
