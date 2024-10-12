import service from "@/api/index.js";

const orderApi = {
    // 根据ID查询
    selectById(id) {
        return service.get(`/order/${id}`);
    },
    // 分页查询
    selectByPage(condition, pageNum, pageSize) {
        condition.pageNum = pageNum;
        condition.pageSize = pageSize;
        return service.get("/order", { params: condition });
    },
    selectByUserId() {
        return service.get("/order/selectByUserId")
    }
}

export default orderApi;