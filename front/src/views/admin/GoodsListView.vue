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
                <el-image style="width: 30px; height: 30px" :src="SERVER_ADDR + '/goods/pic/' + scope.row.picList[0].url" :title="SERVER_ADDR + '/goods/pic/' + scope.row.picList[0].url"/>
              </template>
                <el-image v-for="(pic, index) in scope.row.picList" :key="index" style="width: 40px; height: 40px;margin-right: 5px" :src="SERVER_ADDR + '/goods/pic/' + pic.url" />
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150px">
          <template #default="scope">
            <el-button type="primary" size="small" @click="" round>修改</el-button>
            <el-popconfirm title="你确定要删除该商品吗？" confirm-button-text="确认" cancel-button-text="取消"
                           width="200px" @confirm="deleteGoods(scope.row.id)">
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
  <el-dialog v-model="addDialogShow" title="添加商品" width="500">
    <el-form>
      <el-form-item label="分类" label-width="20%">
        <el-cascader
            v-model="goodsAdd.categoryId"
            :options="allParent"
            :props="props"
            placeholder="请选择分类"
            clearable
            :value-on-clear="null"
        />
      </el-form-item>
      <el-form-item label="名称" label-width="20%">
        <el-input v-model="goodsAdd.name" placeholder="请输入名称" autocomplete="off" />
      </el-form-item>
      <el-form-item label="描述" label-width="20%">
        <el-input v-model="goodsAdd.dscp" placeholder="请输入描述" autocomplete="off" />
      </el-form-item>
      <el-form-item label="售价" label-width="20%">
        <el-input v-model="goodsAdd.price" placeholder="请输入售价" autocomplete="off" />
      </el-form-item>
      <el-form-item label="标价" label-width="20%">
        <el-input v-model="goodsAdd.markPrice" placeholder="请输入标价" autocomplete="off" />
      </el-form-item>
      <el-form-item label="进价" label-width="20%">
        <el-input v-model="goodsAdd.purchasePrice" placeholder="请输入进价" autocomplete="off" />
      </el-form-item>
      <el-form-item label="颜色" label-width="20%">
        <el-input v-model="goodsAdd.color" placeholder="请输入颜色" autocomplete="off" />
      </el-form-item>
      <el-form-item label="版本" label-width="20%">
        <el-input v-model="goodsAdd.version" placeholder="请输入版本" autocomplete="off" />
      </el-form-item>
      <el-form-item label="数量" label-width="20%">
        <el-input v-model="goodsAdd.count" placeholder="请输入数量" autocomplete="off" />
      </el-form-item>
      <el-form-item label="是否推荐" label-width="20%">
        <el-radio-group v-model="goodsAdd.recom">
          <el-radio label="不推荐" :value="0" size="large" />
          <el-radio label="推荐" :value="1" size="large" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否上架" label-width="20%">
        <el-radio-group v-model="goodsAdd.status">
          <el-radio label="已下架" :value="0" size="large" />
          <el-radio label="上架中" :value="1" size="large" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="图片" label-width="20%">
        <el-upload
            v-model:file-list="goodsAdd.picList"
            :action="SERVER_ADDR + '/category/upload'"
            name="pic"
            list-type="picture-card"
            :before-upload="beforePicUpload"
            :on-success="PicAddUploadSuccess"
        >
          <el-icon class="avatar-uploader-icon">
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
  <el-dialog v-model="updateDialogShow" title="添加商品" width="500">
    <el-form>
      <el-form-item label="分类" label-width="20%">
        <el-cascader
            v-model="goodsAdd.categoryId"
            :options="allParent"
            :props="props"
            placeholder="请选择分类"
            clearable
            :value-on-clear="null"
        />
      </el-form-item>
      <el-form-item label="名称" label-width="20%">
        <el-input v-model="goodsAdd.name" placeholder="请输入名称" autocomplete="off" />
      </el-form-item>
      <el-form-item label="描述" label-width="20%">
        <el-input v-model="goodsAdd.dscp" placeholder="请输入描述" autocomplete="off" />
      </el-form-item>
      <el-form-item label="售价" label-width="20%">
        <el-input v-model="goodsAdd.price" placeholder="请输入售价" autocomplete="off" />
      </el-form-item>
      <el-form-item label="标价" label-width="20%">
        <el-input v-model="goodsAdd.markPrice" placeholder="请输入标价" autocomplete="off" />
      </el-form-item>
      <el-form-item label="进价" label-width="20%">
        <el-input v-model="goodsAdd.purchasePrice" placeholder="请输入进价" autocomplete="off" />
      </el-form-item>
      <el-form-item label="颜色" label-width="20%">
        <el-input v-model="goodsAdd.color" placeholder="请输入颜色" autocomplete="off" />
      </el-form-item>
      <el-form-item label="版本" label-width="20%">
        <el-input v-model="goodsAdd.version" placeholder="请输入版本" autocomplete="off" />
      </el-form-item>
      <el-form-item label="数量" label-width="20%">
        <el-input v-model="goodsAdd.count" placeholder="请输入数量" autocomplete="off" />
      </el-form-item>
      <el-form-item label="是否推荐" label-width="20%">
        <el-radio-group v-model="goodsAdd.recom">
          <el-radio label="不推荐" :value="0" size="large" />
          <el-radio label="推荐" :value="1" size="large" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否上架" label-width="20%">
        <el-radio-group v-model="goodsAdd.status">
          <el-radio label="已下架" :value="0" size="large" />
          <el-radio label="上架中" :value="1" size="large" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="图片" label-width="20%">
        <el-upload
            v-model:file-list="goodsAdd.picList"
            list-type="picture-card"
            :action="SERVER_ADDR + '/category/upload'"
            name="pic"
            :before-upload="beforePicUpload" :on-success="PicAddUploadSuccess"
        >
          <el-icon class="avatar-uploader-icon">
            <Plus />
          </el-icon>
        </el-upload>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="updateDialogShow = false">取消</el-button>
        <el-button type="primary" @click="insert">确认</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 修改对话框结束 -->

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
  //给级联选择器绑定的数据和官方规定的key不一致时需要设置
  label: "name", //页面上显示的信息
  value: "id", //值
  children: "childList", //子级
  emitPath: false
}
//是否显示添加的对话框
const addDialogShow = ref(false);
//是否显示修改的对话框
const updateDialogShow = ref(false);
//被添加商品的信息
const goodsAdd = ref({
  name: '',
  dscp: '',
  price: null,
  markPrice: null,
  purchasePrice: null,
  color: null,
  version: null,
  count: null,
  recom: null,
  categoryId: null,
  status: null,
  picList: []
});

