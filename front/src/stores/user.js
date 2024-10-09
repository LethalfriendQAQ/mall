import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', () => {
  const user = ref(null)
  //返回user
  const userInfo = computed(() =>  user.value)
  //更新token
  function updateUser(u) {
    user.value = u;
  }

  //重置token
  function $reset() {
    user.value = null;
  }

  return { user, userInfo, updateUser, $reset }
}, {
  persist :{
    key: 'user',
    storage: sessionStorage
  }
})
