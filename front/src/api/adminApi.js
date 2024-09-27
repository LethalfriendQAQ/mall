import service from "@/api/index.js";
import qs from 'qs'
const adminApi = {
    // 添加
    insert(admin) {
        return service.post("/admin", admin);
    },
    // 删除
    delete(id) {
        return service.delete(`/admin/${id}`);
    },
    // 修改
    changeInfo(admin) {
        return service.put("/admin/changeInfo", admin);
    },
    // 根据ID查询
    selectById(id) {
        return service.get(`/admin/${id}`);
    },
    // 分页查询
    selectByPage(condition, pageNum, pageSize) {
        condition.pageNum = pageNum;
        condition.pageSize = pageSize;
        return service.get("/admin", { params: condition });
    },
    captcha() {
        return service.get("/admin/captcha");
    },
    login(admin) {
        return service.post("/admin/login", qs.stringify(admin))
    },
    //获取已登录用户的信息
    getInfo() {
        return service.get("/admin/info")
    },
    changePassword(chgPwdObj) {
        return service.put("/admin/changePassword", qs.stringify(chgPwdObj))
    }
}

export default adminApi;