const goodsUpdate = ref({
  name: '',
  dscp: '',
  price: null,
  markPrice: null,
  purchasePrice: null,
  color: null,
  version: null,
  count: null,
  recom: null,
  categoryId: null,
  status: null,
  picList: []
});

function selectById(id) {
  goodsApi.selectById(id)
      .then(resp => {
        goodsUpdate.value = resp.data;
        //处理图片 realName -> url
        for (let i = 0; i < goodsUpdate.value.picList.length; i++) {
          goodsUpdate.value.picList[i].url = `${SERVER_ADDR.value}/goods/pic/${goodsUpdate.value.picList[i].url}`;
        }
        updateDialogShow.value = true;
      });
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
function PicAddUploadSuccess(resp, uploadFile) {
  if (resp.code == 10000) {
    ElMessage.success(resp.msg);
    uploadFile.url = `${SERVER_ADDR.value}/goods/pic/${resp.data}`;
    uploadFile.realName = resp.data;
  } else {
    ElMessage.error(resp.msg);
  }
}


function insert() {
  //处理图片 realName -> url
  for (let i = 0; i < goodsAdd.value.picList.length; i++) {
    goodsAdd.value.picList[i].url = goodsAdd.value.picList[i].realName;
  }
  goodsApi.insert(goodsAdd.value)
      .then(resp => {
        if (resp.code == 10000) {
          //弹出消息
          ElMessage.success(resp.msg);
          //隐藏对话框
          addDialogShow.value = false;
          //清空对话框中的数据
          goodsAdd.value = {
            name: '',
            dscp: '',
            price: null,
            markPrice: null,
            purchasePrice: null,
            color: null,
            version: null,
            count: null,
            recom: null,
            categoryId: null,
            status: null,
            picList: []
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


//根据id删除商品
function deleteGoods(id) {
  goodsApi.delete(id)
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
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>