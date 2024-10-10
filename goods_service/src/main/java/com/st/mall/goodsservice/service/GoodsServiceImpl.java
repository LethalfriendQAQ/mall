package com.st.mall.goodsservice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.st.mall.common.bean.Category;
import com.st.mall.common.bean.Goods;
import com.st.mall.common.exception.StException;
import com.st.mall.goodsservice.mapper.CategoryMapper;
import com.st.mall.goodsservice.mapper.GoodsMapper;
import com.st.mall.goodsservice.mapper.GoodsPicMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DubboService
public class GoodsServiceImpl implements com.st.mall.common.service.GoodsService {
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
        //判断是否有图片需要添加
        if (goods.getPicList() != null && goods.getPicList().size() > 0) {
            //设置图片的商品id
            goods.getPicList()
                    .stream()
                    .forEach(pic -> pic.setGoodsId(goods.getId()));

            //添加图片
            goodsPicMapper.insert(goods.getPicList());
        }
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
    public void update(Goods goods) throws StException {
        Goods newInfo = goods;
        Goods oldInfo = goodsMapper.selectById(goods.getId());
        //判断商品是否存在
        if (oldInfo == null) {
            throw new StException("该商品不存在,无法修改");
        }
        //判断是否修改了分类
        if (newInfo.getCategoryId() != null && newInfo.getCategoryId().equals(oldInfo.getCategoryId())) {
            //判断修改之后的分类是否存在并且必须是子分类
            Category category = categoryMapper.selectById(newInfo.getCategoryId());
            if (category == null || (category != null && category.getParentId() == 0)) {
                throw new StException("该分类不存在或分类是父分类，无法修改");
            }
        }
        //判断是否同名 -分类 -名字 -颜色 -版本
        Integer newcategoryId = newInfo.getCategoryId() == null ? oldInfo.getCategoryId() : newInfo.getCategoryId();
        String newName = newInfo.getName() == null ? oldInfo.getName() : newInfo.getName();
        String newColor = newInfo.getColor() == null ? oldInfo.getColor() : newInfo.getColor();
        String newVersion = newInfo.getName() == null ? oldInfo.getVersion() : newInfo.getVersion();
        Goods condition = new Goods();
        condition.setCategoryId(newcategoryId);
        condition.setName(newName);
        condition.setColor(newColor);
        condition.setVersion(newVersion);
        if (goodsMapper.selectByCondition(condition)
                .stream()
                .anyMatch(item -> item.getName().equals(newName) && !item.getId().equals(newInfo.getId()))) {
            throw new StException("修改之后的信息和其他商品信息相同，无法修改");
        }
        //修改商品信息
        goodsMapper.update(goods);

        //修改图片 1.删除之前的图片 2.添加
        if (newInfo.getPicList() != null && newInfo.getPicList().size() > 0) {
            goodsPicMapper.delete(newInfo.getId());
            newInfo.getPicList().forEach(pic -> pic.setGoodsId(newInfo.getId()));
            goodsPicMapper.insert(newInfo.getPicList());
        }

    }

    @Override
    public Goods selectById(Integer id) {
        return goodsMapper.selectById(id);
    }

    @Override
    public PageInfo<Goods> selectByCondition(Goods condition, Integer pageNum, Integer pageSize) {
        //设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        //查询
        List<Goods> goods = goodsMapper.selectByCondition(condition);
        //创建分页信息
        PageInfo<Goods> pageInfo = new PageInfo<>(goods);
        return pageInfo;
    }

    @Override
    public PageInfo<Goods> selectByCondition1(Goods condition, Integer pageNum, Integer pageSize) {
        //设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        //查询
        List<Goods> goods = goodsMapper.selectByCondition1(condition);
        //创建分页信息
        PageInfo<Goods> pageInfo = new PageInfo<>(goods);
        return pageInfo;
    }
}
