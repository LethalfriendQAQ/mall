import service from "@/api/index.js";
import qs from 'qs'

const userApi = {
    getInfo(userInfo) {
        return service.get("/user/info", qs.stringify(userInfo))
    },
    login(userInfo) {
        return service.post("/user/login", qs.stringify(userInfo))
    },
    reg(userInfo) {
      return service.post("/user/reg", qs.stringify(userInfo))
    },
    captcha() {
        return service.get("/user/captcha")
    },
    // 添加
    insert(user) {
        return service.post("/user", user);
    },
    // 删除
    delete(id) {
        return service.delete(`/user/${id}`);
    },
    // 修改
    update(user) {
        return service.put("/user", user);
    },
    // 根据ID查询
    selectById(id) {
        return service.get(`/user/${id}`);
    },
    // 分页查询
    selectByPage(condition, pageNum, pageSize) {
        condition.pageNum = pageNum;
        condition.pageSize = pageSize;
        return service.get("/user", { params: condition });
    },
    changePassword(chgPwdObj) {
        return service.put("/user/changePassword", qs.stringify(chgPwdObj))
    },
    changePayPassword(chgPayPwdObj) {
        return service.put("/user/changePayPassword", qs.stringify(chgPayPwdObj))
    },
    setPayPwd(setPayPwd) {
        return service.put("/user/setPayPwd", qs.stringify(setPayPwd))
    },
    setRealname(setRealName) {
        return service.put("/user/setRealname", qs.stringify(setRealName))
    },
    recharge(recharge) {
        return service.put("/user/recharge", qs.stringify(recharge))
    },
    cancelAccount(cancelAccount) {
        return service.put("/user/cancelAccount", qs.stringify(cancelAccount))
    }
}
export default userApi;