package com.st.mall.userservice.mapper;

import com.st.mall.common.bean.Collect;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CollectMapper {
    int insert(Collect collect);

    int delete(Integer id);

    Collect selectById(Integer id);

    Collect selectByGoodsIdAndUserId(@Param("goodsId") Integer goodsId, @Param("userId")Integer userId);

    List<Collect> selectByUserId(Integer userId);
}
