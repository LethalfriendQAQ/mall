<template>
  <!--走马灯-->
  <el-carousel height="450px">
    <el-carousel-item v-for="(banner, index) in banners" :key="index">
      <el-image :src="banner" />
    </el-carousel-item>
  </el-carousel>

  <!-- 广告位 -->
  <div class="ad">
    <ul>
      <li><el-image fit="cover" src="/src/assets/iphone16.png" /></li>
      <li><el-image fit="cover" src="/src/assets/iphone16.png" /></li>
      <li><el-image fit="cover" src="/src/assets/iphone16.png" /></li>
      <li><el-image fit="cover" src="/src/assets/iphone16.png" /></li>
    </ul>
  </div>


  <!-- 推荐的商品 -->
  <div class="recom" v-for="(category, index) in parentList" :key="index">
    <div class="categoryName">{{ category.name }}</div>
    <div class="goodsList">
      <ul>
        <li v-for="(goods, index) in category.goodsList" :key="index" @click="toGoodsView(goods.id)">
          <div class="pic">
            <el-image v-if="goods.picList && goods.picList.length > 0" :src="`${SERVER_ADDR}/goods/pic/${goods.picList[0].url}`" />
          </div>
          <div class="name">{{ goods.name }}</div>
          <div class="dscp">{{ goods.dscp }}</div>
          <div class="price">￥ {{ goods.price }}</div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import categoryApi from "@/api/categoryApi.js";
import { useRouter } from "vue-router";

const router = useRouter();
//已上架的父分类
const parentList = ref([]);

//服务器的地址
const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);

const banners = ref([
  "/src/assets/banner/banner1.png",
  "/src/assets/banner/banner2.png",
  "/src/assets/banner/banner3.png"
]);

function getParent() {
  const condition = {
    parentId: 0,
    status: 1
  };
  categoryApi.selectByPage(condition)
      .then(resp => {
        parentList.value = resp.data;
      });
}

//跳转到商品详情页
function toGoodsView(id) {
  router.push({
    path: '/user/goods',
    query: { id }
  });
}

getParent();
</script>

<style scoped>
.ad {
  margin: 20px 0;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
}
.ad ul {
  display: flex;
  padding: 0;
  list-style: none;
  width: 100%; /* 确保广告位占满父元素的宽度 */
}
.ad ul li {
  flex: 1 1 23%; /* 每行显示4个广告位，调整为23%以便填充 */
  margin: 0 5px; /* 增加左右间距 */
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  position: relative; /* 为绝对定位做准备 */
  overflow: hidden; /* 隐藏超出部分 */
}
.ad ul li .el-image {
  width: 100%; /* 图片占满整个li */
  height: 100%; /* 图片高度设为100% */
  object-fit: cover; /* 填充 */
}

.recom {
  margin-bottom: 20px;
}
.recom .goodsList {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start; /* 左对齐 */
}
.recom .goodsList ul {
  padding: 0;
  list-style: none;
  display: flex;
  flex-wrap: wrap;
}
.recom .goodsList ul li {
  width: calc(19% - 10px); /* 每行显示5个商品，保持更小 */
  margin: 0 11px 20px; /* 左右5px的间距，底部20px的间距 */
  text-align: center;
  background-color: #EEE;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}
.recom .goodsList ul li:hover {
  box-shadow: 0px 0px 15px rgba(153, 153, 153, 0.7);
  background-color: #F5F5F5; /* 更改背景色 */
}
.recom .categoryName {
  font-size: 25px;
  font-weight: bold;
  margin-bottom: 5px;
}
.name, .dscp, .price {
  line-height: 25px;
}
.recom .name {
  font-size: 14px; /* 减小商品名称字体 */
  font-weight: bold;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.recom .dscp {
  color: #AAA;
  font-size: 12px; /* 减小描述字体 */
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.recom .price {
  font-size: 12px;
  color: var(--theme-color);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .ad ul li {
    flex: 1 1 48%; /* 手机显示2个广告位 */
  }
  .recom .goodsList ul li {
    width: calc(50% - 10px); /* 手机显示2个商品 */
  }
}

@media (max-width: 480px) {
  .recom .goodsList ul li {
    width: 100%; /* 手机显示1个商品 */
  }
}
</style>
