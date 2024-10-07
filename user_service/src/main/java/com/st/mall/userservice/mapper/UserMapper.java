package com.st.mall.userservice.mapper;

import com.st.mall.common.bean.User;

import java.util.List;

public interface UserMapper {
    int insert(User user);

    int delete(Integer id);

    int update(User user);

    List<User> selectByCondition(User condition);

    User selectById(Integer id);
    User selectById1(Integer id);
}
