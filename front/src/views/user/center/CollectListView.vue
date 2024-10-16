<template>
    <el-row style="margin-bottom: 20px">
      <el-col :span="12" style="font-size: 18px; font-weight: bold">我的收藏</el-col>
    </el-row>

    <!-- 收藏的商品 -->
    <div class="collection">
      <div class="goodsList">
        <ul>
          <li v-for="(item, index) in collectItems" :key="index" @click="toGoodsView(item.goods.id)">
            <div class="pic">
              <el-image v-if="item.goods.picList && item.goods.picList.length > 0" :src="`${SERVER_ADDR}/goods/pic/${item.goods.picList[0].url}`" />
            </div>
            <div class="name">{{ item.goods.name }}</div>
            <div class="dscp">{{ item.goods.dscp }}</div>
            <div class="price">￥ {{ item.goods.price }}</div>
            <div style="text-align: right; padding: 5px">
              <el-button type="danger" size="small" @click.stop="cancelCollect(item.id)">取消收藏</el-button>
            </div>
          </li>
        </ul>
      </div>
    </div>
    <el-row class="row-bg" justify="center" style="margin-top: 20px">
      <el-pagination background layout="prev, pager, next" :total="pageInfo.total"
                     :page-size="pageInfo.pageSize" @change="selectCollects"/>
    </el-row>
</template>

<script setup>
import { ref } from "vue";
import { ElMessage } from "element-plus";
import collectApi from "@/api/collectApi.js";
import { useRouter } from "vue-router";

const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);
const router = useRouter();

const collectItems = ref([]); // 用于存储收藏的商品

const pageInfo = ref({
  pages: 0,
  pageSize: 0,
  pageNum: 0
});

function cancelCollect(id) {
  collectApi.delete(id)
      .then(resp => {
        console.log(resp);
        if(resp.code == 10000) {
          ElMessage.success(resp.msg);
          //刷新收藏的状态
          selectCollects();
        } else {
          ElMessage.error(resp.msg);
        }
      })
}


// 获取收藏商品
function selectCollects(pageNum, pageSize) {
  collectApi.selectByPage(pageNum, pageSize)
      .then(resp => {
        if (resp.code === 10000) {
          collectItems.value = resp.data.list; // 使用返回的list数组
          pageInfo.value = resp.data; // 更新分页信息
        } else {
          ElMessage.error(resp.msg);
        }
      });
}

selectCollects(1, 8);

// 跳转到商品详情页
function toGoodsView(id) {
  router.push({ path: '/user/goods', query: { id } });
}
</script>

<style scoped>
.collection {
  margin-top: 20px;
}
.goodsList ul {
  display: flex;
  flex-wrap: wrap;
  padding: 0;
  list-style: none;
}
.goodsList li {
  width: 230px;
  margin-right: 20px;
  margin-bottom: 20px;
  text-align: center;
  background-color: #FFF; /* 白色背景 */
  border: 1px solid #E6E6E6; /* 边框颜色 */
  border-radius: 8px; /* 圆角 */
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); /* 阴影效果 */
  transition: all 0.3s; /* 动画效果 */
  cursor: pointer;
}
.goodsList li:hover {
  transform: translateY(-5px); /* 悬停时向上移动 */
  box-shadow: 0 4px 16px var(--theme-color); /* 悬停时增强阴影 */
}
.pic {
  padding: 10px;
}
.name {
  font-size: 16px;
  font-weight: bold;
  color: #333; /* 商品名称颜色 */
  margin: 10px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap; /* 单行显示 */
}
.dscp {
  color: #AAA; /* 描述颜色 */
  margin: 5px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap; /* 单行显示 */
}
.price {
  font-size: 14px;
  color: var(--theme-color); /* 使用主题色 */
}
</style>
