<template>
  <el-row style="margin-top: 15px">
    <el-col :span="12">
      <el-image src="/src/assets/ad.png" />
    </el-col>
    <el-col :span="12" style="float: right">
      <el-form label-width="auto" class="loginForm" :rules="rules" :model="userInfo" @keydown.enter="login">
        <h3 style="text-align: center; margin-bottom: 20px;">用户登录</h3>
        <el-form-item label="用户名" prop="username" label-width="20%">
          <el-input prefix-icon="UserFilled" v-model="userInfo.username" placeholder="请输入用户名" autocomplete="off" />
        </el-form-item>
        <el-form-item label="密码" prop="password" label-width="20%">
          <el-input prefix-icon="Key" v-model="userInfo.password" type="password" placeholder="请输入密码" autocomplete="off" />
        </el-form-item>
        <el-form-item label="验证码" prop="captchaInput" label-width="20%">
          <el-input prefix-icon="Picture" style="width: 220px" v-model="userInfo.captchaInput" placeholder="请输入验证码" autocomplete="off" />
          <el-image style="margin-left: 5px" :src="imageBase64Data" @click="getCaptcha" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="loginBtn" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<script setup>
import {ref} from "vue";
import userApi from "@/api/userApi.js";
import {ElMessage} from "element-plus";
import { useRouter } from "vue-router";
import { useTokenStore } from "@/stores/token.js";
import { useUserStore } from "@/stores/user.js";

const userStore = useUserStore();
const tokenStore = useTokenStore();

const router = useRouter();

//用户的信息
const userInfo = ref({
  username: '',
  password: '',
  key: '',
  captchaInput: ''
});

const imageBase64Data = ref(null);
// 表单验证规则
const rules = {
  username: [
    {required: true, message: '用户名不能为空', trigger: 'blur'}
  ],
  password: [
    {required: true, message: '密码不能为空', trigger: 'blur'}
  ],
  captchaInput: [
    {required: true, message: '验证码不能为空', trigger: 'blur'}
  ]
};
//获取验证码
function getCaptcha() {
  userApi.captcha()
      .then(resp => {
        userInfo.value.key = resp.data.key;
        imageBase64Data.value = resp.data.imageBase64Data;
      });
}

function login() {
  userApi.login(userInfo.value)
      .then(resp => {
        if (resp.code == 10000) {
          // 弹出消息
          ElMessage.success(resp.msg);
          //保存token
          tokenStore.updateToken(resp.data);
          //获取用户信息
          getInfo();
          //跳转到前台首页
          router.push("/user/index");
        } else {
          // 弹出消息
          ElMessage.error(resp.msg);
        }
      });
}
function getInfo() {
  userApi.getInfo()
      .then(resp => {
        if (resp.code == 10000) {
          //将已登录用户的信息保存在pinia中
          userStore.updateUser(resp.data);
        }
      });
}

getCaptcha();
</script>

<style scoped>
.loginForm {
  width: 500px;
  margin: 45px 40px;
}
.loginBtn {
  width: 400px;
  margin-left: 100px;
}
</style>