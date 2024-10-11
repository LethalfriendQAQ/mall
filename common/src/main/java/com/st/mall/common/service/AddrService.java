package com.st.mall.common.service;

import com.st.mall.common.bean.Addr;
import com.st.mall.common.exception.StException;

import java.util.List;

public interface AddrService {
    int insert(Addr addr);

    int delete(Integer  id, Integer userId) throws StException;

    int update(Addr addr, Integer userId) throws StException;
    List<Addr> selectByCondition(Addr condition);

    Addr selectById(Integer id);
}
