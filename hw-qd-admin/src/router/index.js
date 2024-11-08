import { createRouter, createWebHistory } from "vue-router";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      redirect: "/home/self",
    },
    {
      path: "/login",
      name: "login",
      component: () => import("../views/Login.vue"),
    },
    {
      path: "/home",
      name: "Home",
      component: () => import("../views/Home.vue"),
      children: [
        {
          path: "books",
          name: "Books",
          component: () => import("../views/Books.vue"),
        },
        {
          path: "carts",
          name: "Carts",
          component: () => import("../views/Carts.vue"),
        },
        {
          path: "self",
          name: "Self",
          component: () => import("../views/Self.vue"),
        },
        {
          path: "orders",
          name: "Orders",
          component: () => import("../views/Orders.vue"),
        },
      ],
    },
    {
      path: "/:catchAll(.*)",
      component: () => import("../views/Not.vue"),
    },
  ],
});
router.beforeEach((to, from, next) => {
  const isAuthenticated = !!localStorage.getItem("token"); // 检查 JWT 是否存在

  if (to.path === "/login" || isAuthenticated) {
    // 如果是登录页面或者已登录，继续导航
    next();
  } else {
    // 否则重定向到登录页面
    next("/login");
  }

  // 处理根路径
  if (to.path === "/") {
    next(isAuthenticated ? "/home" : "/login");
  }
});
export default router;
