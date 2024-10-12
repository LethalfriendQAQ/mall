import service from "@/api/index.js";

const cartApi = {
    // 添加
    insert(goodsId) {
        return service.post(`/cart/${goodsId}`);
    },
    delete(id) {
        return service.delete(`/cart/${id}`);
    },
    deleteByIds(ids) {
        return service.delete("/cart",  {
            data: {
                ids
            }
        });
    },
    update(cart) {
        return service.put("/cart", cart);
    },
    selectCartList() {
        return service.get("/cart");
    },
    selectById(id) {
        return service.get(`/cart/${id}`)
    },
    selectByIds(cartIds) {
        //  /cart/ids?cartId=1&cartId=2&cartId=3
        let params = "?cartIds=";
        for (let i = 0; i < cartIds.length; i++) {
            params += cartIds[i];
            if (i != cartIds.length - 1) {
                params += '&cartIds='
            }
        }
        return service.get(`/cart/ids${params}`);
    }
}
export default cartApi;