import service from "@/api/index.js";

const userApi = {
    // 添加
    insert(user) {
        return service.post("/user", user);
    },
    // 删除
    delete(id) {
        return service.delete(`/user/${id}`);
    },
    // 修改
    update(user) {
        return service.put("/user", user);
    },
    // 根据ID查询
    selectById(id) {
        return service.get(`/user/${id}`);
    },
    // 分页查询
    selectByPage(condition, pageNum, pageSize) {
        condition.pageNum = pageNum;
        condition.pageSize = pageSize;
        return service.get("/user", { params: condition });
    }
}
export default userApi;