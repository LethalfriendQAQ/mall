<template>
  <!--用户个人中心（查看所有订单、查看收藏、个人信息、修改密码、地址管理、账户充值）-->
  <el-container>
    <el-aside>
      <div class="title">个人中心</div>
      <ul class="menuList">
        <li
            v-for="(item, index) in menuItems"
            :key="index"
            :class="{ active: selectedIndex === index }"
            @click="selectMenu(index)"
        >
          <RouterLink :to="item.route">{{ item.label }}</RouterLink>
        </li>
      </ul>
    </el-aside>
    <el-main>
      <RouterView />
    </el-main>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';

// 菜单项数据
const menuItems = [
  { label: '个人信息', route: '/user/info' },
  { label: '我的订单', route: '/user/orderList' },
  { label: '我的收藏', route: '/user/collectList' },
  { label: '修改密码', route: '/user/password' },
  { label: '地址管理', route: '/user/addrList' },
  { label: '账户充值', route: '/user/recharge' },
];

// 选中的菜单项索引
const selectedIndex = ref(null);

// 获取当前路由
const route = useRoute();

// 在组件挂载后设置选中的菜单项索引
onMounted(() => {
  menuItems.forEach((item, index) => {
    if (item.route === route.path) {
      selectedIndex.value = index;
    }
  });
});

// 选择菜单项
const selectMenu = (index) => {
  selectedIndex.value = index;
};
</script>

<style scoped>
.el-aside {
  padding: 20px;
  width: 150px;
}
.title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
}
.menuList li {
  margin: 10px 0px;
}
.menuList li a {
  font-size: 15px;
  color: #333333;
  text-decoration: none; /* 去掉下划线 */
}
.menuList li.active a {
  font-weight: bold; /* 加粗 */
  color: var(--theme-color);
}
</style>
