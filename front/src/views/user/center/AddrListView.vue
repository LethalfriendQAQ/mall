<template>
  <el-row style="margin-bottom: 20px">
    <el-col :span="12" style="font-size: 18px; font-weight: bold">我的地址</el-col>
    <el-col :span="12" style="text-align: right"><el-button type="primary">添加地址</el-button> </el-col>
  </el-row>
  <div>
    <ul class="addrList">
      <li v-for="(addr, index) in addrList" :key="index">
        <div class="area">{{addr.province}} - {{addr.city}} - {{addr.district}} - {{addr.street}}</div>
        <div class="address">{{addr.address}}</div>
        <div class="contect">{{addr.contact}}</div>
        <div class="phone">{{addr.phone}}</div>
        <div class="option">
          <el-button size="small" >设为默认</el-button>
          <el-button size="small" >编辑</el-button>
          <el-button size="small" >删除</el-button>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup>
import addrApi from "@/api/addrApi.js";
import {ref} from "vue";
import {ElMessage} from "element-plus";

//当前用户的所有地址信息
const addrList = ref([]);

//获取当前用户的所有的地址
function getAddrList() {
  addrApi.selectByUser()
      .then(resp => {
        if (resp.code == 10000) {
          addrList.value = resp.data;
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

getAddrList();
</script>

<style scoped>
  .addrList li {
    margin: 0px 20px 20px 0px;
    padding: 20px;
    float: left;
    width: 280px;
    border: 1px solid #EEEEEE;
    border-radius: 5px;
    background-color: #FFFFFF;
  }
  .addrList li:hover {
    box-shadow: 0px 0px 20px #8c939d;
  }
  .addrList li:nth-child(3n) {
    margin-right: 0px;
  }
  .addrList li div {
    height: 30px;
    line-height: 30px;
  }
  .addrList li .area {
    font-size: 18px;
    font-weight: bold;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .addrList li .address {
    font-size: 14px;
    color: #AAAAAA;
  }
  .addrList li .contect {
    //color: var(--theme-color);
    font-weight: bold;
  }
  .addrList li .phone {
    font-size: 14px;
  }
  .addrList li .option {
    text-align: right;
  }
</style>