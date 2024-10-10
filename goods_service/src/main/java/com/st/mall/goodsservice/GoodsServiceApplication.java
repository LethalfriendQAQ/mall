package com.st.mall.goodsservice;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//scanBasePackages - 让SpringBoot扫描特定的包
@SpringBootApplication(scanBasePackages = {"com.st.mall"})
@MapperScan(basePackages = "com.st.mall.goodsservice.mapper")
@EnableDubbo
public class GoodsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodsServiceApplication.class, args);
    }

}
