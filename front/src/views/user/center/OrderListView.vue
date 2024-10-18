<template>
  <el-row style="margin-bottom: 20px">
    <el-col :span="12" style="font-size: 18px; font-weight: bold">我的订单</el-col>
  </el-row>
  <!--显示我的订单-->
  <div class="order-list">
    <el-card v-for="(order, index) in pageInfo.list" :key="index" class="order-item" style="margin-bottom: 20px">
      <div class="order-header">
        <el-row>
          <el-col :span="6">订单时间：{{ order.createTime }}</el-col>
          <el-col :span="12">订单号：{{ order.id }}</el-col>
          <el-col :span="6" class="text-right">
            <el-button type="text" @click="deleteOrder(order.id)" title="删除订单">
              <el-icon style="color: red;"><Delete /></el-icon>
            </el-button>
          </el-col>
        </el-row>
      </div>

      <!--订单详情，商品列表-->
      <div class="order-details">
        <el-row v-for="(orderDetail, index) in order.orderDetails" :key="index" class="order-detail-item">
          <!-- 商品图片 -->
          <el-col :span="4" class="goods-image">
            <el-image style="width: 50px; height: 50px" :src="`${SERVER_ADDR}/goods/pic/${orderDetail.goods.picList[0].url}`" />
          </el-col>

          <!-- 商品信息 -->
          <el-col :span="10" class="goods-info">
            <p class="goods-name">{{ orderDetail.goods.name }}</p>
            <p class="goods-price">单价：￥{{ orderDetail.goods.price }}</p>
          </el-col>

          <!-- 商品数量 -->
          <el-col :span="3" class="goods-count">数量：x{{ orderDetail.count }}</el-col>

          <!-- 再次购买 -->
          <el-col :span="4" class="order-actions text-right">
            <el-button type="success" size="small" style="margin-top: 15px" @click="buyAgain(orderDetail.goods.id)">再次购买</el-button>
          </el-col>
        </el-row>
      </div>

      <!-- 合并订单状态和订单总金额 -->
      <div class="order-status-footer">
        <el-row justify="end">
          <el-col :span="7" class="order-status text-right">
            <div v-if="order.status == 0">
              <el-button type="danger" size="small" @click="toPayPage(order.id)">立即付款</el-button>
            </div>
            <div v-else>
              <el-tag type="primary">{{ ["已支付", "已发货", "已收货", "退货退款", "仅退款", "售后", "其他"][order.status - 1] }}</el-tag>
              <el-button style="margin-left: 20px" type="success" size="small" v-if="order.status == 2" @click="changeStatus(order.id)">确认收货</el-button>
            </div>
          </el-col>
          <el-col :span="5" class="order-total text-right">
            <p>订单总金额：<strong>￥{{ order.totalAmount }}</strong></p>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
  <el-row class="row-bg" justify="center" style="margin-top: 20px">
    <el-pagination background layout="prev, pager, next" :total="pageInfo.total"
                   :page-size="pageInfo.pageSize" @change="selectOrders"/>
  </el-row>
</template>

<script setup>
import { ref } from "vue";
import { ElMessage } from "element-plus";
import orderApi from "@/api/orderApi.js";
import {useRouter} from "vue-router";

const router = useRouter();
const orderList = ref([]);
const pageInfo = ref({
  pages: 0,
  pageSize: 0,
  pageNum: 0
})
const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);



function changeStatus(id) {
  const updateStatus = ref({
    id,
    status: 3
  })
  orderApi.updateStatus(updateStatus.value.id, updateStatus.value.status)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success("收货成功");
          selectOrders(pageInfo.value.pageNum);
        } else {
          ElMessage.error(resp.msg);
        }
      });
}

function toPayPage(id) {
  router.push({
    path: '/user/pay',
    query: {
      orderId: id
    }
  })
}

function buyAgain(id) {
  router.push({
    path: '/user/goods',
    query: {
      id
    }
  })
}



function selectOrders(pageNum, pageSize) {
  orderApi.selectByUser(pageNum, pageSize)
      .then(resp => {
        if (resp.code == 10000) {
          pageInfo.value = resp.data;
          orderList.value = pageInfo.value.list;
          orderList.value.forEach(order => {
            let orderTotal = 0;
            if (Array.isArray(order.orderDetails)) {
              order.orderDetails.forEach(detail => {
                if (detail.price && detail.count) {
                  orderTotal += detail.price * detail.count;
                }
              });
            }
            order.totalAmount = orderTotal; // 存储每个订单的总金额
          });
        } else {
          ElMessage.error(resp.msg);
        }
      });
}

selectOrders(1, 5);
</script>

<style scoped>
.order-list {
  margin: 20px 0;
}

.order-item {
  background-color: #fff;
  border-radius: 5px;
  padding: 10px;
}

.order-header, .order-footer {
  background-color: #f5f5f5;
  padding: 10px;
  font-size: 14px;
}

.order-details {
  padding: 10px 0;
}

.goods-image img {
  width: 80px;
  height: 80px;
  object-fit: cover;
}

.goods-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.goods-name {
  font-weight: 600;
  margin-bottom: 5px;
}

.goods-price {
  color: var(--theme-color);
}

.goods-count {
  display: flex;
  align-items: center;
  justify-content: center;
}

.order-status {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.order-actions {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.order-actions .el-button {
  margin-bottom: 5px;
}

.order-total {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.text-right {
  text-align: right;
}

.order-status-footer {
  padding: 10px 0;
  border-top: 1px solid #f0f0f0;
}
</style>
