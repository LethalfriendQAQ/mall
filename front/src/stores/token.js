import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useTokenStore = defineStore('token', () => {
  const token = ref(null)
  //返回token
  const tokenStr = computed(() =>  token.value)
  //更新token
  function updateToken(tk) {
    token.value = tk;
  }

  //重置token
  function $reset() {
    token.value = null;
  }

  return { token, tokenStr, updateToken, $reset }
}, {
  persist :{
    key: 'token',
    storage: sessionStorage,
    paths: ['token']
  }
})
