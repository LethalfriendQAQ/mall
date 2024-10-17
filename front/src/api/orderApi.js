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
    selectByUser(pageNum, pageSize) {
        return service.get("/order/user", {
            params: {
                pageNum,
                pageSize
            }
        })
    },
    pay(orderVo) {
        return service.post("/order/pay", orderVo)
    },
    // 根据ID查询
    adminSelectById(id) {
        return service.get(`/order/adminSelectById/${id}`);
    }
}

export default orderApi;