import service from "@/api/index.js";

const orderApi = {
    insert(orderVo) {
        return service.post("/order", orderVo);
    },
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
    },
    pay(orderVo) {
        return service.post("/order/pay", orderVo)
    }
}

export default orderApi;