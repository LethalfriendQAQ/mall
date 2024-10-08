package com.st.mall.userservice.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
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
    public User login(String username, String password) throws StException {
        //根据用户名查询判断用户是否存在
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new StException("该用户不存在");
        }
        //判断用户是否禁用或注销
        if (user.getStatus().equals(2) || user.getStatus().equals(3)) {
            throw new StException("该用户已被禁用或注销");
        }
        //对用户输入的密码进行加密 - 使用MD5算法和盐进行加密
        String md5Pwd = SecureUtil.md5(SecureUtil.md5(password + user.getSalt()));
        //对加密之后的密码和数据库中的密码进行比较
        if (!md5Pwd.equals(user.getPassword())) {
            throw new StException("密码错误");
        }
        return user;
    }

    //@Override
    public void reg(User user) {
        //判断该用户名在系统中是否存在
        //生成盐
        //对密码进行加密
        //添加用户
    }

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
        BigDecimal money = user.getMoney() == null ? BigDecimal.ZERO : new BigDecimal(user.getMoney().toString());
        user.setMoney(money.setScale(2, RoundingMode.HALF_UP));
        user.setStatus(0);
        user.setRegTime(new java.sql.Timestamp(System.currentTimeMillis()));
        // 生成雪花 ID 字符串
        String id = IdUtil.getSnowflakeNextIdStr();
        // 取前八位
        user.setSalt(id.length() > 8 ? id.substring(0, 8) : id);
        user.setPassword(SecureUtil.md5(SecureUtil.md5(user.getPassword() + user.getSalt())));
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
