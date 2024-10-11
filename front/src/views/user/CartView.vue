<template>
  <div v-if="cartList.length > 0">
    <el-row class="title">
      <el-col :span="2">
        <el-checkbox
            label="全选"
            v-model="checkAllState"
            :indeterminate="halfState"
            @change="checkAllOrNone"
        />
      </el-col>
      <el-col :span="11">商品</el-col>
      <el-col :span="2">单价</el-col>
      <el-col :span="6">数量</el-col>
      <el-col :span="2">小计</el-col>
      <el-col :span="1">操作</el-col>
    </el-row>
    <el-row class="cartItem" v-for="(cart, index) in cartList" :key="index">
      <el-col :span="2">
        <el-checkbox v-model="cart.checkState" @change="changeState" />
      </el-col>
      <el-col :span="2">
        <el-image
            :src="`${SERVER_ADDR}/goods/pic/${cart.goods.picList[0].url}`"
            style="height: 50px; width: 50px"
            fit="contain"
        />
      </el-col>
      <el-col :span="5">{{ cart.goods.name }}</el-col>
      <el-col :span="4">{{ cart.goods.color }} - {{ cart.goods.version }}</el-col>
      <el-col :span="2">{{ cart.goods.price }}</el-col>
      <el-col :span="6">
        <el-input-number
            v-model="cart.count"
            :min="1"
            size="small"
            @change="updateCount(cart)"
        />
      </el-col>
      <el-col :span="2">{{ cart.goods.price * cart.count }}</el-col>
      <el-col :span="1">
        <el-button type="danger" :icon="Delete" circle  @click="deleteById(cart.id)"/>
      </el-col>
    </el-row>
    <el-row class="cartFooter">
      <el-col :span="2">
        <el-checkbox
            label="全选"
            v-model="checkAllState"
            :indeterminate="halfState"
            @change="checkAllOrNone"
        />
      </el-col>
      <el-col :span="3">
        <el-link :underline="false" @click="deleteChecked">删除选中的商品</el-link>
      </el-col>
      <el-col :span="11">
        <el-link :underline="false" @click="deleteAll">清理购物车</el-link>
      </el-col>
      <el-col :span="3">
        已选择{{ checkedCount }}件商品
      </el-col>
      <el-col :span="3">
        总价 ￥{{ total }}
      </el-col>
      <el-col :span="2">
        <el-button type="danger" @click="toCreateOrderPage">结算</el-button>
      </el-col>
    </el-row>
  </div>
  <div v-else class="none">
    购物车空空~，去看看心仪的商品吧~<RouterLink to="/user/index" >去购物</RouterLink>
  </div>
</template>

<script setup>
import {ref} from "vue";
import cartApi from "@/api/cartApi.js";
import { useRoute } from "vue-router";
import {Delete} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";


const router = useRouter();
//服务器的地址
const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);
//当前用户所有的购物车信息
const cartList = ref([]);
//表示全选按钮是否选中
const checkAllState = ref(false);
//没有全选的状态
const halfState = ref(false);
//选中商品的数量
const checkedCount = ref(0);
//选中商品的总金额
const total = ref(0);


//跳转到生成订单的页面
function toCreateOrderPage() {
  //获取已选中的购物车的id
  let cartIds = [];
  cartList.value.forEach(cart => {
    if (cart.checkState) {
      cartIds.push(cart.id);
    }
  });

  if (cartIds.length == 0) {
    ElMessage.warning("您还没有选择商品");
    return;
  }
  router.push({
    path: "/user/createOrder",
    query: {
      cartIds
    }

  });
}

