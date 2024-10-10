<template>
  <el-row class="title">
    <el-col :span="2">
      <el-checkbox label="全选" />
    </el-col>
    <el-col :span="11">商品</el-col>
    <el-col :span="2">单价</el-col>
    <el-col :span="6">数量</el-col>
    <el-col :span="2">小计</el-col>
    <el-col :span="1">操作</el-col>
  </el-row>
  <el-row class="cartItem" v-for="(cart, index) in cartList" :key="index">
    <el-col :span="2">
      <el-checkbox label="全选" />
    </el-col>
    <el-col :span="2">
      <el-image
          :src="`${SERVER_ADDR}/goods/pic/${cart.goods.picList[0].url}`"
          style="height: 50px; width: 50px"
          fit="contain"
      />
    </el-col>
    <el-col :span="5">{{ cart.goods.name }}</el-col>
    <el-col :span="4">{{ cart.goods.color }}</el-col>
    <el-col :span="2">{{ cart.goods.price }}</el-col>
    <el-col :span="6">{{ cart.count }}</el-col>
    <el-col :span="2">{{ cart.goods.markPrice }}</el-col>
    <el-col :span="1">操作</el-col>
  </el-row>
</template>

<script setup>
import {ref} from "vue";
import cartApi from "@/api/cartApi.js";
import { useRoute } from "vue-router";

//服务器的地址
const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);
const route = useRoute();
//当前用户所有的购物车信息
const cartList = ref([]);
//查询当前用户所有的购物车信息
function selectCartList() {
  cartApi.selectCartList()
      .then(resp => {
        cartList.value = resp.data;
      });
}

selectCartList();
</script>

<style scoped>
  .title {
    margin-top: 20px;
    padding-left: 10px;
    font-size: 14px;
    background-color: #FFFFFF;
    height: 50px;
    line-height: 50px;
  }
  .cartItem {
    margin-bottom: 10px;
    border-bottom: 1px solid #EEEEEE;
    padding-left: 10px;
    font-size: 14px;
    background-color: #FFFFFF;
    height: 60px;
    line-height: 50px;
  }
</style>