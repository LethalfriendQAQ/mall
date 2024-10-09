<template>
  <el-row>
    <el-col :span="12">
      <!-- 商品图片 -->
      <el-carousel height="400px">
        <el-carousel-item v-for="(pic, index) in goods.picList" :key="index">
          <el-image
              fit="contain"
              :src="`${SERVER_ADDR}/goods/pic/${pic.url}`"
              style="height: 400px; width: 600px;"
          >
          </el-image>
        </el-carousel-item>
      </el-carousel>
    </el-col>
    <el-col :span="12">
      <!-- 商品进本信息 -->
      <div class="info">
        <div class="name">{{ goods.name }}</div>
        <div class="dscp">{{ goods.dscp }}</div>
        <div class="price">售价 ￥{{ goods.price }}</div>
        <div class="markPrice">原价 ￥{{ goods.markPrice }}</div>
        <div class="color"><span>颜色 {{ goods.color }}</span></div>
        <div class="version"><span>版本 {{ goods.version }}</span></div>
        <div class="count">数量 {{ goods.count }}</div>
        <div class="score">
          <el-rate
              v-model="goods.score"
              disabled
              show-score
              text-color="#ff9900"
              :colors="['#ff9900', '#ff9900', '#ff9900']"
              score-template="{value} 分"
          />
        </div>
        <div class="goodsBtn">
          <el-button type="primary"><el-icon><ShoppingCart /></el-icon>加入购物车</el-button>
          <el-button type="success"><el-icon><Money /></el-icon>直接购买</el-button>
          <el-button type="info" v-if="!collectInfo" @click="collect"><el-icon><StarFilled /></el-icon>收藏</el-button>
          <el-button type="danger" v-else @click="cancelCollect"><el-icon><Star /></el-icon>取消收藏</el-button>
        </div>
      </div>
    </el-col>
  </el-row>
  <el-row>
    <!-- 商品详情/商品评价 -->
    <el-tabs model-value="first" class="demo-tabs" type="border-card" style="width: 1200px">
      <el-tab-pane label="商品详情" name="first" v-html="goods.detail"></el-tab-pane>
      <el-tab-pane label="商品评价" name="second" v-html="goods.detail"></el-tab-pane>
    </el-tabs>
  </el-row>
</template>

<script setup>
import { useRoute } from "vue-router";
import goodsApi from "@/api/goodsApi.js";
import {ref} from "vue";
import collectApi from "@/api/collectApi.js";
import {ElMessage} from "element-plus";

//商品收藏的状态
const collectInfo = ref(null);

//服务器的地址
const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);
const route = useRoute();

const goods = ref({});
//根据商品id查询商品的详情信息
function selectById() {
  let id = route.query.id;
  goodsApi.selectById(id)
      .then(resp => {
        goods.value = resp.data;
        //获取当前商品收藏的情况
        getCollectInfo();
      });
}
function getCollectInfo() {
  collectApi.selectByGoodsIdAndUserId(goods.value.id)
      .then(resp => {
        collectInfo.value = resp.data
      })
}

//收藏
function collect() {
  collectApi.insert(goods.value.id)
      .then(resp => {
        if(resp.code == 10000) {
          ElMessage.success(resp.msg);
          //刷新收藏的状态
          getCollectInfo();
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

function cancelCollect() {
  collectApi.delete(collectInfo.value.id)
      .then(resp => {
        if(resp.code == 10000) {
          ElMessage.success(resp.msg);
          //刷新收藏的状态
          getCollectInfo();
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

selectById();
</script>

<style scoped>
.el-carousel {
  margin: 15px 0px;
  border: 1px solid #AAA;
}
.info {
  margin: 15px 60px;
}

.info div {
  margin-bottom: 15px;
}
.info .name {
  font-size: 25px;
  font-weight: bold;
}
.info .dscp {
  color: #999999;
}
.info .price {
  //color: var(--theme-color);
  font-size: 18px;
}
.info .markPrice {
  color: #999999;
  font-size: 15px;
  text-decoration: line-through;
}
.info span {
  display: inline-block;
  line-height: 30px;
  border: 2px solid var(--theme-color);
  border-radius: 5px;
  background-color: #FFFFFF;
  padding: 0px 10px;
}
.info .count {
  font-size: 15px;
}
.el-icon {
  margin-right: 3px;
}
</style>