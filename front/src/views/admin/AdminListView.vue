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
        <el-table-column prop="password" label="密码"/>
        <el-table-column prop="salt" label="盐"/>
        <el-table-column prop="phone" label="电话号码"/>
        <el-table-column prop="email" label="邮箱"/>
        <el-table-column prop="realname" label="真实姓名"/>
        <el-table-column prop="createTime" label="创建时间"/>
        <el-table-column label="当前状态">
          <template #default="scope">
            <el-tag type="warning" v-if="scope.row.status == 1">禁用</el-tag>
            <el-tag type="success " v-else>正常</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
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
</template>

<script setup>
import adminApi from "@/api/adminApi.js";
import {ref} from "vue";
import {useTokenStore} from "@/stores/token.js";

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
//分页查询
function selectByPage(pageNum) {
  adminApi.selectByPage(condition.value, pageNum, 5)
      .then(resp => {
        console.log(resp);
        pageInfo.value = resp.data;
      })
}

selectByPage();
</script>

<style scoped>

</style>