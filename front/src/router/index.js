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
import UserIndexView from "@/views/user/IndexView.vue"
import UserRegView from "@/views/user/RegView.vue"
import UserSearchView from "@/views/user/SearchView.vue"
import GoodsView from "@/views/user/GoodsView.vue";
import CartView from "@/views/user/CartView.vue";
import CreateOrderView from "@/views/user/CreateOrderView.vue";
import CenterView from "@/views/user/CenterView.vue";
import AddrListView from "@/views/user/center/AddrListView.vue";
import CollectListView from "@/views/user/center/CollectListView.vue";
import UserOrderListView from "@/views/user/center/OrderListView.vue";
import InfoView from "@/views/user/center/InfoView.vue";
import PasswordView from "@/views/user/center/PasswordView.vue";
import RechargeView from "@/views/user/center/RechargeView.vue";
import PayView from "@/views/user/PayView.vue";



const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'user_home',
            component: UserHomeView,
            redirect: '/user/index',
            children: [
                {
                    path: '/user/login',
                    name: 'user_login',
                    component: UserLoginView
                }, {
                    path: '/user/index',
                    name: 'user_index',
                    component: UserIndexView
                }, {
                    path: '/user/search/:categoryId?',
                    name: 'user_search',
                    props: true,
                    component: UserSearchView
                }, {
                    path: '/user/reg',
                    name: 'user_reg',
                    component: UserRegView
                }, {
                    path: '/user/goods',
                    name: 'user_goods',
                    component: GoodsView
                }, {
                    path: '/user/cart',
                    name: 'user_cart',
                    component: CartView
                }, {
                    path: '/user/createOrder',
                    name: 'user_createOrder',
                    component: CreateOrderView
                }, {
                    path: '/user/pay',
                    name: 'user_pay',
                    component: PayView
                }, {
                    path: '/user/center',
                    name: 'user_center',
                    component: CenterView,
                    redirect: '/user/info',
                    children: [
                        {
                            path: '/user/addrList',
                            name: 'user_addrList',
                            component: AddrListView
                        }, {
                            path: '/user/collectList',
                            name: 'user_collectList',
                            component: CollectListView
                        }, {
                            path: '/user/info',
                            name: 'user_info',
                            component: InfoView
                        }, {
                            path: '/user/orderList',
                            name: 'user_orderList',
                            component: UserOrderListView
                        }, {
                            path: '/user/password',
                            name: 'user_password',
                            component: PasswordView
                        }, {
                            path: '/user/recharge',
                            name: 'user_recharge',
                            component: RechargeView
                        }
                    ]
                }
            ]
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
        },
    ]
})
//导航守卫
router.beforeEach((to, from) => {
    //to - 跳转到的位置
    //from - 原位置
    //TODO 将来还会对其他页面放行 例如：前台首页，搜索页，详情页，用户登录注册页
    if(to.path == '/admin/login' ||
        to.path == '/' ||
        to.path == '/user/login' ||
        to.path == '/user/index' ||
        to.path == '/user/reg' ||
        to.path == '/user/goods' ||
        to.path.startsWith('/user/search')
    ) {
        return true;
    } else {
        const tokenStore = useTokenStore();

        //判断store中是否有token
        if(tokenStore.token) {
            return true;
        } else {
            //判断跳转到管理员的登录页还是用户的登录页
            if (to.path.startsWith("/admin")) {
                return "/admin/login";
            } else {
                return "/user/login";
            }
        }
    }
});

export default router
