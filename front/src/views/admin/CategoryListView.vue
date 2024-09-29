<template>
  <el-col :span="24">
    <el-card>
      <el-form :inline="true" class="demo-form-inline">
        <el-form-item>
          <el-button type="primary" @click="addDialogShow = true">添加</el-button>
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
              <el-avatar shape="square" :src="SERVER_ADDR + '/category/pic/' + scope.row.pic" :title="SERVER_ADDR + '/category/pic/' + scope.row.pic"/>
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
              <el-switch
                  v-model="scope.row.recom"
                  inline-prompt
                  :inactive-value="0"
                  :active-value="1"
                  inactive-text="不推荐"
                  active-text="推荐"
                  @change="chgRecom(scope.row.id, scope.row.recom, scope.row.parentId)"
              />
            </template>
          </el-table-column>

          <el-table-column label="是否上架">
            <template #default="scope">
              <el-switch
                  v-model="scope.row.status"
                  inline-prompt
                  :inactive-value="0"
                  :active-value="1"
                  inactive-text="已下架"
                  active-text="上架中"
                  @change="chgStatus(scope.row.id, scope.row.status, scope.row.parentId)"
              />
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

  <!-- 添加对话框开始 -->
  <el-dialog v-model="addDialogShow" title="添加分类" width="500">
    <el-form>
      <el-form-item label="名称" label-width="20%">
        <el-input v-model="categoryAdd.name" placeholder="请输入名称" autocomplete="off" />
      </el-form-item>
      <el-form-item label="描述" label-width="20%">
        <el-input v-model="categoryAdd.dscp" placeholder="请输入描述" autocomplete="off" />
      </el-form-item>
      <el-form-item label="父分类" label-width="20%">
        <el-select v-model="categoryAdd.parentId" placeholder="请选择父分类" clearable :empty-values="[0]" :value-on-clear="0" style="width: 300px;">
          <el-option v-for="(category, index) in allParent" :key="index"
                     :label="category.name" :value="category.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="是否推荐" label-width="20%">
        <el-radio-group v-model="categoryAdd.recom">
          <el-radio label="不推荐" :value="0" size="large" />
          <el-radio label="推荐" :value="1" size="large" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否上架" label-width="20%">
        <el-radio-group v-model="categoryAdd.status">
          <el-radio label="已下架" :value="0" size="large" />
          <el-radio label="上架中" :value="1" size="large" />
        </el-radio-group>
      </el-form-item>
      <!--</el-form-item>-->
      <el-form-item label="图片" label-width="20%">
        <el-upload class="avatar-uploader" :action="SERVER_ADDR + '/category/upload'" name="pic" :headers="headers"
                   :show-file-list="false" :before-upload="beforePicUpload" :on-success="PicAddUploadSuccess">
          <img v-if="categoryAdd.pic" :src="SERVER_ADDR + '/category/pic/' + categoryAdd.pic" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon">
            <Plus />
          </el-icon>
        </el-upload>
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


  <!-- 修改对话框开始 -->
  <el-dialog v-model="updateDialogShow" title="修改分类" width="500">
    <el-form>
      <el-form-item label="名称" label-width="20%">
        <el-input v-model="categoryUpdate.name" placeholder="请输入名称" autocomplete="off" />
      </el-form-item>
      <el-form-item label="描述" label-width="20%">
        <el-input v-model="categoryUpdate.dscp" placeholder="请输入描述" autocomplete="off" />
      </el-form-item>
      <el-form-item label="父分类" label-width="20%">
        <el-select v-model="categoryUpdate.parentId" placeholder="请选择父分类" clearable :empty-values="[0]" :value-on-clear="0" style="width: 300px;">
          <el-option v-for="(category, index) in allParent" :key="index"
                     :label="category.name" :value="category.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="是否推荐" label-width="20%">
        <el-radio-group v-model="categoryUpdate.recom">
          <el-radio label="不推荐" :value="0" size="large" />
          <el-radio label="推荐" :value="1" size="large" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否上架" label-width="20%">
        <el-radio-group v-model="categoryUpdate.status">
          <el-radio label="已下架" :value="0" size="large" />
          <el-radio label="上架中" :value="1" size="large" />
        </el-radio-group>
      </el-form-item>
      <!--</el-form-item>-->
      <el-form-item label="图片" label-width="20%">
        <el-upload class="avatar-uploader" :action="SERVER_ADDR + '/category/upload'" name="pic" :headers="headers"
                   :show-file-list="false" :before-upload="beforePicUpload" :on-success="PicUpdateUploadSuccess">
          <img v-if="categoryUpdate.pic" :src="SERVER_ADDR + '/category/pic/' + categoryUpdate.pic" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon">
            <Plus />
          </el-icon>
        </el-upload>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="updateDialogShow = false">取消</el-button>
        <el-button type="primary" @click="update">确认</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 修改对话框结束 -->

