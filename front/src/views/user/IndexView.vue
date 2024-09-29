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
      <li><el-image src="/src/assets/ad.png" /></li>
      <li><el-image src="/src/assets/ad.png" /></li>
      <li><el-image src="/src/assets/ad.png" /></li>
      <li><el-image src="/src/assets/ad.png" /></li>
    </ul>
  </div>

  <!-- 推荐的商品 -->
  <div class="recom">
    <div class="categoryName">手机</div>
    <div class="goodsList">
      <ul>
        <li>
          <div class="pic"><el-image src="/src/assets/ad.png" /></div>
          <div class="name">华为Mete60</div>
          <div class="dscp">不买不爱国</div>
          <div class="price">￥ 6999</div>
        </li>
        <li>
          <div class="pic"><el-image src="/src/assets/ad.png" /></div>
          <div class="name">华为Mete60</div>
          <div class="dscp">不买不爱国</div>
          <div class="price">￥ 6999</div>
        </li>
        <li>
          <div class="pic"><el-image src="/src/assets/ad.png" /></div>
          <div class="name">华为Mete60</div>
          <div class="dscp">不买不爱国</div>
          <div class="price">￥ 6999</div>
        </li>
        <li>
          <div class="pic"><el-image src="/src/assets/ad.png" /></div>
          <div class="name">华为Mete60</div>
          <div class="dscp">不买不爱国</div>
          <div class="price">￥ 6999</div>
        </li>
        <li>
          <div class="pic"><el-image src="/src/assets/ad.png" /></div>
          <div class="name">华为Mete60</div>
          <div class="dscp">不买不爱国</div>
          <div class="price">￥ 6999</div>
        </li>
        <li>
          <div class="pic"><el-image src="/src/assets/ad.png" /></div>
          <div class="name">华为Mete60</div>
          <div class="dscp">不买不爱国</div>
          <div class="price">￥ 6999</div>
        </li>
        <li>
          <div class="pic"><el-image src="/src/assets/ad.png" /></div>
          <div class="name">华为Mete60</div>
          <div class="dscp">不买不爱国</div>
          <div class="price">￥ 6999</div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
  import {ref} from "vue";

  const banners = ref([
    "/src/assets/banner/banner1.png",
    "/src/assets/banner/banner2.png",
    "/src/assets/banner/banner3.png"
  ])
</script>

<style scoped>
  .ad {
    margin: 20px 0;
  }
  .ad ul li {
    float: left;
    width: 285px;
    margin-right: 20px;
  }
  .ad ul li:nth-child(4n) {
    margin-right: 0px;
  }
  .recom {
    margin-bottom: 20px;
  }
  .recom .goodsList ul li {
    width: 224px;
    float: left;
    margin-right: 20px;
    margin-bottom: 20px;
    text-align: center;
    background-color: #EEE;

  }
  .recom .goodsList ul li:hover {
    box-shadow: 0px 0px 20px #999999;
  }
  .recom .goodsList ul li:nth-child(5n) {
    margin-right: 0px;
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
    font-size: 16px;
    font-weight: bold;
  }
  .recom .dscp {
    color: #AAA;
  }
  .recom .price {
    font-size: 12px;
    color: var(--theme-color);
  }
</style>