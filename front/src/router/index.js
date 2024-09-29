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
import UserLoginView from "@/views/user/LoginView.vue";
import UserHomeView from "@/views/user/HomeView.vue";



const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'user_home',
            component: UserHomeView
        }, {
            path: '/admin/login',
            name: 'admin_login',
            component: LoginView
        }, {
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
        }, {
            path: '/user/login',
            name: 'user_login',
            component: UserLoginView
        }
    ]
})
//导航守卫
router.beforeEach((to, from) => {
    //to - 跳转到的位置
    //from - 原位置
    //TODO 将来还会对其他页面放行 例如：前台首页，搜索页，详情页，用户登录注册页
    if(to.path == '/admin/login' || to.path == '/user/login' || to.path == '/') {
        return true;
    } else {
        const tokenStore = useTokenStore();

        //判断store中是否有token
        if(tokenStore.token) {
            return true;
        } else {
            //判断跳转到管理员的登录页还是用户的登录页
            let currentPath = router.currentRoute.value.path;
            if (to.path.startsWith("/admin")) {
                return "/admin/login";
            } else {
                return "/user/login";
            }
        }
    }
});

export default router
