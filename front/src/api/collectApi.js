import service from "@/api/index.js";
import qs from 'qs'

const collectApi = {
    // 收藏
    insert(goodsId) {
        return service.post(`/collect/${goodsId}`);
    },
    // 取消收藏
    delete(id) {
        return service.delete(`/collect/${id}`);
    },
    // 根据商品id和用户id查询
    selectByGoodsIdAndUserId(goodsId) {
        return service.get(`/collect/${goodsId}`);
    },
    // 分页查询
    selectByPage(pageNum, pageSize) {
        return service.get("/collect", {
            params: {
                pageNum,
                pageSize
            }
        })
    }
}
export default collectApi;