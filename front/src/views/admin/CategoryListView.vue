<template>
  <el-col :span="24">
    <el-card>
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item>
          <el-button type="primary" @click="">添加</el-button>
        </el-form-item>
        <el-form-item style="float: right;">
          <el-input v-model="condition.name" placeholder="请输入要搜索的名称" @input="selectByPage(1);" />
        </el-form-item>
        <el-form-item style="float: right;">
          <el-select
              v-model="condition.status"
              placeholder="请选择是否推荐"
              style="width: 150px"
              clearable
              :value-on-clear="null"
              @change="selectByPage(1)"
          >
            <el-option label="上架中" value="1"/>
            <el-option label="已下架" value="0"/>
          </el-select>
        </el-form-item>
        <el-form-item style="float: right;">
          <el-select
              v-model="condition.parentId"
              clearable
              placeholder="请选择父分类"
              style="width: 150px"
              :value-on-clear="null"
              @change="selectByPage(1)"
          >
            <el-option label="没有父分类" value="0"/>
            <el-option v-for="(parent, index) in allParent" :key="index" :label="parent.name" :value="parent.id"/>
          </el-select>
        </el-form-item>
      </el-form>
        <el-table :data="pageInfo.list" border style="width: 100%">
          <el-table-column prop="id" label="ID"/>
          <el-table-column prop="name" label="名称"/>
          <el-table-column label="图片">
            <template #default="scope">
              <el-avatar shape="square" :size="size" :src="SERVER_ADDR + '/category/pic/' + scope.row.pic" :title="SERVER_ADDR + '/category/pic/' + scope.row.pic"/>
            </template>
          </el-table-column>
          <el-table-column prop="dscp" label="描述"/>
          <el-table-column label="父分类">
            <template #default="scope">
              <el-text v-if="scope.row.parent">{{scope.row.parent.name}}</el-text>
            </template>
          </el-table-column>
          <el-table-column label="是否推荐">
            <template #default="scope">
              <el-tag type="success" v-if="scope.row.recom == 1">推荐</el-tag>
              <el-tag type="warning" v-else>不推荐</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="是否上架">
            <template #default="scope">
              <el-tag type="success" v-if="scope.row.status == 1">上架中</el-tag>
              <el-tag type="warning" v-else>已下架</el-tag>
            </template>
          </el-table-column>

          <el-table-column label="操作">
            <template #default="scope">
              <el-button type="primary" size="small" @click="" round>修改</el-button>
              <el-popconfirm title="你确定要删除该分类吗？" confirm-button-text="确认" cancel-button-text="取消"
                             width="200px" @confirm="">
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
import categoryApi from "@/api/categoryApi.js";
import {ref} from "vue";

//服务器路径
const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);

//搜索条件
const condition = ref({
  name: null,
  parentId: null,
  status: null
});

//分页信息
const pageInfo = ref({
  pages: 0,
  pageSize: 0,
  pageNum: 0
});
const allParent = ref([]);
const categotyAdd = ref({
  name: null,
  dscp: null,
  pic: null,
  parentId: 0,
  recom: 1,
  status: 1
})

//分页查询
function selectByPage(pageNum) {
  categoryApi.selectByPage(condition.value, pageNum, 5)
      .then(resp => {
        pageInfo.value = resp.data;
      })
}

//获取所有的父分类
function selectAllParent() {
  categoryApi.selectAllParent()
      .then(resp => {
        allParent.value = resp.data;
      })
}
//是否显示添加对话框

selectAllParent();
selectByPage();
</script>

<style scoped>

</style>