package com.st.mall.adminservice.service;

import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.st.mall.adminservice.mapper.AdminMapper;
import com.st.mall.common.bean.Admin;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public boolean insert(Admin admin) throws StException {
        //判断添加的用户名有没有重复
        Admin condition = new Admin();
        condition.setUsername(admin.getUsername());
        if (adminMapper.selectByCondition(condition)
                .stream()
                .anyMatch(item -> item.getUsername().equals(admin.getUsername()))) {
            throw new StException("该用户名已存在");
        }
        return adminMapper.insert(admin) == 1;
    }

    @Override
    public boolean update(Admin admin) throws StException {
        //判断修改后的用户名是否重复
        Admin condition = new Admin();
        condition.setUsername(admin.getUsername());
        if (adminMapper.selectByCondition(condition)
                .stream()
                .anyMatch(item -> item.getUsername().equals(admin.getUsername()))) {
            throw new StException("该用户名已存在");
        }
        return adminMapper.update(admin) == 1;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(Integer id) {
        return adminMapper.delete(id) == 1;
    }

    @Override
    public Admin selectById(Integer id) {
        return adminMapper.selectById(id);
    }

    @Override
    public PageInfo<Admin> selectByCondition(Admin condition, Integer pageNum, Integer pageSize) {
        //设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        //查询
        List<Admin> admins = adminMapper.selectByCondition(condition);
        //创建分页信息
        PageInfo<Admin> pageInfo = new PageInfo<>(admins);
        return pageInfo;
    }

    @Override
    public List<Admin> selectAll() {
        return adminMapper.selectAll();
    }

    @Override
    public Admin login(String username, String password) throws StException {
        //根据用户名查询
        Admin admin = adminMapper.selectByUsername(username);
        if (admin == null) {
            throw new StException("用户名错误");
        }
        //对用户输入的密码进行加密 - 使用MD5算法和盐进行加密
        String md5Pwd = SecureUtil.md5(SecureUtil.md5(password + admin.getSalt()));
        //对加密之后的密码和数据库中的密码进行比较
        if (!md5Pwd.equals(admin.getPassword())) {
            throw new StException("密码错误");
        }
        return admin;
    }
}
