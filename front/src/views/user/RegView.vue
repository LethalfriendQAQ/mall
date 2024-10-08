<template>
<!--  <div style="display: flex; justify-content: center">
    <el-card style="width: 100%;max-width: 600px; margin: 10px; ">
      <h3 style="text-align: center; margin-bottom: 20px;">用户注册</h3>
        <el-form-item label="用户名" label-width="20%">
          <el-input v-model="userAdd.username" placeholder="请输入用户名" autocomplete="off" />
        </el-form-item>
        <el-form-item label="密码" label-width="20%">
          <el-input v-model="userAdd.password" type="password" placeholder="请输入密码" autocomplete="off" />
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" @click="insert">确定</el-button>
          <el-button>取消</el-button>
        </el-form-item>
    </el-card>
  </div>-->

  <el-row style="margin-top: 15px">
      <el-col :span="12">
        <el-image src="/src/assets/ad.png" />
      </el-col>
      <el-col :span="12" style="float: right">
        <el-form label-width="auto" class="loginForm" :rules="rules">
          <h3 style="text-align: center; margin-bottom: 20px;">用户注册</h3>
          <el-form-item label="用户名" prop="username" label-width="20%">
            <el-input prefix-icon="UserFilled" v-model="userReg.username" placeholder="请输入用户名" autocomplete="off" />
          </el-form-item>
          <el-form-item label="密码" prop="password" label-width="20%">
            <el-input prefix-icon="Key" v-model="userReg.password" type="password" placeholder="请输入密码" autocomplete="off" />
          </el-form-item>
          <el-form-item label="验证码" prop="captchaInput" label-width="20%">
            <el-input prefix-icon="Picture" style="width: 220px" v-model="userReg.captchaInput" placeholder="请输入验证码" autocomplete="off" />
            <el-image style="margin-left: 5px" :src="imageBase64Data" @click="getCaptcha" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="regBtn" @click="reg">确定</el-button>
          </el-form-item>
        </el-form>
      </el-col>
  </el-row>
</template>

<script setup>
import {ref} from "vue";
import userApi from "@/api/userApi.js";
import {ElMessage} from "element-plus";
//被注册用户的信息
const userReg = ref({
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
        userReg.value.key = resp.data.key;
        imageBase64Data.value = resp.data.imageBase64Data;
      });
}

function reg() {
  userApi.reg(userReg.value)
      .then(resp => {
        if (resp.code == 10000) {
          // 弹出消息
          ElMessage.success(resp.msg);
          // 清空对话框中的数据
          // userReg.value = {
          //   username: '',
          //   password: '',
          //   key: '',
          //   captchaInput: ''
          // };
        } else {
          // 弹出消息
          ElMessage.error(resp.msg);
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
  .regBtn {
    width: 400px;
    margin-left: 100px;
  }
</style>