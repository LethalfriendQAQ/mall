package com.st.mall.adminservice.mapper;

import com.st.mall.common.bean.Admin;

import java.util.List;

public interface AdminMapper {
    int insert(Admin admin);

    int update(Admin admin);

    int delete(Integer id);

    Admin selectById(Integer id);
    List<Admin> selectByCondition(Admin condition);

    List<Admin> selectAll();

    Admin selectByUsername(String username);
}
