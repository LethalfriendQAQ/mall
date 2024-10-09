package com.st.mall.userservice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.st.mall.common.bean.Collect;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.CollectService;
import com.st.mall.userservice.mapper.CollectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectServiceImpl implements CollectService {
    @Autowired
    private CollectMapper collectMapper;

    @Override
    public boolean insert(Collect collect) throws StException {
        //根据商品id和用户的id查询
        if (collectMapper.selectByGoodsIdAndUserId(collect.getGoodsId(), collect.getUserId()) != null) {
            throw new StException("该商品已被收藏");
        }
        //添加
        return collectMapper.insert(collect) == 1;
    }

    @Override
    public boolean delete(Integer id, Integer userId) throws StException {
        //判断这个收藏是否存在
        Collect collect = collectMapper.selectById(id);
        if (collect == null) {
            throw new StException("该收藏不存在，无法取消收藏");
        }
        //判断这个收藏是否属于当前的用户
        if (!collect.getUserId().equals(userId)) {
            throw new StException("该收藏属于其他用户，无法取消收藏");
        }
        //删除 - 取消收藏
        return collectMapper.delete(id) == 1;
    }

    @Override
    public Collect selectById(Integer id) {
        return null;
    }

    @Override
    public Collect selectByGoodsIdAndUserId(Integer goodsId, Integer userId) {
        return collectMapper.selectByGoodsIdAndUserId(goodsId, userId);
    }

    @Override
    public PageInfo<Collect> selectByPage(Integer pageNum, Integer pageSize, Integer userId) {
        //设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        List<Collect> collects = collectMapper.selectByUserId(userId);
        //创建分页信息
        PageInfo<Collect> pageInfo = new PageInfo<>(collects);
        return pageInfo;
    }
}
