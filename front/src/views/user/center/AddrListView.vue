<template>
  <el-row style="margin-bottom: 20px">
    <el-col :span="12" style="font-size: 18px; font-weight: bold">我的地址</el-col>
    <el-col :span="12" style="text-align: right"><el-button type="primary" @click="addDialogShow = true">添加地址</el-button> </el-col>
  </el-row>
  <div>
    <ul class="addrList">
      <li v-for="(addr, index) in addrList" :key="index" :class="{defaultAddr: addr.status == 1}">
        <div class="area" :title="`${addr.province} - ${addr.city} - ${addr.district} - ${addr.street}`">
          {{addr.province}} - {{addr.city}} - {{addr.district}} - {{addr.street}}
        </div>
        <div class="address">{{addr.address}}</div>
        <div class="contect">{{addr.contact}}</div>
        <div class="phone">{{addr.phone}}</div>
        <div class="option">
          <span v-if="addr.status == 1" class="defaultInfo">默认地址</span>
          <el-button size="small" v-else @click="setDefault(addr.id)">设为默认</el-button>
          <el-button size="small" @click="showUpdateDialog(addr.id)">编辑</el-button>
          <el-popconfirm title="你确定要删除该地址吗？" confirm-button-text="确认" cancel-button-text="取消"
                         width="200px" @confirm="deleteAddr(addr.id)">
            <template #reference>
              <el-button size="small" >删除</el-button>
            </template>
          </el-popconfirm>
        </div>
      </li>
    </ul>
  </div>

  <!-- 添加对话框开始 -->
  <el-dialog v-model="addDialogShow" title="添加地址" width="800">
    <el-form>
      <el-form-item label="省/市/区县/街道" label-width="15%">
        <el-select v-model="addr.province" placeholder="请选择省" style="width: 150px; margin-right: 10px" @change="selectProvince">
          <el-option
              v-for="(p, index) in pcas"
              :key="index"
              :value="p.name"
          />
        </el-select>
        <el-select v-model="addr.city" placeholder="请选择市" style="width: 150px; margin-right: 10px" @change="selectCity">
          <el-option
              v-for="(c, index) in province.children"
              :key="index"
              :value="c.name"
          />
        </el-select>
        <el-select v-model="addr.district" placeholder="请选择区县" style="width: 150px; margin-right: 10px" @change="selectDistrict">
          <el-option
              v-for="(d, index) in city.children"
              :key="index"
              :value="d.name"
          />
        </el-select>
        <el-select v-model="addr.street" placeholder="请选择乡、镇、街道" style="width: 150px; margin-right: 10px">
          <el-option
              v-for="(s, index) in district.children"
              :key="index"
              :value="s.name"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="详细地址" label-width="15%">
        <el-input v-model="addr.address" placeholder="请输入详细地址" style="width: 400px" />
      </el-form-item>
      <el-form-item label="联系人" label-width="15%">
        <el-input v-model="addr.contact" placeholder="请输入详细联系人" style="width: 400px" />
      </el-form-item>
      <el-form-item label="电话" label-width="15%">
        <el-input v-model="addr.phone" placeholder="请输入详细联系电话" style="width: 400px" />
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
  <el-dialog v-model="updateDialogShow" title="添加地址" width="800">
    <el-form>
      <el-form-item label="省/市/区县/街道" label-width="15%">
        <el-select v-model="addr.province" placeholder="请选择省" style="width: 150px; margin-right: 10px" @change="selectProvince">
          <el-option
              v-for="(p, index) in pcas"
              :key="index"
              :value="p.name"
          />
        </el-select>
        <el-select v-model="addr.city" placeholder="请选择市" style="width: 150px; margin-right: 10px" @change="selectCity">
          <el-option
              v-for="(c, index) in province.children"
              :key="index"
              :value="c.name"
          />
        </el-select>
        <el-select v-model="addr.district" placeholder="请选择区县" style="width: 150px; margin-right: 10px" @change="selectDistrict">
          <el-option
              v-for="(d, index) in city.children"
              :key="index"
              :value="d.name"
          />
        </el-select>
        <el-select v-model="addr.street" placeholder="请选择乡、镇、街道" style="width: 150px; margin-right: 10px">
          <el-option
              v-for="(s, index) in district.children"
              :key="index"
              :value="s.name"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="详细地址" label-width="15%">
        <el-input v-model="addr.address" placeholder="请输入详细地址" style="width: 400px" />
      </el-form-item>
      <el-form-item label="联系人" label-width="15%">
        <el-input v-model="addr.contact" placeholder="请输入详细联系人" style="width: 400px" />
      </el-form-item>
      <el-form-item label="电话" label-width="15%">
        <el-input v-model="addr.phone" placeholder="请输入详细联系电话" style="width: 400px" />
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
import addrApi from "@/api/addrApi.js";
import {ref} from "vue";
import {ElMessage} from "element-plus";
import pcas from "@/stores/pcas-code.json";

