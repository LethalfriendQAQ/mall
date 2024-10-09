package com.st.mall.orderservice.controller;

import com.st.mall.common.bean.Cart;
import com.st.mall.common.bean.RespBean;
import com.st.mall.common.service.CartService;
import com.st.mall.common.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
