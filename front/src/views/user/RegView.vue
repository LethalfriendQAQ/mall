<template>
  <el-row style="margin-top: 15px">
      <el-col :span="12">
        <el-image style="height: 270px; margin-left: 90px" src="/src/assets/iphone16_2.png" />
      </el-col>
      <el-col :span="12" style="float: right">
        <el-form  class="regForm" :rules="rules" :model="userInfo" @keydown.enter="reg">
          <h3 style="text-align: center; margin-bottom: 20px;">用户注册</h3>
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
            <el-button type="primary" class="regBtn" @click="reg">注册</el-button>
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

const router = useRouter();

//被注册用户的信息
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

function reg() {
  userApi.reg(userInfo.value)
      .then(resp => {
        if (resp.code == 10000) {
          // 弹出消息
          ElMessage.success(resp.msg);
          router.push("/user/login");
        } else {
          // 弹出消息
          ElMessage.error(resp.msg);
        }
      });
}

getCaptcha();
</script>

<style scoped>
  .regForm {
    width: 500px;
    margin: 45px 40px;
  }
  .regBtn {
    width: 400px;
    margin-left: 100px;
  }
</style>