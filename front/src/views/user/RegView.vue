<template>
  <div style="display: flex; justify-content: center">
    <el-card style="width: 100%;max-width: 600px; margin: 10px; ">
      <h3 style="text-align: center; margin-bottom: 20px;">用户注册</h3>
      <!-- 添加对话框开始 -->
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
      <!-- 添加对话框结束 -->
    </el-card>
  </div>
</template>

<script setup>
import {ref} from "vue";
import UserApi from "@/api/UserApi.js";
import {ElMessage} from "element-plus";
//被添加用户的信息
const userAdd = ref({
  username: '',
  password: ''
});

function insert() {
  UserApi.insert(userAdd.value)
      .then(resp => {
        console.log(resp);
        if (resp.code == 10000) {
          // 弹出消息
          ElMessage.success(resp.msg);
          // 清空对话框中的数据
          userAdd.value = {
            username: '',
            password: ''
          };
        } else {
          // 弹出消息
          ElMessage.error(resp.msg);
        }
      });
}
</script>

<style scoped>

</style>