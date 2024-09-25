package com.st.mall.adminservice;

import com.st.mall.adminservice.mapper.AdminMapper;
import com.st.mall.common.bean.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@SpringBootTest
public class MyTest01 {
    @Autowired
    private AdminMapper adminMapper;


    @Test
    public void test1() {
        adminMapper.selectAll()
                .stream()
                .forEach(System.out::println);
    }

    @Test
    public void test2() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Admin admin = new Admin(null, "121", "1212", "1212", "1111", "1111", "1", sdf.parse("2024-05-08 10:01:01"), 0);
        adminMapper.insert(admin);
    }

    @Test
    public void test3() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Admin admin = new Admin(8, "233", "1212", "1212", "1111", "1111", "1", sdf.parse("2024-05-08 10:01:01"), 0);
        adminMapper.update(admin);
    }
    @Test
    public void test4() {
        Admin condition = new Admin();
        condition.setRealname("杰克");
        adminMapper.selectByCondition(condition)
                .stream()
                .forEach(System.out::println);
    }
}