//是否让添加的对话框显示
const addDialogShow = ref(false);
//是否让修改对话框显示
const updateDialogShow = ref(false);
//保存新增和修改地址的对象
const addr = ref({
  id: null,
  contact: '',
  phone: '',
  province: '',
  city: '',
  district: '',
  street: '',
  address: '',
  userId: '',
  status: null,
})

//选中的省
const province = ref({});
//选中的市
const city = ref({});
//选中的区县
const district = ref({});

//当前用户的所有地址信息
const addrList = ref([]);


function setDefault(id) {
  let addr = {
    id,
    status: 1
  }
  addrApi.update(addr)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          //查询当前用户的所有地址
          getAddrList();
        } else {
          ElMessage.error(resp.msg);
        }
      })
}


//删除地址
function deleteAddr(id) {
  addrApi.delete(id)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          //查询当前用户的所有地址
          getAddrList();
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

//省被选中之后调用
function selectProvince(value) {
  city.value = {};
  district.value = {};
  addr.value.city = '';
  addr.value.district = '';
  addr.value.street = '';
  //province和被选中的省的对象绑定
  pcas.forEach(p => {
    if (p.name == value) {
      province.value = p;
    }
  })
}

//市被选中之后的调用
function selectCity(value) {
  district.value = {};
  addr.value.district = '';
  addr.value.street = '';
  province.value.children.forEach(c => {
    if (c.name == value) {
      city.value = c;
    }
  })
}

//区县被选中之后的调用
function selectDistrict(value) {
  addr.value.street = '';
  city.value.children.forEach(d => {
    if (d.name == value) {
      district.value = d;
    }
  })
}


//获取当前用户的所有的地址
function getAddrList() {
  addrApi.selectByUser()
      .then(resp => {
        if (resp.code == 10000) {
          addrList.value = resp.data;
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

//重置信息
function resetInfo() {
  addr.value = {
    id: null,
    contact: '',
    phone: '',
    province: '',
    city: '',
    district: '',
    street: '',
    address: '',
    userId: '',
    status: null,
  };
  //选中的省
  province.value = {};
  //选中的市
  city.value = {};
  //选中的区县
  district.value = {};
}

//添加
function insert() {
  addrApi.insert(addr.value)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          //重置信息
          resetInfo();
          //隐藏添加的对话框
          addDialogShow.value = false;
          //查询当前用户的所有地址
          getAddrList();
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

//查询地址信息并显示对话框
function showUpdateDialog(id) {
  //根据id查询地址信息
  addrApi.selectById(id)
      .then(resp => {
        if (resp.code == 10000) {
          addr.value = resp.data;
          //设置provice city district这三个对象的值
          x:for (let p of pcas) {
            if (p.name == addr.value.province) {
              province.value = p;
              for (let c of province.value.children) {
                if (c.name == addr.value.city) {
                  city.value = c;
                  for (let d of city.value.children) {
                    if (d.name == addr.value.district) {
                      district.value = d;
                      break x;
                    }
                  }
                }
              }
            }
          }
          updateDialogShow.value = true;
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

//修改
function update() {
  addrApi.update(addr.value)
      .then(resp => {
        if (resp.code == 10000) {
          ElMessage.success(resp.msg);
          //重置信息
          resetInfo();
          //隐藏添加的对话框
          updateDialogShow.value = false;
          //查询当前用户的所有地址
          getAddrList();
        } else {
          ElMessage.error(resp.msg);
        }
      })
}

getAddrList();
</script>

<style scoped>
  .addrList li {
    margin: 0px 20px 20px 0px;
    padding: 20px;
    float: left;
    width: 280px;
    border: 1px solid #EEEEEE;
    border-radius: 5px;
    background-color: #FFFFFF;
  }
  .addrList li:hover {
    box-shadow: 0px 0px 20px #8c939d;
  }
  .addrList li:nth-child(3n) {
    margin-right: 0px;
  }
  .addrList li div {
    height: 30px;
    line-height: 30px;
  }
  .addrList li .area {
    font-size: 18px;
    font-weight: bold;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .addrList li .address {
    font-size: 14px;
    color: #AAAAAA;
  }
  .addrList li .contect {
    color: var(--theme-color);
    font-weight: bold;
  }
  .addrList li .phone {
    font-size: 14px;
  }
  .addrList li .option {
    text-align: right;
  }
  .addrList .defaultInfo {
    font-weight: bold;
    color: var(--theme-color);
    margin-right: 10px;
  }
  .addrList .defaultAddr {
    border: 1px solid var(--theme-color);
  }
  .addrList .defaultAddr:hover {
    box-shadow: 0px 0px 20px var(--theme-color);
  }
</style>