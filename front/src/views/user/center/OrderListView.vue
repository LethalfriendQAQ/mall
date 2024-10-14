<template>
  <el-row style="margin-bottom: 20px">
    <el-col :span="12" style="font-size: 18px; font-weight: bold">我的订单</el-col>
  </el-row>
  <!--&lt;!&ndash;显示我的订单&ndash;&gt;-->
  <!--<div class="orderList">-->
  <!--  <ul class="order">-->
  <!--    <li v-for="(order, index) in orderList" :key="index">-->
  <!--      <el-row>-->
  <!--        <el-col :span="5">{{ order.createTime }}</el-col>-->
  <!--        <el-col :span="15">订单号：{{ order.id }}</el-col>-->
  <!--        <el-col :span="4" style="text-align: right">删除</el-col>-->
  <!--      </el-row>-->
  <!--      <el-row v-for="(orderDetail, index) in order.orderDetails" :key="index">-->
  <!--        <el-col :span="3">图片</el-col>-->
  <!--        <el-col :span="6">{{ orderDetail.goods.name }}</el-col>-->
  <!--        <el-col :span="3">x{{ orderDetail.count }}</el-col>-->
  <!--        <el-col :span="3">收货人</el-col>-->
  <!--        <el-col :span="3">{{ orderDetail.goods.markPrice }}</el-col>-->
  <!--        <el-col :span="3">再次购买</el-col>-->
  <!--        <el-col :span="3">-->
  <!--          <div>{{ orderDetail.status }}</div>-->
  <!--          <div>订单状态</div>-->
  <!--        </el-col>-->
  <!--      </el-row>-->
  <!--    </li>-->
  <!--  </ul>-->
  <!--</div>-->
  <!--显示我的订单-->
  <table class="orderList">
    <tr class="order" v-for="(order, index) in orderList" :key="index">
      <el-row>
        <el-col :span="5">{{ order.createTime }}</el-col>
        <el-col :span="15">订单号：{{ order.id }}</el-col>
        <el-col :span="4" style="text-align: right">删除</el-col>
      </el-row>
      <tr v-for="(orderDetail, index) in order.orderDetails" :key="index">
        <td style="width: 10%;">图片</td>
        <td style="width: 30%;">{{ orderDetail.goods.name }}</td>
        <td style="width: 10%;">x{{ orderDetail.count }}</td>
        <td style="width: 15%;">{{ orderDetail.goods.markPrice }}</td>
        <td style="width: 15%;">
          <el-button type="warning" size="small">再次购买</el-button>
        </td>
        <td style="width: 10%;">收货人</td>
        <td style="width: 10%;">
          <div>{{ orderDetail.status }}</div>
          <div>订单状态</div>
        </td>
      </tr>
    </tr>
  </table>
</template>

<script setup>
import {ref} from "vue";
import {ElMessage} from "element-plus";
import orderApi from "@/api/orderApi.js";

const orderList = ref([])
const orderDetails = ref([]);
//服务器的地址
const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);
function selectOrders() {
  orderApi.selectByUserId()
      .then(resp => {
        console.log(resp);
        if (resp.code == 10000) {
          orderList.value = resp.data;
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

selectOrders();
</script>

<style scoped>
/*.orderList {
  background-color: #FFFFFF;
}
.orderList .order li {
  margin: 20px 0px;
  border: 1px solid #222;
  background-color: #FFFFFF;
}
.orderList .order li .el-row {
  height: 50px;
}*/

/*.orderList {
  width: 100%;
  background-color: #FFFFFF;
}
.orderList .order {
  margin: 20px 0px;
  border: 1px solid #222;
  background-color: #FFFFFF;
}
.orderList .order li {
  height: 50px;
}*/
.orderList {
  width: 100%;
  border-collapse: collapse;
}
.orderList .order {
  margin-bottom: 20px;
  border: 1px solid #ccc;
  display: block;
}
.orderList tr {

}
.orderList td {
  padding: 8px;
  text-align: center;
}

.orderList .order td {
  text-align: left;
}

</style>