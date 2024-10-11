package com.st.mall.userservice.mapper;

import com.st.mall.common.bean.Addr;

import java.util.List;

public interface AddrMapper {
    int insert(Addr addr);

    int delete(Integer  id);

    int update(Addr addr);
    List<Addr> selectByUserId(Integer userId);
    List<Addr> selectByCondition(Addr condition);

    Addr selectById(Integer id);
}
