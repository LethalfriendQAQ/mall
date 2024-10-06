package com.st.mall.userservice;

import com.st.mall.common.bean.User;
import com.st.mall.common.service.UserService;
import com.st.mall.userservice.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MyTest01 {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;
    //@Test
    //public void test11() throws ParseException, StException {
    //    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //    // 构造 User 对象
    //    User user = new User(
    //            null, // id
    //            "12", // username
    //            "11", // password
    //            "11", // payPassword
    //            "11", // salt
    //            "1",  // realname
    //            "1",  // sex
    //            "11", // idCard
    //            "11", // phone
    //            "11", // email
    //            sdf.parse("2024-05-08 10:01:01"), // regTime
    //            BigDecimal.valueOf(1000), // money
    //            1 // status
    //    );
    //    userService.insert(user);
    //}

    //@Test
    //public void test12() throws ParseException, StException {
    //    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //    // 构造 User 对象
    //    User user = new User(
    //            2, // id
    //            "32", // username
    //            "11", // password
    //            "11", // payPassword
    //            "11", // salt
    //            "1",  // realname
    //            "1",  // sex
    //            "11", // idCard
    //            "11", // phone
    //            "11", // email
    //            sdf.parse("2024-05-08 10:01:01"), // regTime
    //            BigDecimal.valueOf(1000), // money
    //            1 // status
    //    );
    //    userService.update(user);
    //}
    //@Test
    //public void test1() throws ParseException {
    //    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //    // 构造 User 对象
    //    User user = new User(
    //            null, // id
    //            "11", // username
    //            "11", // password
    //            "11", // payPassword
    //            "11", // salt
    //            "1",  // realname
    //            "1",  // sex
    //            "11", // idCard
    //            "11", // phone
    //            "11", // email
    //            sdf.parse("2024-05-08 10:01:01"), // regTime
    //            BigDecimal.valueOf(1000), // money
    //            1 // status
    //    );
    //    userMapper.insert(user);
    //}
    //
    //@Test
    //public void test2() throws ParseException {
    //    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //    // 构造 User 对象
    //    User user = new User(
    //            2, // id
    //            "22", // username
    //            "11", // password
    //            "11", // payPassword
    //            "11", // salt
    //            "1",  // realname
    //            "1",  // sex
    //            "11", // idCard
    //            "11", // phone
    //            "11", // email
    //            sdf.parse("2024-05-08 10:01:01"), // regTime
    //            BigDecimal.valueOf(1000), // money
    //            1 // status
    //    );
    //    userMapper.update(user);
    //}
    @Test
    public void test3() {
        User condition = new User();
        condition.setUsername("t");
        userMapper.selectByCondition(condition)
                .stream()
                .forEach(System.out::println);
    }
    @Test
    public void test4() {
        userMapper.delete(3);
    }
}
