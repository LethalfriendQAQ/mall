import service from "@/api/index.js";

const goodsApi = {
    // 添加
    insert(goods) {
        return service.post("/goods", goods);
    },
    // 删除
    delete(id) {
        return service.delete(`/goods/${id}`);
    },
    // 修改
    update(goods) {
        return service.put("/goods", goods);
    },
    // 根据ID查询
    selectById(id) {
        return service.get(`/goods/${id}`);
    },
    // 分页查询
    selectByPage(condition, pageNum, pageSize) {
        condition.pageNum = pageNum;
        condition.pageSize = pageSize;
        return service.get("/goods", { params: condition });
    },
    //分页搜索 - 注意：分类id既可以是父分类的id也可以是子分类的id
    selectByPage1(condition, pageNum, pageSize) {
        condition.pageNum = pageNum;
        condition.pageSize = pageSize;
        return service.get("/goods/search", { params: condition });
    }
};

export default goodsApi;
