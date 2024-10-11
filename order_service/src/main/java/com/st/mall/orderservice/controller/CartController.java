package com.st.mall.orderservice.controller;

import com.st.mall.common.bean.Cart;
import com.st.mall.common.bean.RespBean;
import com.st.mall.common.exception.StException;
import com.st.mall.common.service.CartService;
import com.st.mall.common.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/{goodsId}")
    public RespBean insert(@PathVariable("goodsId") Integer goodsId, @RequestHeader("token") String token) {
        //解析token获取用户id
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        Integer userId = (Integer) map.get("id");
        //创建购物车的对象
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setGoodsId(goodsId);
        cartService.insertOrUpdate(cart);
        return RespBean.ok("已添加到购物车");
    }
    @DeleteMapping("/{id}")
    public RespBean delete(@PathVariable("id") Integer id, @RequestHeader("token") String token) throws StException {
        //解析token获取用户id
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        Integer userId = (Integer) map.get("id");

        cartService.delete(id, userId);
        return RespBean.ok("删除成功");
    }

    @PutMapping
    public RespBean update(@RequestBody Cart cart, @RequestHeader("token") String token) throws StException {
        //解析token获取用户id
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        Integer userId = (Integer) map.get("id");
        cartService.update(cart, userId);
        return RespBean.ok("修改购物车成功");
    }

    @GetMapping
    public RespBean selectCartList(@RequestHeader("token") String token) {
        //解析token获取用户id
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        Integer userId = (Integer) map.get("id");

        Cart condition = new Cart();
        condition.setUserId(userId);

        List<Cart> cartList = cartService.search(condition);
        return RespBean.ok("", cartList);
    }

    @GetMapping("/{id}")
    public RespBean selectById(@PathVariable("id") Integer id) {
        Cart cart = cartService.selectById(id);
        return RespBean.ok("", cart);
    }
    @DeleteMapping
    public RespBean deleteByIds(@RequestBody Map<String, ArrayList<Integer>> data, @RequestHeader("token") String token) throws StException {
        //解析token获取用户id
        Map<String, Object> map = JwtUtil.parseJwtToMap(token);
        Integer userId = (Integer) map.get("id");

        ArrayList<Integer> ids = data.get("ids");
        cartService.deleteByIds(ids, userId);
        return RespBean.ok("删除成功");
    }
}
