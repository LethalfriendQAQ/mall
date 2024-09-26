import {createRouter, createWebHistory} from 'vue-router'
import IndexView from "@/views/admin/IndexView.vue";
import HomeView from "@/views/admin/HomeView.vue";
import AdminListView from "@/views/admin/AdminListView.vue";
import CategoryListView from "@/views/admin/CategoryListView.vue";
import GoodsListView from "@/views/admin/GoodsListView.vue";
import OrderListView from "@/views/admin/OrderListView.vue";
import UserListView from "@/views/admin/UserListView.vue";
import LoginView from "@/views/admin/LoginView.vue";
import {useTokenStore} from "@/stores/token.js";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/admin/login',
            name: 'admin_login',
            component: LoginView
        },
        {
            path: '/admin/home',
            name: 'admin_home',
            component: HomeView,
            redirect: "/admin/index",
            children: [
                {
                    path: '/admin/index',
                    name: 'admin_index',
                    component: IndexView
                }, {
                    path: '/admin/list',
                    name: 'admin_list',
                    component: AdminListView
                }, {
                    path: '/admin/category',
                    name: 'admin_category',
                    component: CategoryListView
                }, {
                    path: '/admin/goods',
                    name: 'admin_goods',
                    component: GoodsListView
                }, {
                    path: '/admin/order',
                    name: 'admin_order',
                    component: OrderListView
                }, {
                    path: '/admin/user',
                    name: 'admin_user',
                    component: UserListView
                }
            ]
        },
    ]
})
//导航守卫
router.beforeEach((to, from) => {
    //to - 要访问的位置
    //from - 其实位置
    if(to.path == '/admin/login') {
        return true;
    } else {
        const tokenStore = useTokenStore();

        //判断store中是否有token
        if(!tokenStore.token) {
            return "/admin/login";
        } else {
            return true;
        }
    }
});

export default router
