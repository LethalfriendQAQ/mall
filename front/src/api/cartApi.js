import service from "@/api/index.js";

const cartApi = {
    // 添加
    insert(goodsId) {
        return service.post(`/cart/${goodsId}`);
    }
}
export default cartApi;