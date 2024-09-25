package com.st.mall.adminservice.mapper;

import com.st.mall.common.bean.Admin;

import java.util.List;

public interface AdminMapper {
    int insert(Admin admin);

    int update(Admin admin);
    List<Admin> selectByCondition(Admin condition);

    List<Admin> selectAll();
}
