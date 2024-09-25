package com.st.mall.adminservice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.st.mall.adminservice.mapper.AdminMapper;
import com.st.mall.common.bean.Admin;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public Admin selectById(Integer id) {
        return adminMapper.selectById(id);
    }

    @Override
    public Object selectByCondition(Admin condition, Integer pageNum, Integer pageSize) {
        //设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        //查询
        List<Admin> admins = adminMapper.selectByCondition(condition);
        //创建分页信息
        PageInfo<Admin> pageInfo = new PageInfo<>();
        return pageInfo;
    }

    @Override
    public List<Admin> selectAll() {
        return adminMapper.selectAll();
    }
}
