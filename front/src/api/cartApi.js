import service from "@/api/index.js";

const cartApi = {
    // 添加
    insert(goodsId) {
        return service.post(`/cart/${goodsId}`);
    },
    delete(id) {
        return service.delete(`/cart/${id}`);
    },
    update(cart) {
        return service.put("/cart", cart);
    },
    selectCartList() {
        return service.get("/cart");
    },
    selectById(id) {
        return service.get(`/cart/${id}`)
    }
}
export default cartApi;