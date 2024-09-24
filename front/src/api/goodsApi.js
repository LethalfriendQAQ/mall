import service from "@/api/index.js";
import {createRouterMatcher as Promise} from "vue-router";
const goodsApi = {
    //添加
    insert(goods) {
        return service.post("/goods", goods);
    },
    //删除
    delete(id) {
        return service.delete(`/goods/${id}`);
    },
    //修改

    //根据id查询
    selectById(id) {
        return service.get(`/goods/${id}`);
    },
    selectByPage(condition, pageNum, pageSize) {
        return service.get("/goods", {
            params: condition
        });
    }
};
export default goodsApi;