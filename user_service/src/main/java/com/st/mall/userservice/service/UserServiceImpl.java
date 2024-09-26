package com.st.mall.userservice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.st.mall.common.bean.User;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.UserService;
import com.st.mall.userservice.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public void insert(User user) throws StException {
        //判断添加的用户名有没有重复
        User condition = new User();
        condition.setUsername(user.getUsername());
        if (userMapper.selectByCondition(condition)
                .stream()
                .anyMatch(item -> item.getUsername().equals(user.getUsername()))) {
            throw new StException("该用户名已存在");
        }
        user.setMoney(new BigDecimal(String.valueOf(user.getMoney())).setScale(2, RoundingMode.HALF_UP));
        userMapper.insert(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer id) {
        userMapper.delete(id);
    }

    @Override
    public void update(User user) throws StException {
        //判断修改后的用户名是否重复
        User condition = new User();
        condition.setUsername(user.getUsername());
        if (userMapper.selectByCondition(condition)
                .stream()
                .anyMatch(item -> item.getUsername().equals(user.getUsername()))) {
            throw new StException("该用户名已存在");
        }
        userMapper.update(user);
    }

    @Override
    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public PageInfo<User> selectByCondition(User condition, Integer pageNum, Integer pageSize) {
        //设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        //查询
        List<User> users = userMapper.selectByCondition(condition);
        //创建分页信息
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return pageInfo;
    }
}
