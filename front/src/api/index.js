import axios from "axios";

//创建Axios的对象
const service = axios.create({
    baseURL: import.meta.env.VITE_SERVER_ADDR
});

//响应拦截器
service.interceptors.response(resp => {
    return resp.data;
}, error => {

});

export default service;