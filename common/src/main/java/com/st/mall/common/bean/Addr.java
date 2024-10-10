package com.st.mall.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Addr implements Serializable {
    private Integer id;
    private String contact;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String street;
    private String address;
    private Integer userId;
    private Integer status;
}
