<template>
  <el-row style="margin-bottom: 20px">
    <el-col :span="12" style="font-size: 18px; font-weight: bold">个人信息</el-col>
  </el-row>
  <el-form :model="userMsg" label-width="auto" style="max-width: 600px">
    <el-form-item label="用户名">
      <el-input v-model="userMsg.username" />
    </el-form-item>
    <el-form-item label="性别">
      <el-radio-group v-model="userMsg.sex">
        <el-radio value="男">男</el-radio>
        <el-radio value="女">女</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="手机号">
      <el-input v-model="userMsg.phone" />
    </el-form-item>
    <el-form-item label="邮箱">
      <el-input v-model="userMsg.email" />
    </el-form-item>
    <el-form-item style="float: right">
      <el-button type="primary" @click="updateInfo">提交</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useUserStore } from "@/stores/user.js";
import userApi from "@/api/userApi.js";
import {ElMessage} from "element-plus";

const userStore = useUserStore();

const userMsg = ref({
  username: "",
  sex: "",
  phone: "",
  email: "",
  id: ""
})

/*const form = ref({
  username: "",
  sex: "",
  phone: "",
  email: "",
  id: ""
});
// 回显用户信息到表单
onMounted(() => {
  if (userStore.userInfo) {
    form.value.username = userStore.userInfo.username;
    form.value.sex = userStore.userInfo.sex;
    form.value.phone = userStore.userInfo.phone;
    form.value.email = userStore.userInfo.email;
    form.value.id = userStore.userInfo.id;
  }
});*/

function updateInfo() {
  userApi.update(userMsg.value)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          selectById();
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

function selectById() {
  userApi.selectById(userStore.userInfo.id)
      .then(resp => {
        if (resp.code == 10000) {
          userMsg.value = resp.data
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

selectById();
</script>

<style scoped>

</style>
