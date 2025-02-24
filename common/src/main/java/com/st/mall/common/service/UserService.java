package com.st.mall.common.service;

import com.github.pagehelper.PageInfo;
import com.st.mall.common.bean.User;
import com.st.mall.common.exception.StException;

public interface UserService {
    //登录
    User login(String username, String password) throws StException;

    //注册
    void reg(User user);
    //添加
    void insert(User user) throws StException;

    //根据ID删除
    void delete(Integer id) throws StException;

    //根据ID修改
    void update(User user) throws StException;
    void byPwdUpdate(User user) throws StException;
    void cancelAccount(User user) throws StException;

    //根据ID查询
    User selectById(Integer id);

    //搜索 - 分页查询
    PageInfo<User> selectByCondition(User condition, Integer pageNum, Integer pageSize);
    //修改密码
    boolean changePassword(String oldPwd, String newPwd, Integer id) throws StException;
    boolean changePayPassword(String oldPwd, String newPwd, Integer id) throws StException;
}
