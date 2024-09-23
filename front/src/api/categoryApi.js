import service from "@/api/index.js";

const categoryApi = {
    insert(category) {
        return service.post("/category", category);
    },
    delete(id) {
        return service.delete(`/category/${id}`);
    },
    update(category) {
        return service.put("/category", category);
    },
    selectByPage(condition, pageNum, pageSize) {
        condition.pageNum = pageNum;
        condition.pageSize = pageSize;
        return service.get("/category/search", {
            params: condition
        })
    },
    selectById(id) {
        return service.get(`/category/${id}`);
    },
    selectAllParent() {
        return service.get("/category/allParent");
    },
    hello() {
        return service.get("/user/hello");
    }
};

export default categoryApi;