//删除已选择的购物车
function deleteChecked() {
  let ids = [];
  cartList.value.forEach(cart => {
    if (cart.checkState) {
      ids.push(cart.id);
    }
  });
  if (ids.length == 0) {
    ElMessage.warning("没有选择购物车，无法删除");
    return;
  }
  //批量删除
  cartApi.deleteByIds(ids)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          cartList.value = cartList.value.filter(cart => !cart.checkState);
          changeState();
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

//清空购物车
function deleteAll() {
  let ids = [];
  cartList.value.forEach(cart => {
    ids.push(cart.id);
  });
  if (ids.length == 0) {
    ElMessage.warning("购物车中没有商品，无法删除");
    return;
  }
  //批量删除
  cartApi.deleteByIds(ids)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          //重新获取购物车列表
          selectCartList();
          //重新设置全选按钮状态 - 重新计算总金额和数量
          changeState();
        } else {
          ElMessage.error(resp.msg);
        }
      })
}
//全选 - 全不选
function checkAllOrNone() {
  //让每个购物车向前面的多选按钮的状态和全选按钮的状态保持一致
  cartList.value.forEach(cart => {
    cart.checkState = checkAllState.value;
    halfState.value = false;
  });
  getCountAndTotal();
}

//购物车向前面的多选按钮发生状态时调用的函数
function changeState() {
  /**
   * 设置全选按钮状态
   * 1. 如果所有的checkbox都被选中，设置全选按钮选中
   * 2. 如果所有的checkbox都没被选中，设置全选按钮没有选中
   * 重新计算选中商品的数量
   * 重新计算选中商品的金额
   */
  let count = 0; //被选中的checkbox的数量
  cartList.value.forEach(cart => {
    if (cart.checkState) {
      count++; //计算被选中的checkbox的数量
    }
  });
  if (count == cartList.value.length) {
    checkAllState.value = true;
    halfState.value = false;
  } else if (count == 0){
    checkAllState.value = false;
    halfState.value = false;
  } else {
    halfState.value = true;
  }
  getCountAndTotal();
}

//计算选中商品的质量和金额
function getCountAndTotal() {
  checkedCount.value = 0;
  total.value = 0;
  cartList.value.forEach(cart => {
    if (cart.checkState) {
      checkedCount.value += cart.count; //计算选中的商品的数量
      total.value += cart.goods.price * cart.count; //计算选中商品的金额
    }
  });
}


//查询当前用户所有的购物车信息
function selectCartList() {
  cartApi.selectCartList()
      .then(resp => {
        cartList.value = resp.data;
      });
}

function deleteById(id) {
  cartApi.delete(id)
      .then(resp => {
        if (resp.code == 10000) {
          // 弹出消息
          ElMessage.success(resp.msg);
          //将被删除的项从cartList中清除
          cartList.value = cartList.value.filter(cart => cart.id != id);
          //设置全选按钮的状态
          changeState();
          getCountAndTotal();
        } else {
          // 弹出消息
          ElMessage.error(resp.msg);
        }
      })
}

//修改购物车商品的数量
function updateCount(cart) {
  const cartInfo = {
    id: cart.id,
    count: cart.count
  }
  cartApi.update(cartInfo)
      .then(resp => {
        if (resp.code == 10000) {
          //让购物车中商品的数量和数据库中数量保持一致
          cartApi.selectById(cartInfo.id)
              .then(resp => {
                cartList.value.forEach(cart => {
                  if (cart.id == cartInfo.id) {
                    cart.count = resp.data.count;
                  }
                });
                getCountAndTotal();
              })
        } else {
          // 弹出消息
          ElMessage.error(resp.msg);
        }
      })
}

selectCartList();
</script>

<style scoped>
  .title, .cartFooter {
    margin: 20px 0px;
    padding-left: 10px;
    font-size: 14px;
    background-color: #FFFFFF;
    height: 50px;
    line-height: 50px;
  }
  .cartItem {
    margin-bottom: 10px;
    padding-left: 10px;
    font-size: 14px;
    background-color: #FFFFFF;
    height: 50px;
    line-height: 50px;
  }
  .none {
    line-height: 300px;
    height: 300px;
    background-color: #FFFFFF;
    text-align: center;
  }
</style>