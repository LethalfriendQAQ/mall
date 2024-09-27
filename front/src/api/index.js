import axios from 'axios'
import { ElMessage } from 'element-plus';
import router from '@/router';
import { useTokenStore } from "@/stores/token.js";

//创建Axios的对象
const service = axios.create({
    baseURL: import.meta.env.VITE_SERVER_ADDR
});

//Axios的请求的拦截器
service.interceptors.request.use(function (config) {
    const tokenStore = useTokenStore();
    // 如果不是登录请求就要在请求头中添加token
    if (tokenStore.tokenStr) {
        //将Store中的token放在请求头当中
        config.headers.token = tokenStore.tokenStr;
    }
    return config;
}, error => {

});



//axios的响应拦截器
service.interceptors.response.use(resp => {
    //获取续期的jwt
    const token = resp.headers.token;

    //检查token是否存在
    if (token) {
        //将续期的jwt放在store中
        const tokenStore = useTokenStore();
        tokenStore.updateToken(token);
    }

    return resp.data;
}, error => {
    if (error.response && error.response.status === 403) {  // 改为检查 `error.response.status`
        ElMessage.error({
            message: '令牌错误，请重新登录',
            type: 'success',
            duration: 1200,
            onClose: () => {
                //将store中的token设置为初值
                const tokenStore = useTokenStore();
                tokenStore.$reset();
                let currentPath = router.currentRoute.value.path;
                if (currentPath.startsWith("/admin")) {
                    return "/admin/login";
                } else {
                    return "/user/login";
                }
            }
        });
    }
    return Promise.reject(error);  // 保证将错误返回
});

export default service;