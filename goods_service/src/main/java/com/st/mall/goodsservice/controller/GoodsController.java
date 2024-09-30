package com.st.mall.goodsservice.controller;

import com.github.pagehelper.PageInfo;
import com.st.mall.common.bean.Goods;
import com.st.mall.common.bean.RespBean;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    //添加
    @PostMapping
    public RespBean insert(@RequestBody Goods goods) throws StException {
        goods.setScore(5F);
        goodsService.insert(goods);
        return RespBean.ok("添加成功");
    }
    //删除
    @DeleteMapping("/{id}")
    public RespBean delete(@PathVariable("id") Integer id) throws StException {
        goodsService.delete(id);
        return RespBean.ok("删除成功");
    }

    //修改
    @PutMapping
    public RespBean update(@RequestBody Goods goods) throws StException {
        goodsService.update(goods);
        return RespBean.ok("修改成功");
    }

    //搜索
    @GetMapping
    public RespBean selectByPage(Goods condition, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 5 : pageSize;

        PageInfo<Goods> pageInfo = goodsService.selectByCondition(condition, pageNum, pageSize);
        return RespBean.ok("查询成功", pageInfo);
    }
    @GetMapping("/search")
    public RespBean selectByPage1(Goods condition, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 5 : pageSize;

        PageInfo<Goods> pageInfo = goodsService.selectByCondition1(condition, pageNum, pageSize);
        return RespBean.ok("查询成功", pageInfo);
    }

    //根据id查询
    @GetMapping("/{id}")
    public RespBean selectById(@PathVariable("id") Integer id) {
        Goods goods = goodsService.selectById(id);
        return RespBean.ok("", goods);
    }
}
