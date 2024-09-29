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
            <el-option label="禁用" value="1"/>
            <el-option label="正常" value="0"/>
          </el-select>
        </el-form-item>
      </el-form>
      <el-table :data="pageInfo.list" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="50px"/>
        <el-table-column prop="username" label="用户名"/>
        <el-table-column prop="phone" label="电话号码"/>
        <el-table-column prop="email" label="邮箱"/>
        <el-table-column prop="realname" label="真实姓名"/>
        <el-table-column prop="createTime" label="创建时间"/>
        <el-table-column label="当前状态">
          <template #default="scope">
            <el-switch
                v-model="scope.row.status"
                inline-prompt
                :inactive-value="0"
                :active-value="1"
                inactive-text="禁用"
                active-text="正常"
                style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                @change="chgStatus(scope.row.id, scope.row.status)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-popconfirm title="你确定要重置密码吗？" confirm-button-text="确认" cancel-button-text="取消"
                           width="200px" @confirm="ResetPassword(scope.row.id, scope.row.salt)">
              <template #reference>
                <el-button size="small" type="warning" round>重置密码</el-button>
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
  <el-dialog v-model="addDialogShow" title="添加管理员" width="500">
    <el-form>
      <el-form-item label="名称" label-width="20%">
        <el-input v-model="adminAdd.username" placeholder="请输入用户名" autocomplete="off" />
      </el-form-item>
      <el-form-item label="电话号码" label-width="20%">
        <el-input v-model="adminAdd.phone" placeholder="请输入电话号码" autocomplete="off" />
      </el-form-item>
      <el-form-item label="邮箱" label-width="20%">
        <el-input v-model="adminAdd.email" placeholder="请输入邮箱" autocomplete="off" />
      </el-form-item>
      <el-form-item label="真实姓名" label-width="20%">
        <el-input v-model="adminAdd.realname" placeholder="请输入真实姓名" autocomplete="off" />
      </el-form-item>
    </el-form>
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
import adminApi from "@/api/adminApi.js";
import {ref} from "vue";
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
  pageNum: 0,
});

const addDialogShow = ref(false);

const adminAdd = ref({
  username: null,
  password: '',
  salt: null,
  phone: null,
  email:null,
  realname: null,
  createTime: null,
  status: null
})

function insert() {
  adminApi.insert(adminAdd.value)
      .then(resp => {
        console.log(resp);
        if (resp.code == 10000) {
          //弹出消息
          ElMessage.success(resp.msg);
          //隐藏对话框
          addDialogShow.value = false;
          //清空对话框中的数据
          adminAdd.value = {
            username: null,
            password: '',
            salt: null,
            phone: null,
            email:null,
            realname: null,
            createTime: null,
            status: null
          };
          //刷新表格数据
          selectByPage(pageInfo.value.pageNum);
        } else {
          //弹出消息
          ElMessage.error(resp.msg);
        }
      });
}

function chgStatus(id, status) {
  const admin = {
    id,
    status
  }
  adminApi.changeInfo(admin)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          selectByPage(pageInfo.value.pageNum);
        } else {
          ElMessage.error(resp.msg);
        }
      })
}
function ResetPassword(id, salt) {
  const admin = {
    id,
    salt
  }
  adminApi.changeInfo(admin)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          selectByPage(pageInfo.value.pageNum);
        } else {
          ElMessage.error(resp.msg);
        }
      })
}


//分页查询
function selectByPage(pageNum) {
  adminApi.selectByPage(condition.value, pageNum, 5)
      .then(resp => {
        pageInfo.value = resp.data;
      })
}

selectByPage(1);
</script>

<style scoped>

</style>