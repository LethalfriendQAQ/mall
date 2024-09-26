<template>
  <el-col :span="24">
    <el-card>
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item>
          <el-button type="primary" @click="addDialogShow = true">添加</el-button>
        </el-form-item>
        <el-form-item style="float: right;">
          <el-input v-model="condition.username" placeholder="请输入要搜索的用户名" @input="selectByPage(1);" />
        </el-form-item>
        <el-form-item style="float: right;">
          <el-select
              v-model="condition.status"
              placeholder="请选择账号状态"
              style="width: 150px"
              clearable
              :value-on-clear="null"
              @change="selectByPage(1)"
          >
            <el-option label="未认证" value="0"/>
            <el-option label="已认证" value="1"/>
            <el-option label="已注销" value="2"/>
          </el-select>
        </el-form-item>
      </el-form>
      <el-table :data="pageInfo.list" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="50px"/>
        <el-table-column prop="username" label="用户名"/>
        <el-table-column prop="password" label="密码"/>
        <el-table-column prop="salt" label="盐"/>
        <el-table-column prop="realname" label="真实姓名"/>
        <el-table-column prop="sex" label="性别"/>
        <el-table-column prop="idCard" label="真实姓名"/>
        <el-table-column prop="phone" label="真实姓名"/>
        <el-table-column prop="email" label="真实姓名"/>
        <el-table-column prop="regTime" label="真实姓名"/>
        <el-table-column prop="money" label="真实姓名"/>
        <el-table-column label="当前状态">
          <template #default="scope">
            <el-tag type="info" v-if="scope.row.status == 0">未认证</el-tag>
            <el-tag type="success" v-if="scope.row.status == 1">已认证</el-tag>
            <el-tag type="warning" v-if="scope.row.status == 2">已注销</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150px">
          <template #default="scope">
            <el-button type="primary" size="small" @click="selectById(scope.row.id)" round>修改</el-button>
            <el-popconfirm title="你确定要删除该分类吗？" confirm-button-text="确认" cancel-button-text="取消"
                           width="200px" @confirm="deleteCategory(scope.row.id)">
              <template #reference>
                <el-button size="small" type="danger" round>删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <el-row class="row-bg" justify="center" style="margin-top: 20px">
        <el-pagination background layout="prev, pager, next" :total="pageInfo.total"
                       :page-size="pageInfo.pageSize" @change="selectByPage"/>
      </el-row>
    </el-card>
  </el-col>

  <!-- 添加对话框开始 -->
  <el-dialog v-model="addDialogShow" title="添加用户" width="500">
      <el-form-item label="用户名" label-width="20%">
        <el-input v-model="userAdd.username" placeholder="请输入用户名" autocomplete="off" />
      </el-form-item>
      <el-form-item label="密码" label-width="20%">
        <el-input v-model="userAdd.password" placeholder="请输入密码" autocomplete="off" />
      </el-form-item>
      <el-form-item label="支付密码" label-width="20%">
        <el-input v-model="userAdd.payPassword" placeholder="请输入支付密码" autocomplete="off" />
      </el-form-item>
      <el-form-item label="盐" label-width="20%">
        <el-input v-model="userAdd.salt" placeholder="请输入盐" autocomplete="off" />
      </el-form-item>
      <el-form-item label="真实姓名" label-width="20%">
        <el-input v-model="userAdd.realname" placeholder="请输入真实姓名" autocomplete="off" />
      </el-form-item>
      <el-form-item label="性别" label-width="20%">
        <el-radio-group v-model="userAdd.sex">
          <el-radio label="男" :value="男" size="large" />
          <el-radio label="女" :value="女" size="large" />
        </el-radio-group>

      </el-form-item>
      <el-form-item label="卡号" label-width="20%">
        <el-input v-model="userAdd.idCard" placeholder="请输入卡号" autocomplete="off" />
      </el-form-item>
      <el-form-item label="电话号码" label-width="20%">
        <el-input v-model="userAdd.phone" placeholder="请输入电话号码" autocomplete="off" />
      </el-form-item>
    <el-form-item label="邮箱" label-width="20%">
      <el-input v-model="userAdd.email" placeholder="请输入邮箱" autocomplete="off" />
    </el-form-item>
    <el-form-item label="注册时间" label-width="20%">
        <el-date-picker
            v-model="userAdd.regTime"
            type="datetime"
            placeholder="请选择注册日期"
            format="YYYY/MM/DD HH:mm:ss"
        />
    </el-form-item>
    <el-form-item label="余额" label-width="20%">
      <el-input v-model="userAdd.money" placeholder="请输入余额" autocomplete="off" />
    </el-form-item>
      <el-form-item label="账号状态" label-width="20%">
        <el-radio-group v-model="userAdd.status">
          <el-radio label="未认证" :value="0" size="large" />
          <el-radio label="已认证" :value="1" size="large" />
          <el-radio label="已注销" :value="2" size="large" />
        </el-radio-group>
      </el-form-item>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="addDialogShow = false">取消</el-button>
        <el-button type="primary" @click="insert">确认</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 添加对话框结束 -->
</template>

<script setup>
import {ref} from "vue";
import UserApi from "@/api/UserApi.js";
import goodsApi from "@/api/goodsApi.js";
import {ElMessage} from "element-plus";

//搜索条件
const condition = ref({
  username: null,
  realname: null,
  status: null
});
//分页信息b
const pageInfo = ref({
  pages: 0,
  pageSize: 0,
  pageNum: 0
});
//是否显示添加的对话框
const addDialogShow = ref(false);
//是否显示修改的对话框
const updateDialogShow = ref(false);

//被添加用户的信息
const userAdd = ref({
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

function insert() {
  UserApi.insert(userAdd.value)
      .then(resp => {
        console.log(resp);
        if (resp.code == 10000) {
          // 弹出消息
          ElMessage.success(resp.msg);
          // 隐藏对话框
          addDialogShow.value = false;
          // 清空对话框中的数据
          userAdd.value = {
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
          };
          // 刷新表格数据
          selectByPage(pageInfo.value.pageNum);
        } else {
          // 弹出消息
          ElMessage.error(resp.msg);
        }
      });
}


//分页查询
function selectByPage(pageNum) {
  UserApi.selectByPage(condition.value, pageNum, 5)
      .then(resp => {
        pageInfo.value = resp.data;
      })
}

selectByPage();
</script>

<style scoped>

</style>