<template>
  <!--显示所有的地址-->
  <div class="addrList">
    <div class="title">我的地址</div>
    <ul>
      <li v-for="(addr, index) in addrList" :key="index" @click="addrId = addr.id">
        <span :class="['contact', addr.id == addrId ? 'selectAddr' : '']">{{addr.contact}}</span>
        <span class="address">{{addr.contact}} {{addr.contact}} {{addr.province}} {{addr.city}} {{addr.district}} {{addr.street}} {{addr.address}}</span>
      </li>
    </ul>
  </div>
  <!--显示所有选中的购物车-->
  <div class="cartList">
    <div class="title">我的商品</div>
    <ul>
      <li v-for="(cart, index) in cartList" :key="index">
        <el-row>
          <el-col :span="1" style="margin-right: 10px">
            <el-image
                :src="`${SERVER_ADDR}/goods/pic/${cart.goods.picList[0].url}`"
                fit="contain"
                style="width: 50px; height: 50px"
            />
          </el-col>
          <el-col :span="3">{{cart.goods.name}}</el-col>
          <el-col :span="5">{{cart.goods.color}} - {{cart.goods.version}}</el-col>
          <el-col :span="3">￥{{cart.goods.price}}</el-col>
          <el-col :span="1">x{{cart.count}}</el-col>
          <el-col :span="3">总价 ￥{{cart.goods.price * cart.count}}</el-col>
        </el-row>
      </li>
    </ul>
  </div>
  <!--显示总金额和生成订单的按钮-->
  <div class="footer">
    总金额 ￥<span class="title">{{ total }}</span>元
    <el-button type="danger" class="createOrder" @click="createOrder">生成订单</el-button>
  </div>
</template>

<script setup>
import {useRoute} from "vue-router";
import addrApi from "@/api/addrApi.js";
import {ref} from "vue";
import {ElMessage} from "element-plus";
import cartApi from "@/api/cartApi.js";
import orderApi from "@/api/orderApi.js";
import OrderApi from "@/api/orderApi.js";

//服务器的地址
const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);

const route = useRoute();
const addrList = ref([]);
//购物车的id
const cartId = ref([])

//表示被选中的地址 - 用来生成 订单地址的id
const addrId = ref(null);
//当前用户选中的购物车
const cartList = ref([]);
//总金额
const total = ref(0);

//生成订单
function createOrder() {
  //判断有没有选择地址
  if (!addrId.value) {
    ElMessage.error("请选择收货地址");
    return;
  }
  //已选择的购物车的地址
  let cartIds = route.query.cartIds;
  let orderVo = {
    addrId: addrId.value,
    cartIds: cartIds
  }
  //发送请求生成订单
  orderApi.insert(orderVo)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

//获取当前用户的所有的地址
function getAddrList() {
  addrApi.selectByUser()
      .then(resp => {
        if (resp.code == 10000) {
          addrList.value = resp.data;
          for (let addr of addrList.value) {
            if (addr.status == 1) {
              addrId.id = addr.id;
            }
          }
        } else {
          ElMessage.error(resp.msg);
        }
      })
}
//查询当前用户已选择的购物车的信息
function getCartList() {
  let cartIds = route.query.cartIds;
  cartApi.selectByIds(cartIds)
      .then(resp => {
        if (resp.code == 10000) {
          cartList.value = resp.data;
          cartList.value.forEach(cart => {
            total.value += cart.count * cart.goods.price;
          })
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

getCartList();
getAddrList();
</script>

<style scoped>
.addrList, .cartList, .footer {
  margin: 20px 0px;
  padding: 20px;
  border: 1px solid #222;
  background-color: #FFFFFF;
}
.footer {
  text-align: right;
}
.footer .createOrder {
  margin-left: 15px;
}
.footer .title {
  font-size: 18px;
  font-weight: bold;
  color: var(--theme-color);
}
.title {
  margin-bottom: 20px;
  font-weight: bold;
  font-size: 18px;
}
.addrList ul li {
  margin: 10px 0px 10px 15px;
  height: 30px;
  line-height: 30px;
}
.addrList .contact {
  margin-right: 15px;
  display: inline-block;
  text-align: center;
  border: 2px solid #AAA;
  width: 80px;
  cursor: pointer;
  border-radius: 20px;
}
.addrList .contact:hover {
  border: 2px solid var(--theme-color);
}
.addrList .selectAddr {
  border: 2px solid var(--theme-color);
}
.cartList ul li {
  margin: 10px 0px 10px 15px;
  height: 50px;
  line-height: 50px;
}
</style>