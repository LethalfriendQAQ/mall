package com.st.mall.goodsservice.mapper;

import com.st.mall.common.bean.GoodsPic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsPicMapper {
    //添加
    int insert(@Param("picList") List<GoodsPic> picList);

    //根据商品的id进行删除
    int delete(Integer goodsId);

    //根据商品的id查询
    List<GoodsPic> selectByGoodsId(Integer goodsId);

}
