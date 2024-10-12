<template>
  <!--显示我的订单-->
  <div class="orderList">
    <el-row style="margin-bottom: 20px">
      <el-col :span="12" style="font-size: 18px; font-weight: bold">我的订单</el-col>
    </el-row>
    <ul>
      <li v-for="(order, index) in orderList" :key="index">

      </li>
    </ul>
  </div>
</template>

<script setup>
import {ref} from "vue";
import {ElMessage} from "element-plus";
import orderApi from "@/api/orderApi.js";

const orderList = ref([])

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

</style>