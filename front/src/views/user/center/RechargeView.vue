<template>
  <el-row>
    <el-col :span="12" style="font-size: 18px; font-weight: bold">账户充值</el-col>
  </el-row>
  <el-row>
    <el-col :span="24">
      <el-card shadow="hover" class="box-card">
        <div>
          <el-icon style="font-size: 50px"><Wallet /></el-icon>
          <p>当前余额：<span style="color: var(--theme-color)">{{ user.money }}</span>元</p>
        </div>
        <div>
          <el-form-item label="充值金额" label-width="20%">
            <el-input v-model="recharge.money" placeholder="请输入充值金额" autocomplete="off" />
          </el-form-item>
          <el-form-item label="支付密码" label-width="20%">
            <el-input v-model="recharge.password" type="password" placeholder="请输入支付密码" autocomplete="off" />
          </el-form-item>
        </div>
        <div>
          <el-button type="primary" style="width: 300px" @click="setRecharge">充值</el-button>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script setup>
import { Wallet} from "@element-plus/icons-vue";
import {ref} from "vue";
import userApi from "@/api/userApi.js";
import {ElMessage} from "element-plus";
import { useUserStore } from "@/stores/user.js";

const userStore = useUserStore();

const recharge = ref({
  password: "",
  money: ""
})

const user = ref({
  username: '',
  password: '',
  payPassword: null,
  salt: null,
  realname: null,
  sex: null,
  idCard: null,
  phone: null,
  email: null,
  regTime: null,
  money: null,
  status: 0
});

function selectById() {
  userApi.getInfo(userStore.userInfo.id)
      .then(resp => {
        if (resp.code == 10000) {
          user.value = resp.data;
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

function setRecharge() {
  userApi.recharge(recharge.value)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success("充值成功");
          recharge.value = {
            password: "",
            money: ""
          }
          selectById();
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

selectById();
</script>

<style scoped>
.box-card {
  text-align: center;
  font-size: 14px;
}
</style>