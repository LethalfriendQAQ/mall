<template>
  <el-container>
    <!--<el-header height="60px" style="border-radius: 20px;margin: 10px;margin-bottom: 0px;">-->
    <el-header height="60px">
      <el-menu
          default-active="1"
          class="el-menu-demo"
          mode="horizontal"
          background-color="#242424"
          text-color="#fff"
          :ellipsis="false"
          active-text-color="#b3ff24"
      >
        <el-menu-item index="1" style="width: 200px">在线商城</el-menu-item>
        <el-sub-menu index="2">
          <template #title>{{ admin.username }}</template>
          <el-menu-item index="center" @click="updateDialogShow = true">个人中心</el-menu-item>
          <el-menu-item index="chgPwd" @click="chgPwdDialogShow = true">修改密码</el-menu-item>
          <el-menu-item index="logout" @click="logout">退出登录</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-header>
    <el-container>
      <!--<el-aside width="300px" style="border-radius: 20px;margin: 10px;">-->
      <el-aside width="200px">
        <el-menu
            active-text-color="#b3ff24"
            background-color="#242424"
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
            <el-menu-item index="/admin/list" style="background-color: #4a5054;">管理员列表</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="2">
            <template #title>
              <el-icon><GoodsFilled /></el-icon>
              <span>商品分类管理</span>
            </template>
            <el-menu-item index="/admin/category" style="background-color: #4a5054;">分类列表</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="3">
            <template #title>
              <el-icon><Goods /></el-icon>
              <span>商品管理</span>
            </template>
            <el-menu-item index="/admin/goods" style="background-color: #4a5054;">商品列表</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="4">
            <template #title>
              <el-icon><UserFilled /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/admin/user" style="background-color: #4a5054;">用户列表</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="5">
            <template #title>
              <el-icon><Tickets /></el-icon>
              <span>订单管理</span>
            </template>
            <el-menu-item index="/admin/order" style="background-color: #4a5054;">订单列表</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      <el-main>
        <RouterView />
      </el-main>
    </el-container>
  </el-container>

  <!-- 修改密码对话框开始 -->
  <el-dialog v-model="chgPwdDialogShow" title="修改密码" width="500">
    <el-form>
      <el-form-item label="原密码" label-width="20%">
        <el-input v-model="chgPwdObj.oldPwd" type="password" placeholder="请输入原密码" autocomplete="off" />
      </el-form-item>
      <el-form-item label="新密码" label-width="20%">
        <el-input v-model="chgPwdObj.newPwd" type="password" placeholder="请输入新密码" autocomplete="off" />
      </el-form-item>
      <el-form-item label="确认新密码" label-width="20%">
        <el-input v-model="chgPwdObj.newPwd1" type="password" placeholder="请确认新密码" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="chgPwdDialogShow = false">取消</el-button>
        <el-button type="primary" @click="changePassword">确认</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 修改密码对话框结束 -->


  <!-- 修改管理员信息对话框开始 -->
  <el-dialog v-model="updateDialogShow" title="修改管理员个人信息" width="500">
    <el-form>
      <el-form-item label="用户名" label-width="20%">
        <el-input v-model="admin.username" disabled autocomplete="off" />
      </el-form-item>
      <el-form-item label="电话" label-width="20%">
        <el-input v-model="admin.phone" placeholder="请输入新电话" autocomplete="off" />
      </el-form-item>
      <el-form-item label="邮箱" label-width="20%">
        <el-input v-model="admin.email" placeholder="请输入邮箱" autocomplete="off" />
      </el-form-item>
      <el-form-item label="真实姓名" label-width="20%">
        <el-input v-model="admin.realname" placeholder="请输入真实姓名" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="updateDialogShow = false">取消</el-button>
        <el-button type="primary" @click="changeInfo">确认</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 修改管理员信息对话框结束 -->

</template>

<script setup>
import {RouterView} from "vue-router";
import router from "@/router/index.js";
import { useTokenStore} from "@/stores/token.js";
import adminApi from "@/api/adminApi.js";
import {ref} from "vue";
import {ElMessage} from "element-plus";


const tokenStore = useTokenStore();
const admin = ref({
  username: null,
  phone: null,
  email: null,
  realname: null
});

const chgPwdDialogShow = ref(false);

const chgPwdObj = ref({
  oldPwd: null,
  newPwd: null,
  newPwd1: null,
})

//是否显示管理员信息对话框
const updateDialogShow = ref(false);

function changeInfo() {
  adminApi.changeInfo(admin.value)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          updateDialogShow.value = false;
        } else {
          ElMessage.error(resp.msg);
        }
        getInfo();
      })
}

function changePassword() {
  adminApi.changePassword(chgPwdObj.value)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          //重置token并退出
          logout()
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

function logout() {
  //重置store中的token
  tokenStore.$reset();
  //跳转到登录页
  router.push('/admin/login');
}

//获取已登录用户的信息
function getInfo() {
  adminApi.getInfo()
      .then(resp => {
          admin.value = resp.data
      })
}

getInfo()
</script>

<style scoped>
.el-menu--horizontal > .el-menu-item:nth-child(1) {
  margin-right: auto;
}

.el-header {
  padding: 0px;
}
.el-container, .el-menu {
  height: 100%;
}

</style>