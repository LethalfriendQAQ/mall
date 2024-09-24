<template>
  <el-container>
    <!--<el-header height="60px" style="border-radius: 20px;margin: 10px;margin-bottom: 0px;">-->
    <el-header height="60px">
      <el-menu
          default-active="1"
          class="el-menu-demo"
          mode="horizontal"
          background-color="#545c64"
          text-color="#fff"
          :ellipsis="false"
          active-text-color="#ffd04b"
      >
        <el-menu-item index="1" style="width: 200px">在线商城</el-menu-item>
        <el-sub-menu index="2">
          <template #title>Admin</template>
          <el-menu-item index="center">个人中心</el-menu-item>
          <el-menu-item index="chgPwd">修改密码</el-menu-item>
          <el-menu-item index="logout">退出登录</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-header>
    <el-container>
      <!--<el-aside width="300px" style="border-radius: 20px;margin: 10px;">-->
      <el-aside width="200px">
        <el-menu
            active-text-color="#ffd04b"
            background-color="#545c64"
            class="el-menu-vertical-demo"
            text-color="#fff"
            :router="true"
        >
          <el-menu-item index="/admin/index">
            <el-icon><House /></el-icon>
            后台首页
          </el-menu-item>
          <el-sub-menu index="1">
            <template #title>
              <el-icon><Avatar /></el-icon>
              <span>管理员管理</span>
            </template>
            <el-menu-item index="/admin/list">管理员列表</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="2">
            <template #title>
              <el-icon><GoodsFilled /></el-icon>
              <span>商品分类管理</span>
            </template>
            <el-menu-item index="/admin/category">分类列表</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="3">
            <template #title>
              <el-icon><Goods /></el-icon>
              <span>商品管理</span>
            </template>
            <el-menu-item index="/admin/goods">商品列表</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="4">
            <template #title>
              <el-icon><UserFilled /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/admin/user">用户列表</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="5">
            <template #title>
              <el-icon><Tickets /></el-icon>
              <span>订单管理</span>
            </template>
            <el-menu-item index="/admin/order">订单列表</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      <el-main>
        <RouterView />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import {RouterView} from "vue-router";
import router from "@/router/index.js";
</script>

<style scoped>
.el-menu--horizontal > .el-menu-item:nth-child(1) {
  margin-right: auto;
}

.el-header {
  padding: 0px;
}
</style>