package com.st.mall.userservice.controller;

import com.github.pagehelper.PageInfo;
import com.st.mall.common.bean.Collect;
import com.st.mall.common.bean.RespBean;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.CollectService;
import com.st.mall.common.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/collect")
public class CollectController {
    @Autowired
    private CollectService collectService;

    @PostMapping("/{goodsId}")
    public RespBean insert (@PathVariable("goodsId") Integer goodsId, @RequestHeader("token") String token) throws StException {
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        Integer userId = (Integer) map.get("id");

        //创建收藏对象
        Collect collect = new Collect();
        collect.setGoodsId(goodsId);
        collect.setUserId(userId);
        //添加
        collectService.insert(collect);
        return RespBean.ok("收藏成功");
    }

    @DeleteMapping("/{id}")
    public RespBean delete(@PathVariable("id") Integer id, @RequestHeader("token") String token) throws StException {
        //解析token获取用户id
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        Integer userId = (Integer) map.get("id");
        collectService.delete(id, userId);
        return RespBean.ok("取消收藏成功");
    }
    @GetMapping("/{goodsId}")
    public RespBean selectByGoodsIdAndUserId(@PathVariable("goodsId") Integer goodsId, @RequestHeader("token") String token) {
        //解析token获取用户id
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        Integer userId = (Integer) map.get("id");

        Collect collect = collectService.selectByGoodsIdAndUserId(goodsId, userId);
        return RespBean.ok("查询成功", collect);
    }
    @GetMapping
    public RespBean selectByPage(Integer pageNum, Integer pageSize, Integer userId) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 5 : pageSize;

        PageInfo<Collect> pageInfo = collectService.selectByPage(pageNum, pageSize, userId);
        return RespBean.ok("查询成功", pageInfo);
    }
}
