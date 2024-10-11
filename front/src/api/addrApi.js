import service from "@/api/index.js";

const addrApi = {
    // 添加
    insert(addr) {
        return service.post("/addr", addr);
    },
    delete(id) {
        return service.delete(`/addr/${id}`);
    },
    update(addr) {
        return service.put("/addr", addr);
    },
    selectById(id) {
        return service.get(`/addr/${id}`)
    },
    selectByUser() {
        return service.get("/addr")
    }
}
export default addrApi;