package com.st.mall.orderservice.mapper;

import com.st.mall.common.bean.Addr;

import java.util.List;

public interface AddrMapper {
    int insert(Addr addr);

    int delete(Integer  id);

    int update(Addr addr);
    List<Addr> selectByUserId(Integer userId);

    Addr selectById(Integer id);
}
