<template>
Goods
</template>

<script setup>
import { useRoute } from "vue-router";
import goodsApi from "@/api/goodsApi.js";
import {ref} from "vue";

const route = useRoute();

const goods = ref({});
//获取路径传递的参数
function selectById() {
  let id = route.query.id;
  goodsApi.selectById(id)
      .then(resp => {
        goods.value = resp.data;
      });
}

selectById();
</script>

<style scoped>

</style>