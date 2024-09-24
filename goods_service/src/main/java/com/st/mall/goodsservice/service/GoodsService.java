package com.st.mall.goodsservice.service;

import com.github.pagehelper.PageInfo;
import com.st.mall.common.bean.Category;
import com.st.mall.common.bean.Goods;
import com.st.mall.common.exception.StException;
import com.st.mall.goodsservice.mapper.CategoryMapper;
import com.st.mall.goodsservice.mapper.GoodsMapper;
import com.st.mall.goodsservice.mapper.GoodsPicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GoodsService implements com.st.mall.common.service.GoodsService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsPicMapper goodsPicMapper;
    @Override
    public void insert(Goods goods) throws StException {
        //所属分类是否存在，并且必须是子分类
        Category category = categoryMapper.selectById(goods.getCategoryId());
        if (category == null || (category != null && category.getParentId().equals(0))) {
            throw new StException("所属分类不存在或分类是一个父分类无法添加");
        }
        //不能添加同名的商品 - 查询是否存在同名的商品
        Goods condition = new Goods();
        condition.setCategoryId(goods.getCategoryId());
        condition.setName(goods.getName());
        condition.setColor(goods.getColor());
        condition.setVersion(goods.getVersion());

        if (goodsMapper.selectByCondition(condition)
                .stream()
                .anyMatch(item -> item.getName().equals(goods.getName()))) {
            throw new StException("存在相同的商品无法添加");
        }

        //添加商品
        goodsMapper.insert(goods);
        //设置图片的商品id
        goods.getPicList()
                .stream()
                .forEach(pic -> pic.setGoodsId(goods.getId()));

        //添加图片
        goodsPicMapper.insert(goods.getPicList());

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer id) throws StException {
        //判断商品是否存在
        if (goodsMapper.selectById(id) == null) {
            throw new StException("该商品不存在");
        }
        //TODO 判断是否关联订单
        //删除商品图片
        goodsPicMapper.delete(id);
        //删除商品
        goodsMapper.delete(id);

    }

    @Override
    public void update(Goods goods) {

    }

    @Override
    public Goods selectById(Integer id) {
        return null;
    }

    @Override
    public PageInfo<Goods> selectByConfition(Goods condition) {
        return null;
    }
}
