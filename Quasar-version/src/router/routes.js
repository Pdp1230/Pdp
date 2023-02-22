const routes = [
  {
    path: "/",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      { path: "", component: () => import("pages/Home.vue"), name: "Home" },
      {
        path: "login",
        component: () => import("pages/Login.vue"),
        name: "Login",
      },
      {
        path: "reset-password",
        component: () => import("pages/ResetPassword.vue"),
        name: "ResetPassword",
      },
    ],
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: "/*",
    component: () => import("pages/ErrorNotFound.vue"),
  },
];

export default routes;