<template>
  <div class="category">
    <!--显示所有上架商品-->
    <ul>
      <li @click="selectFirstCategory({})" :class="{active: !firstCaregorySelected.id}">全部</li>
      <li v-for="(category, index) in firstCaregoryList"
          :key="index"
          :class="{active: category.id == firstCaregorySelected.id}"
          @click="selectFirstCategory(category)">{{ category.name }}</li>
    </ul>
    <ul v-if="firstCaregorySelected.id">
      <li @click="selectSecondCategory({})" :class="{active: !secondCaregorySelected.id}">全部</li>
      <li v-for="(category, index) in secondCategoryList"
          :key="index"
          :class="{active: category.id == secondCaregorySelected.id}"
          @click="selectSecondCategory(category)">{{ category.name }}</li>
    </ul>
  </div>

  <!-- 显示搜索到的商品 -->
  <div class="goodsList">
    <ul>
      <li v-for="(goods, index) in pageInfo.list" :key="index" @click="toGoodsView(goods.id)">
        <div class="pic">
          <el-image :src="`${SERVER_ADDR}/goods/pic/${goods.picList[0].url}`" />
        </div>
        <div class="name">{{ goods.name }}</div>
        <div class="dscp">{{ goods.dscp }}</div>
        <div class="price">￥ {{ goods.price }}</div>
      </li>
    </ul>
    <el-row class="row-bg" justify="center" style="margin-top: 20px">
      <el-pagination background layout="prev, pager, next" :total="pageInfo.total"
                     :page-size="pageInfo.pageSize" @change="search"/>
    </el-row>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import categoryApi from "@/api/categoryApi.js";
import goodsApi from "@/api/goodsApi.js";
import { useRouter } from "vue-router";

const router = useRouter();

//通过路径传递参数
const props = defineProps({
  categoryId: String
});

//所有需要显示的父分类
const firstCaregoryList = ref([]);
//所有要显示的子分类
const secondCategoryList = ref([]);
//被选中的父分类
const firstCaregorySelected = ref({});
//被选中的子分类
const secondCaregorySelected = ref({});
//被选中的分类id - 可能是父分类的id也可能是子分类的id
const categoryId = ref(null);
//分页信息
const pageInfo = ref({
  pages: 0,
  pageSize: 0,
  pageNum: 0
});
//服务器的地址
const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);

watch(props, () => {
  getParent();
});

//点击父分类调用的函数
function selectFirstCategory(category) {
  firstCaregorySelected.value = category;
  secondCategoryList.value = [];
  categoryId.value = firstCaregorySelected.value.id || null;

  if (category.childList) {
    category.childList
        .filter(c => c.status == 1)
        .forEach(c => secondCategoryList.value.push(c));
  }
  search(1);
}

//点击子分类调用的函数
function selectSecondCategory(category) {
  secondCaregorySelected.value = category;
  categoryId.value = secondCaregorySelected.value.id || firstCaregorySelected.value.id;
}

//获取父分类   上架   推荐
function getParent() {
  const condition = {
    parentId: 0,
    recom: 1,
    status: 1
  };
  categoryApi.selectByPage(condition)
      .then(resp => {
        firstCaregoryList.value = resp.data;
        let flag = true;
        firstCaregoryList.value.forEach(c => {
          if (props.categoryId == c.id) {
            flag = false;
            selectFirstCategory(c);
          }
        });
        if (flag) {
          selectFirstCategory({});
        }
      });
}

//搜索
function search(pageNum) {
  const condition = {
    status: 1,
    categoryId: categoryId.value
  };
  goodsApi.selectByPage1(condition, pageNum, 20)
      .then(resp => {
        pageInfo.value = resp.data;
      });
}

//跳转到商品详情页
function toGoodsView(id) {
  router.push({
    path: '/user/goods',
    query: {
      id
    }
  });
}

search();
getParent();
</script>

<style scoped>
.category {
  margin: 20px 0;
}

.category ul {
  display: flex;
  flex-wrap: wrap;
  padding: 0;
  list-style: none;
}

.category ul li {
  margin: 10px 15px;
  font-size: 14px;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background-color 0.3s, color 0.3s;
}

.category ul li:hover {
  background-color: #f0f0f0; /* Hover effect */
}

.active {
  font-weight: bold;
  color: var(--theme-color);
  background-color: #e0e0e0; /* Active background */
}

.goodsList {
  margin-top: 20px;
}

.goodsList ul {
  display: flex;
  flex-wrap: wrap;
  padding: 0;
  list-style: none;
}

.goodsList ul li {
  width: calc(19% - 10px); /* 每行显示5个商品，保持更小 */
  margin: 0 11px 20px; /* 左右5px的间距，底部20px的间距 */
  text-align: center;
  background-color: #EEE;
  cursor: pointer;
  border-radius: 4px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s;
}

.goodsList ul li:hover {
  box-shadow: 0px 0px 20px rgba(0, 0, 0, 0.2);
}

.name, .dscp, .price {
  line-height: 25px;
}

.name {
  font-size: 16px;
  font-weight: bold;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
