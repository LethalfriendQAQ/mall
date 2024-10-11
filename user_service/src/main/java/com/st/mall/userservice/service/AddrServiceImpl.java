package com.st.mall.userservice.service;

import com.st.mall.common.bean.Addr;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.AddrService;
import com.st.mall.userservice.mapper.AddrMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddrServiceImpl implements AddrService {
    @Autowired
    private AddrMapper addrMapper;

    @Override
    public int insert(Addr addr) {
        addr.setStatus(0);
        return addrMapper.insert(addr);
    }

    @Override
    public int delete(Integer id, Integer userId) throws StException {
        //判断这个地址是否存在
        Addr a = addrMapper.selectById(id);

        if (a == null) {
            throw new StException("该地址不存在无法删除");
        }
        //判断这个地址是否属于当前用户
        if (!a.getUserId().equals(userId)) {
            throw new StException("该地址属于其他用户，无法删除");
        }
        //删除
        return addrMapper.delete(id);
    }

    @Override
    public int update(Addr addr, Integer userId) throws StException {
        //判断这个地址是否存在
        Addr a = addrMapper.selectById(addr.getId());

        if (a == null) {
            throw new StException("该地址不存在无法删除");
        }
        //判断这个地址是否属于当前用户
        if (!a.getUserId().equals(userId)) {
            throw new StException("该地址属于其他用户，无法删除");
        }
        return addrMapper.update(a);
    }

    @Override
    public List<Addr> selectByCondition(Addr condition) {
        return null;
    }

    @Override
    public Addr selectById(Integer id) {
        return null;
    }
}
