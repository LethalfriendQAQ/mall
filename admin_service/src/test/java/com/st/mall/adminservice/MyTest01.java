package com.st.mall.adminservice;

import cn.hutool.crypto.SecureUtil;
import com.st.mall.adminservice.mapper.AdminMapper;
import com.st.mall.common.bean.Admin;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@SpringBootTest
public class MyTest01 {
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @Test
    public void test21() throws ParseException, StException {
        adminService.changePassword("123", "admin", 1);
    }

    @Test
    public void test11() throws ParseException, StException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Admin admin = new Admin(null, "1212", "1212", "1212", "1111", "1111", "1", sdf.parse("2024-05-08 10:01:01"), 0);
        adminService.insert(admin);
    }

    @Test
    public void test12() throws ParseException, StException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Admin admin = new Admin(8, "122", "1212", "1212", "1111", "1111", "1", sdf.parse("2024-05-08 10:01:01"), 0);
        adminService.update(admin);
    }

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
    @Test
    public void test5() {
        System.out.println(adminMapper.selectById(2));
    }
    @Test
    public void test6() {
        String md5Pwd = SecureUtil.md5(SecureUtil.md5("123" + "981a735b"));
        System.out.println(md5Pwd);
    }
    @Test
    public void test7() throws StException {
        System.out.println(adminService.login("admin", "123"));
    }
}
