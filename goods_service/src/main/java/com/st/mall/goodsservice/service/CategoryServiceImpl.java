package com.st.mall.goodsservice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.st.mall.common.bean.Category;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.CategoryService;
import com.st.mall.goodsservice.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public boolean insert(Category category) throws StException {
        if (!category.getParentId().equals(0)) {
            //判断被添加分类对应的父分类是否存在
            Category parent = categoryMapper.selectById(category.getParentId());
            if (parent == null || (parent != null && !parent.getParentId().equals(0))) {
                throw new StException("被添加分类的父分类不存在或不合法，无法添加");
            }
        }
        //判断同一个父分类下是否有同名的子分类
        List<Category> categoryList = categoryMapper.selectByParentId(category.getParentId());
        boolean flag = categoryList.stream()
                .anyMatch(item -> item.getName().equals(category.getName()));
        if (flag) {
            throw new StException("被添加分类的名字和同级别其他分类的名字相同，不允许添加");
        }
        return categoryMapper.insert(category) == 1;
    }

    @Override
    public boolean delete(Integer id) throws StException {
        //判断分类是否存在
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new StException("该分类不存在");
        }
        //如果是父类判断其下是否有子分类
        if (category.getParentId().equals(0) && category.getChildList().size() > 0) {
            throw new StException("该分类存在关联的子分类，不能删除");
        }
        return categoryMapper.delete(id) == 1;
    }

    @Override
    public boolean update(Category category) throws StException {
        //查询被修改分类的详细信息（根据id进行查询） - 修改之前的详细信息
        Category oldInfo = categoryMapper.selectById(category.getId());
        Category newInfo = category;

        //判断被修改的分类是否存在
        if (oldInfo == null) {
            throw new StException("该分类不存在，无法修改");
        }

        //如果分类下存在子分类，那么这个父分类就不能被修改为子分类
        if (oldInfo.getParentId().equals(0) && oldInfo.getChildList().size() > 0 && (newInfo.getParentId() != null && !newInfo.getParentId().equals(0))) {
            throw new StException("该分类存在子分类，无法修改为子分类");
        }

        //一个分类不能把自己设置为自己的父分类
        if (!newInfo.getParentId().equals(0) && !newInfo.getParentId().equals(0) && newInfo.getParentId().equals(oldInfo.getId())) {
            throw new StException("不能设置当前分类为自己的父分类");
        }

        /**
         * 判断同一个父分类下是否有同名的子分类 - 防止重名
         *  1. 查询同一个父分类下所有的子分类
         *  2. 判断是否同名
         */
        //获取修改之后的父分类的id
        Integer parentId = newInfo.getParentId() == null ? oldInfo.getParentId() : newInfo.getParentId();
        //获取修改之后的名字
        String name = newInfo.getName() == null ? oldInfo.getName() : newInfo.getName();
        //查询修改之后同一个父分类下所有的子分类
        if (categoryMapper.selectByParentId(parentId)
                .stream()
                .anyMatch(item -> item.getName().equals(name) && !item.getId().equals(category.getId()))) {
            throw new StException("修改之后的分类名和其他同级分类名相同");
        }

        //修改
        return categoryMapper.update(category) == 1;
    }

    //分页和不分页的场景下都可以使用
    @Override
    public Object selectByCondition(Category condition, Integer pageNum, Integer pageSize) {
        if (pageNum == null) {
            return categoryMapper.selectByCondition(condition);
        } else {
            //设置分页参数
            PageHelper.startPage(pageNum, pageSize);
            //查询
            List<Category> categories = categoryMapper.selectByCondition(condition);
            //创建分页信息的对象
            PageInfo<Category> pageInfo = new PageInfo<>(categories);
            return pageInfo;
        }
    }

    @Override
    public List<Category> selectByParentId(Integer parentId) {
        return categoryMapper.selectByParentId(parentId);
    }

    @Override
    public Category selectById(Integer id) {
        return categoryMapper.selectById(id);
    }
}
