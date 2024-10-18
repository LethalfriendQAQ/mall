<template>
  <el-col :span="24">
    <el-card>
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item style="float: right;">
          <el-input v-model="condition.id" placeholder="请输入要搜索的订单ID" @input="selectByPage(1);" />
        </el-form-item>
        <el-form-item style="float: right;">
          <el-input v-model="condition.userId" placeholder="请输入要搜索的用户ID" @input="selectByPage(1);" />
        </el-form-item>
        <el-form-item style="float: right;">
          <el-select
              v-model="condition.status"
              placeholder="请选择订单状态"
              style="width: 150px"
              clearable
              :value-on-clear="null"
              @change="selectByPage(1)"
          >
            <el-option label="未支付" value="0"/>
            <el-option label="已支付" value="1"/>
            <el-option label="已发货" value="2"/>
            <el-option label="已收货" value="3"/>
            <el-option label="退贷退款" value="4"/>
            <el-option label="退换货" value="5"/>
            <el-option label="仅退款" value="6"/>
            <el-option label="售后" value="7"/>
            <el-option label="其他" value="8"/>
          </el-select>
        </el-form-item>
      </el-form>
      <el-table :data="pageInfo.list" border style="width: 100%">
        <el-table-column prop="id" label="ID"/>
        <el-table-column prop="userId" label="用户ID"/>
        <el-table-column prop="createTime" label="下单时间"/>
        <el-table-column prop="express" label="快递单号"/>
        <el-table-column label="支付方式">
          <template #default="scope">
            <el-tag type="primary" v-if="scope.row.payType == 0">余额支付</el-tag>
            <el-tag type="primary" v-if="scope.row.payType == 1">支付宝</el-tag>
            <el-tag type="primary" v-if="scope.row.payType == 2">微信</el-tag>
            <el-tag type="primary" v-if="scope.row.payType == 3">银联</el-tag>
            <el-tag type="primary" v-if="scope.row.payType == 4">其他</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="addrId" label="地址ID"/>
        <el-table-column prop="addrDetail" label="地址详情"/>
        <el-table-column label="当前状态">
          <template #default="scope">
            <el-tag type="danger" v-if="scope.row.status == 0">未支付</el-tag>
            <el-tag type="primary" v-if="scope.row.status == 1">已支付</el-tag>
            <el-tag type="success" v-if="scope.row.status == 2">已发货</el-tag>
            <el-tag type="warning" v-if="scope.row.status == 3">已收货</el-tag>
            <el-tag type="primary" v-if="scope.row.status == 4">退贷退款</el-tag>
            <el-tag type="primary" v-if="scope.row.status == 5">退换货</el-tag>
            <el-tag type="primary" v-if="scope.row.status == 6">仅退款</el-tag>
            <el-tag type="primary" v-if="scope.row.status == 7">售后</el-tag>
            <el-tag type="primary" v-if="scope.row.status == 8">其他</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="190px">
          <template #default="scope">
            <el-button type="success" size="small" @click="selectById(scope.row.id)" round>订单详情</el-button>
            <el-button type="primary" size="small" @click="selectById1(scope.row.id)" round>修改状态</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-row class="row-bg" justify="center" style="margin-top: 20px">
        <el-pagination background layout="prev, pager, next" :total="pageInfo.total"
                       :page-size="pageInfo.pageSize" @change="selectByPage"/>
      </el-row>
    </el-card>
  </el-col>


  <!-- 订单详情对话框开始 -->
  <el-dialog v-model="orderDetailDialogShow" title="订单详情" width="1000">
    <el-descriptions
        class="margin-top"
        :column="3"
        :size="size"
        border
    >
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <user />
            </el-icon>
            用户名
          </div>
        </template>
        {{ order.user.username }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <iphone />
            </el-icon>
            电话
          </div>
        </template>
        {{ order.user.phone }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <office-building />
            </el-icon>
            地址
          </div>
        </template>
        {{ order.addrDetail }}
      </el-descriptions-item>
    </el-descriptions>

    <el-row style="margin: 20px 0px 20px 0px">
      <el-col :span="12" style="font-size: 18px">商品详情</el-col>
    </el-row>

    <el-card class="order-detail-card" v-for="(orderDetail, index) in orderDetailList" :key="index">
      <el-row class="orderDetailItem" type="flex" align="middle">
        <el-col :span="2" class="image-col">
          <el-image
              :src="`${SERVER_ADDR}/goods/pic/${orderDetail.goods.picList[0]?.url}`"
              style="height: 50px; width: 50px"
              fit="contain"
              class="product-image"
          />
        </el-col>
        <el-col :span="5" class="product-name">{{ orderDetail.goods.name }}</el-col>
        <el-col :span="4" class="product-variant">{{ orderDetail.goods.color }} - {{ orderDetail.goods.version }}</el-col>
        <el-col :span="2" class="product-price">{{ orderDetail.goods.price.toFixed(2) }}</el-col>
        <el-col :span="5" class="product-count">x{{ orderDetail.count }}</el-col>
        <el-col :span="6" class="total-price">合计 {{ (orderDetail.goods.price * orderDetail.count).toFixed(2) }}</el-col>
      </el-row>
    </el-card>

    <el-row style="margin: 20px 0px 20px 0px; text-align: right">
      <el-col :span="24" style="font-size: 18px;">总金额: <span style="color: var(--theme-color); font-weight: bold">{{total.toFixed(2)}}</span>元</el-col>
    </el-row>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="orderDetailDialogShow = false">关闭</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 订单详情对话框结束 -->

  <!--修改订单状态对话框开始-->
  <el-dialog v-model="updateStatusDialogShow" title="修改订单状态" style="width: 300px;">
    <el-form-item>
      <el-select
          v-model="updateStatus.status"
          placeholder="请选择订单状态"
          style="width: 150px"
      >
        <el-option label="未支付" value="0"/>
        <el-option label="已支付" value="1"/>
        <el-option label="已发货" value="2"/>
        <el-option label="已收货" value="3"/>
        <el-option label="退贷退款" value="4"/>
        <el-option label="退换货" value="5"/>
        <el-option label="仅退款" value="6"/>
        <el-option label="售后" value="7"/>
        <el-option label="其他" value="8"/>
      </el-select>
    </el-form-item>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="updateStatusDialogShow = false">取消</el-button>
        <el-button type="primary" @click="changeStatus">确定</el-button>
      </div>
    </template>
  </el-dialog>
  <!--修改订单状态对话框结束-->
</template>

<script setup>
import {ref} from "vue";
import orderApi from "@/api/orderApi.js";
import {ElMessage} from "element-plus";

//总金额
const total = ref(0);
//服务器的地址
const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);
//搜索条件
const condition = ref({
  id: null,
  userId: null,
  status: null
});
//分页信息b
const pageInfo = ref({
  pages: 0,
  pageSize: 0,
  pageNum: 0,
});
const orderDetailDialogShow = ref(false);
const updateStatusDialogShow = ref(false);

const order = ref({
  user: {},
  orderDetails: []
})

const orderDetailList = ref([]);

const updateStatus = ref({
  id: "",
  status: null
})

function changeStatus() {
  orderApi.updateStatus(updateStatus.value.id, updateStatus.value.status) // Ensure you pass both id and status here
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success("设置成功");
          updateStatusDialogShow.value = false;
          selectByPage(pageInfo.value.pageNum);
        } else {
          ElMessage.error(resp.msg);
        }
      });
}


