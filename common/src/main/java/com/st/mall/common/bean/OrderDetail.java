package com.st.mall.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail implements Serializable {
    private Integer id;
    private Integer count;
    private BigDecimal price;
    private String orderId;
    private Integer goodsId;
    private Goods goods;
}
