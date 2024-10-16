<template>
  <el-row>
    <el-col :span="12" style="font-size: 18px; font-weight: bold">账号安全</el-col>
  </el-row>
  <el-row :gutter="20" class="safe" :model="info">
    <!-- 修改手机号 -->
    <!--<el-col :span="8">
      <el-card shadow="hover" class="box-card">
        <el-icon style="font-size: 150px"><Iphone /></el-icon>
        <div slot="header" class="clearfix">
          <el-tag type="warning">手机号</el-tag>
        </div>
        <div>
          <p>当前手机号：{{ info.phone }}</p>
          <el-button type="primary">修改手机号</el-button>
        </div>
      </el-card>
    </el-col>-->

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
        <div v-if="user.payPassword != null">
          <p>设置支付密码保障交易安全</p>
          <el-button type="primary" @click="chgPayPwdDialogShow = true">修改支付密码</el-button>
        </div>
        <div v-else>
          <p>设置支付密码保障交易安全</p>
          <el-button type="primary" @click="setPayPwdDialogShow = true">设置支付密码</el-button>
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
        <div v-if="info.status == 1">
          <p>已认证：{{ info.realname }}</p>
          <el-button type="primary" plain disabled>已实名认证</el-button>
        </div>
        <div v-else>
          <p>未认证</p>
          <el-button type="primary">实名认证</el-button>
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

  <!-- 设置支付密码对话框开始 -->
  <el-dialog v-model="setPayPwdDialogShow" title="设置支付密码" width="500">
    <el-form-item label="登录密码" label-width="20%">
      <el-input v-model="setPayPwd.password" type="password" placeholder="请输入登录密码" autocomplete="off" />
    </el-form-item>
    <el-form-item label="支付密码" label-width="20%">
      <el-input v-model="setPayPwd.payPassword" type="password" placeholder="请设置支付密码" autocomplete="off" />
    </el-form-item>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="setPayPwdDialogShow = false">取消</el-button>
        <el-button type="primary" @click="update">确认</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 设置支付密码对话框结束 -->

</template>

<script setup>
import { Money, Key, Iphone, User } from "@element-plus/icons-vue";
import {onMounted, ref} from "vue";
import {ElMessage} from "element-plus";
import userApi from "@/api/userApi.js";
import { useUserStore } from "@/stores/user.js";

const userStore = useUserStore();
const chgPwdDialogShow = ref(false);
const chgPayPwdDialogShow = ref(false);
const setPayPwdDialogShow = ref(false);
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

const info = ref({
  realname: "",
  phone: "",
  id: "",
  password: "",
  payPassword: "",
  status: ""
});

const setPayPwd = ref({
  password: "",
  payPassword: ""
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

onMounted(() => {
  if (userStore.userInfo) {
    info.value.username = userStore.userInfo.username;
    info.value.phone = userStore.userInfo.phone;
    info.value.realname = userStore.userInfo.realname;
    info.value.password = userStore.userInfo.password;
    info.value.payPassword = userStore.userInfo.payPassword;
    info.value.id = userStore.userInfo.id;
    info.value.status = userStore.userInfo.status;
  }
});

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

function selectById() {
  userApi.getInfo(info.value.id)
      .then(resp => {
        if (resp.code == 10000) {
          user.value = resp.data;
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

function update() {
  userApi.setPayPwd(setPayPwd.value)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          setPayPwdDialogShow.value = false;
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

.safe .el-col {
  margin-top: 20px;
}
</style>
