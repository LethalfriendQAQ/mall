import service from "@/api/index.js";

const adminApi = {
    // 添加
    insert(admin) {
        return service.post("/admin", admin);
    },
    // 删除
    delete(id) {
        return service.delete(`/admin/${id}`);
    },
    // 修改
    update(admin) {
        return service.put("/admin", admin);
    },
    // 根据ID查询
    selectById(id) {
        return service.get(`/admin/${id}`);
    },
    // 分页查询
    selectByPage(condition, pageNum, pageSize) {
        condition.pageNum = pageNum;
        condition.pageSize = pageSize;
        return service.get("/admin", { params: condition });
    }
}

export default adminApi;