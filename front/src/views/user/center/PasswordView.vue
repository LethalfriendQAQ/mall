<template>
  <el-row>
    <el-col :span="12" style="font-size: 18px; font-weight: bold">账号安全</el-col>
  </el-row>
  <el-row :gutter="20" class="safe">
    <!-- 修改手机号 -->
    <el-col :span="8">
      <el-card shadow="hover" class="box-card">
        <el-icon style="font-size: 150px"><Iphone /></el-icon>
        <div slot="header" class="clearfix">
          <el-tag type="warning">手机号</el-tag>
        </div>
        <div>
          <p>当前手机号：155****095</p>
          <el-button type="primary">修改手机号</el-button>
        </div>
      </el-card>
    </el-col>

    <!-- 修改登录密码 -->
    <el-col :span="8">
      <el-card shadow="hover" class="box-card">
        <el-icon style="font-size: 150px"><Key /></el-icon>
        <div slot="header" class="clearfix">
          <el-tag type="warning">登录密码</el-tag>
        </div>
        <div>
          <p>定期更换密码有助于账号安全</p>
          <el-button type="primary" @click="chgPwdDialogShow = true">修改登录密码</el-button>
        </div>
      </el-card>
    </el-col>

    <!-- 修改支付密码 -->
    <el-col :span="8">
      <el-card shadow="hover" class="box-card">
        <el-icon style="font-size: 150px"><Money /></el-icon>
        <div slot="header" class="clearfix">
          <el-tag type="warning">支付密码</el-tag>
        </div>
        <div>
          <p>设置支付密码保障交易安全</p>
          <el-button type="primary" @click="chgPayPwdDialogShow = true">修改支付密码</el-button>
        </div>
      </el-card>
    </el-col>

    <!-- 实名认证 -->
    <el-col :span="8">
      <el-card shadow="hover" class="box-card">
        <el-icon style="font-size: 150px"><User /></el-icon>
        <div slot="header" class="clearfix">
          <el-tag type="warning">实名认证</el-tag>
        </div>
        <div>
          <p>已认证：**4华</p>
          <el-button type="primary">查看实名认证</el-button>
        </div>
      </el-card>
    </el-col>

    <!-- 注销账号 -->
    <el-col :span="8">
      <el-card shadow="hover" class="box-card">
        <el-icon style="font-size: 150px"><SwitchButton /></el-icon>
        <div slot="header" class="clearfix">
          <el-tag type="warning">注销账号</el-tag>
        </div>
        <div>
          <p>彻底关闭不再使用该账号</p>
          <el-button type="primary">确定注销账号</el-button>
        </div>
      </el-card>
    </el-col>
  </el-row>

  <!-- 修改密码对话框开始 -->
  <el-dialog v-model="chgPwdDialogShow" title="修改密码" width="500">
    <el-form-item label="原密码" label-width="20%">
      <el-input v-model="chgPwdObj.oldPwd" type="password" placeholder="请输入原密码" autocomplete="off" />
    </el-form-item>
    <el-form-item label="新密码" label-width="20%">
      <el-input v-model="chgPwdObj.newPwd" type="password" placeholder="请输入新密码" autocomplete="off" />
    </el-form-item>
    <el-form-item label="确认新密码" label-width="20%">
      <el-input v-model="chgPwdObj.newPwd1" type="password" placeholder="请确认新密码" autocomplete="off" />
    </el-form-item>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="chgPwdDialogShow = false">取消</el-button>
        <el-button type="primary" @click="changePassword">确认</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 修改密码对话框结束 -->

  <!-- 修改支付密码对话框开始 -->
  <el-dialog v-model="chgPayPwdDialogShow" title="修改支付密码" width="500">
    <el-form-item label="原密码" label-width="20%">
      <el-input v-model="chgPayPwdObj.oldPwd" type="password" placeholder="请输入原密码" autocomplete="off" />
    </el-form-item>
    <el-form-item label="新密码" label-width="20%">
      <el-input v-model="chgPayPwdObj.newPwd" type="password" placeholder="请输入新密码" autocomplete="off" />
    </el-form-item>
    <el-form-item label="确认新密码" label-width="20%">
      <el-input v-model="chgPayPwdObj.newPwd1" type="password" placeholder="请确认新密码" autocomplete="off" />
    </el-form-item>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="chgPayPwdDialogShow = false">取消</el-button>
        <el-button type="primary" @click="changePayPassword">确认</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 修改支付密码对话框结束 -->

</template>

<script setup>
import { Money, Key, Iphone, User } from "@element-plus/icons-vue";
import {ref} from "vue";
import {ElMessage} from "element-plus";
import userApi from "@/api/userApi.js";


const chgPwdDialogShow = ref(false);
const chgPayPwdDialogShow = ref(false);
const chgPwdObj = ref({
  oldPwd: null,
  newPwd: null,
  newPwd1: null,
})

const chgPayPwdObj = ref({
  oldPwd: null,
  newPwd: null,
  newPwd1: null,
})

function changePassword() {
  userApi.changePassword(chgPwdObj.value)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          chgPwdDialogShow.value = false;
        } else {
          ElMessage.error(resp.msg);
        }
      })
}
function changePayPassword() {
  userApi.changePayPassword(chgPayPwdObj.value)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          chgPayPwdDialogShow.value = false;
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

</script>

<style scoped>
.box-card {
  text-align: center;
  font-size: 14px;
}

.safe .el-col {
  margin-top: 20px;
}
</style>
