package com.st.mall.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVo {
    private Integer addrId;
    private Integer[] cartIds;
    private Integer userId;
}
