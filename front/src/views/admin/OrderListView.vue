<template>
  <el-col :span="24">
    <el-card>
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item>
          <el-button type="primary" @click="addDialogShow = true">添加</el-button>
        </el-form-item>
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
            <el-tag type="primary" v-if="scope.row.status == 0">余额支付</el-tag>
            <el-tag type="primary" v-if="scope.row.status == 1">支付宝</el-tag>
            <el-tag type="primary" v-if="scope.row.status == 2">微信</el-tag>
            <el-tag type="primary" v-if="scope.row.status == 3">银联</el-tag>
            <el-tag type="primary" v-if="scope.row.status == 4">其他</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="addrId" label="地址ID"/>
        <el-table-column prop="addrDetail" label="地址详情"/>
        <el-table-column prop="status" label="当前状态"/>
        <el-table-column label="当前状态">
          <template #default="scope">
            <el-tag type="primary" v-if="scope.row.status == 0">未支付</el-tag>
            <el-tag type="primary" v-if="scope.row.status == 1">已支付</el-tag>
            <el-tag type="primary" v-if="scope.row.status == 2">已发货</el-tag>
            <el-tag type="primary" v-if="scope.row.status == 3">已收货</el-tag>
            <el-tag type="primary" v-if="scope.row.status == 4">退贷退款</el-tag>
            <el-tag type="primary" v-if="scope.row.status == 5">退换货</el-tag>
            <el-tag type="primary" v-if="scope.row.status == 6">仅退款</el-tag>
            <el-tag type="primary" v-if="scope.row.status == 7">售后</el-tag>
            <el-tag type="primary" v-if="scope.row.status == 8">其他</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="230px">
          <template #default="scope">
            <el-button type="primary" size="small" @click="selectById(scope.row.id)" round>订单详情</el-button>
            <el-button type="primary" size="small" @click="" round>修改</el-button>
            <el-popconfirm title="你确定要删除该分类吗？" confirm-button-text="确认" cancel-button-text="取消"
                           width="200px" @confirm="">
              <template #reference>
                <el-button size="small" type="danger" round>删除</el-button>
              </template>
            </el-popconfirm>
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
        {{ orderDetail.user.username }}
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
        {{ orderDetail.user.phone }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon :style="iconStyle">
              <office-building />
            </el-icon>
            Address
          </div>
        </template>
        {{ orderDetail.addrDetail }}
      </el-descriptions-item>
    </el-descriptions>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="orderDetailDialogShow = false">关闭</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 订单详情对话框结束 -->
</template>

<script setup>
import {ref} from "vue";
import {useTokenStore} from "@/stores/token.js";
import orderApi from "@/api/orderApi.js";

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

const orderDetail = ref({
  user: {},
  orderDetails: []
})


//分页查询
function selectByPage(pageNum) {
  orderApi.selectByPage(condition.value, pageNum, 5)
      .then(resp => {
        pageInfo.value = resp.data;
      })
}

function selectById(id) {
  orderApi.selectById(id)
      .then(resp => {
        orderDetail.value = resp.data;
        console.log(orderDetail.value);
        orderDetailDialogShow.value = true;
      })
}

selectByPage(1);
</script>

<style scoped>

</style>