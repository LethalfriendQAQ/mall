<template>
  <el-row style="margin-bottom: 20px">
    <el-col :span="12" style="font-size: 18px; font-weight: bold">我的订单</el-col>
  </el-row>
  <el-col :span="24">
    <el-card>
      <el-table  border style="width: 100%">
        <el-table-column prop="id" label="ID" width="50px"/>
        <el-table-column prop="name" label="名称"/>
        <el-table-column prop="dscp" label="描述"/>
        <el-table-column prop="price" label="售价"/>
      </el-table>
    </el-card>
  </el-col>
</template>

<script setup>
import userApi from "@/api/userApi.js";
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