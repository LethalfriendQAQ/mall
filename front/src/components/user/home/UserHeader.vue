<template>
  <div class="nav_bg">
    <div class="center">
      <el-row>
        <el-col :span="20">
          <ul>
            <li><RouterLink to="/">商城首页</RouterLink></li>
            <li><RouterLink to="">商城首页</RouterLink></li>
            <li><RouterLink to="">商城首页</RouterLink></li>
            <li><RouterLink to="">商城首页</RouterLink></li>
            <li><RouterLink to="">商城首页</RouterLink></li>
            <li><RouterLink to="">商城首页</RouterLink></li>
            <li><RouterLink to="">商城首页</RouterLink></li>
            <li><RouterLink to="">商城首页</RouterLink></li>
          </ul>
        </el-col>
        <el-col :span="4">
          <ul class="user">
            <template v-if="!userStore.userInfo">
              <li><RouterLink to="/user/login">登录</RouterLink></li>
              <li><RouterLink to="/user/reg">注册</RouterLink></li>
            </template>
            <template v-else>
              <li><el-link :underline="false" @click="logout">退出</el-link></li>
              <li><el-link>{{ userStore.userInfo.username }}</el-link></li>
              <li class="cartLi"><RouterLink to="/user/cart"><el-icon><ShoppingCart /></el-icon></RouterLink></li>
            </template>
          </ul>
        </el-col>
      </el-row>
    </div>
  </div>

  <!-- 头部搜索 -->
  <div class="center">
    <el-row>
      <el-col :span="16">
        <img src="/src/assets/apple.png" style="height: 50px; margin: 10px" />
      </el-col>
      <el-col :span="8">
        <div class="search">
            <el-button class="search_btn">
              <el-icon><Search /></el-icon>
            </el-button>
            <el-input type="text" class="search_input" />
        </div>
      </el-col>
    </el-row>
  </div>
  <!-- 上架的第一级分类 -->
  <div class="center">
    <ul class="category" >
      <li v-for="(category, index) in parentList" :key="index"><RouterLink :to="`/user/search/${category.id}`" >{{ category.name }}</RouterLink></li>
    </ul>
  </div>
  <!--  -->


</template>

<script setup>

import {Search} from "@element-plus/icons-vue";
import {ref} from "vue";
import categoryApi from "@/api/categoryApi.js";
import { useUserStore } from "@/stores/user.js";
import { useTokenStore } from "@/stores/token.js";
import { useRouter } from "vue-router";

const router = useRouter();
const tokenStore = useTokenStore();
const userStore = useUserStore();
const parentList = ref([])

function getParent() {
  const condition = ({
    parentId: 0,
    status: 1
  });
  categoryApi.selectByPage(condition)
      .then(resp => {
        parentList.value = resp.data;
      })
}
//退出
function logout() {
  //清空token
  tokenStore.$reset();
  //清空用户信息
  userStore.$reset();
  //跳转到前台首页
  router.push("/user/index");
}

getParent();
</script>

<style scoped>
  .cartLi .el-icon {
    top: 5px;
  }
  .nav_bg {
    background-color: #333;
    line-height: 40px;
  }

  ul li {
    float: left;
    margin-right: 20px;
  }
  .user li {
    float: right;
  }
  a {
    color: #999;
  }

  a:hover {
    color: aliceblue;
  }
  .search {
    padding-top: 20px;
  }
  .search_input{
    width: 260px;
    float: right;
  }
  .search_btn {
    float: right;
    color: #fff;
    margin-left: 5px;
    border-radius: 10px;
    border: 0px;
    background-color: #000000;
  }
  .search_btn:hover {
    background-color: var(--theme-color);
  }
  .category {
    background-color: var(--theme-color);
    line-height: 30px;
  }
  .category li {
    float: left;
    padding: 0 10px;
  }
  .category li a {
    color: #FFF;
  }
  .category li:hover {
    background-color: black;
  }
</style>