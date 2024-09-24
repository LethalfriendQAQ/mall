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
          <el-cascader
              v-model="condition.categoryId"
              :options="allParent"
              :props="props"
              placeholder="请选择分类"
              clearable
              :value-on-clear="null"
              @change="selectByPage(1)"
          />
        </el-form-item>

      </el-form>
      <el-table :data="pageInfo.list" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="50px"/>
        <el-table-column prop="name" label="名称"/>
        <el-table-column prop="dscp" label="描述"/>
        <el-table-column prop="price" label="售价"/>
        <el-table-column prop="markPrice" label="标价"/>
        <el-table-column prop="color" label="颜色"/>
        <el-table-column prop="version" label="版本"/>
        <el-table-column prop="count" label="数量"/>
        <el-table-column label="是否推荐">
          <template #default="scope">
            <el-tag type="success" v-if="scope.row.recom == 1">推荐</el-tag>
            <el-tag type="warning" v-else>不推荐</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="分类">
          <template #default="scope">
            <el-text >{{scope.row.category.name}}</el-text>
          </template>
        </el-table-column>
        <el-table-column prop="score" label="评分"/>
        <el-table-column label="是否上架">
          <template #default="scope">
            <el-tag type="success" v-if="scope.row.status == 1">上架中</el-tag>
            <el-tag type="warning" v-else>已下架</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="图片">
          <template #default="scope">
            <el-popover placement="right"  trigger="hover" v-if="scope.row.picList.length > 0">
              <template #reference>
                <el-image :key="index" style="width: 30px; height: 30px" :src="SERVER_ADDR + '/goods/pic/' + scope.row.picList[0].url" :fit="fit"  :title="SERVER_ADDR + '/goods/pic/' + scope.row.picList[0].url"/>
              </template>
                <el-image v-for="(pic, index) in scope.row.picList" :key="index" style="width: 40px; height: 40px;margin-right: 5px" :src="SERVER_ADDR + '/goods/pic/' + pic.url" :fit="fit" />
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150px">
          <template #default="scope">
            <el-button type="primary" size="small" @click="" round>修改</el-button>
            <el-popconfirm title="你确定要删除该商品吗？" confirm-button-text="确认" cancel-button-text="取消"
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
import goodsApi from "@/api/goodsApi.js";
import {ref} from "vue";
import {ElMessage} from "element-plus";
import categoryApi from "@/api/categoryApi.js";
//服务器路径
const SERVER_ADDR = ref(import.meta.env.VITE_SERVER_ADDR);

//分页信息
const pageInfo = ref({
  pages: 0,
  pageSize: 0,
  pageNum: 0
});

//搜索条件
const condition = ref({
  name: null,
  categoryId: null,
  status: null
})
//父分类
const allParent = ref([]);

//级联选择器的配置选项
const props = {
  label: "name",
  value: "id",
  children: "childList",
  emitPath: false
}
//获取所有的父分类
function selectAllParent() {
  categoryApi.selectAllParent()
      .then(resp => {
        allParent.value = resp.data;
      })
}
//分页查询
function selectByPage(pageNum) {
  goodsApi.selectByPage(condition.value, pageNum, 10)
      .then(resp => {
        pageInfo.value = resp.data;
      })
}

selectAllParent();
selectByPage(1);
</script>

<style scoped>
.avatar-uploader,
.avatar {
  width: 178px;
  height: 178px;
  display: block;
  border: 1px dotted #dcdfe6;
  border-radius: 5px;
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>