</template>

<script setup>
import categoryApi from "@/api/categoryApi.js";
import {computed, ref} from "vue";
import {ElMessage} from "element-plus";
import {useTokenStore} from "@/stores/token.js";

const tokenStore = useTokenStore();
const headers = computed(() => {
  const token = tokenStore.tokenStr;
  return {
    token
  }
})

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
//添加分类的信息
const categoryAdd = ref({
  name: null,
  dscp: null,
  pic: null,
  parentId: 0,
  recom: 1,
  status: 1
})
//是否显示添加对话框
const addDialogShow = ref(false);

//修改分类的信息
const categoryUpdate = ref({
  id: null,
  name: null,
  dscp: null,
  pic: null,
  parentId: 0,
  recom: 1,
  status: 1
});
//是否显示修改对话框
const updateDialogShow = ref(false);



//分页查询
function selectByPage(pageNum) {
  categoryApi.selectByPage(condition.value, pageNum, 5)
      .then(resp => {
        pageInfo.value = resp.data;
      })
}

function chgRecom(id, recom, parentId) {
  const category = {
    id,
    recom,
    parentId
  }
  categoryApi.update(category)
      .then(resp => {
        if (resp.code == 10000) {
          //弹出消息
          ElMessage.success(resp.msg);
          //刷新表格数据
          selectByPage(pageInfo.value.pageNum);
        } else {
          //弹出消息
          ElMessage.error(resp.msg);
        }
      });
}

function chgStatus(id, status, parentId) {
  const category = {
    id,
    status,
    parentId
  }
  categoryApi.update(category)
      .then(resp => {
        if (resp.code == 10000) {
          //弹出消息
          ElMessage.success(resp.msg);
          //刷新表格数据
          selectByPage(pageInfo.value.pageNum);
        } else {
          //弹出消息
          ElMessage.error(resp.msg);
        }
      });
}

//获取所有的父分类
function selectAllParent() {
  categoryApi.selectAllParent()
      .then(resp => {
        allParent.value = resp.data;
      })
}
//添加成功上传之前的回调
function beforePicUpload(rawFile) {
  if (rawFile.type !== 'image/jpeg') {
    ElMessage.error('图片仅支持jpg格式');
    return false
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('图片不能超过2M');
    return false
  }
  return true
}


//添加成功上传之后的回调
function PicAddUploadSuccess(resp) {
  if (resp.code == 10000) {
    ElMessage.success(resp.msg);
    categoryAdd.value.pic = resp.data;
  } else {
    ElMessage.error(resp.msg);
  }
}

//修改成功上传之后的回调
function PicUpdateUploadSuccess(resp) {
  if (resp.code == 10000) {
    ElMessage.success(resp.msg);
    categoryUpdate.value.pic = resp.data;
  } else {
    ElMessage.error(resp.msg);
  }
}

//定义方法完成分类添加
function insert() {
  categoryApi.insert(categoryAdd.value)
      .then(resp => {
        if (resp.code == 10000) {
          //弹出消息
          ElMessage.success(resp.msg);
          //隐藏对话框
          addDialogShow.value = false;
          //清空对话框中的数据
          categoryAdd.value = {
            name: null,
            dscp: null,
            pic: null,
            parentId: 0,
            recom: 1,
            status: 1
          };
          //imgUrl清空
          //imageUrl.value = '';
          //刷新表格数据
          selectByPage(pageInfo.value.pageNum);
        } else {
          //弹出消息
          ElMessage.error(resp.msg);
        }
      });
}

//删除分类
function deleteCategory(id) {
  categoryApi.delete(id)
      .then(resp => {
        //判断-弹出消息-刷新表格
        if (resp.code == 10000) {
          //弹出消息
          ElMessage.success(resp.msg);
          selectByPage(pageInfo.value.pageNum);
        } else {
          //弹出消息
          ElMessage.error(resp.msg);
        }
      })
}

//根据id查询被修改分类的信息
function selectById(id) {
  categoryApi.selectById(id)
      .then(resp => {
          categoryUpdate.value = resp.data;
          updateDialogShow.value = true;
       });
}

//定义方法完成分类修改
function update() {
  categoryApi.update(categoryUpdate.value)
      .then(resp => {
        if (resp.code == 10000) {
          //弹出消息
          ElMessage.success(resp.msg);
          //隐藏对话框
          updateDialogShow.value = false;
          //刷新表格数据
          selectByPage(pageInfo.value.pageNum);
        } else {
          //弹出消息
          ElMessage.error(resp.msg);
        }
      });
}

selectAllParent();
selectByPage();
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