function selectById1(id) {
  updateStatus.value.id = id;
  orderApi.adminSelectById(id)
      .then(resp => {
        order.value = resp.data;
        updateStatus.value.status = order.value.status;
        updateStatusDialogShow.value = true;
      })
}

//分页查询
function selectByPage(pageNum) {
  orderApi.selectByPage(condition.value, pageNum, 10)
      .then(resp => {
        pageInfo.value = resp.data;
      })
}

function selectById(id) {
  total.value = 0; // 先重置总金额
  orderApi.adminSelectById(id)
      .then(resp => {
        order.value = resp.data;
        orderDetailList.value = order.value.orderDetails;
        orderDetailList.value.forEach(orderDetail => {
          total.value += orderDetail.count * orderDetail.goods.price;
        })
        orderDetailDialogShow.value = true;
      })
}


function clear() {
  if (orderDetailDialogShow.value = false) {
    orderDetailList.value = [];
  }
}

clear();
selectByPage(1);
</script>

<style scoped>
.order-detail-card {
  margin-bottom: 10px;
  border-radius: 8px;
  box-shadow: none; /* 去掉阴影 */
}

.orderDetailItem {
  background-color: #f9f9f9;
  border-bottom: 1px solid #eaeaea;
  padding: 5px 0; /* 减小上下内边距 */
}

.image-col {
  display: flex;
  justify-content: center;
  align-items: center;
}

.product-image {
  border-radius: 4px;
  border: 1px solid #e0e0e0;
}

.product-name {
  font-weight: bold;
  color: #333;
}

.product-variant {
  color: #777;
}

.product-price, .total-price {
  color: #e67e22;
  font-weight: bold;
}

.action-col {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>