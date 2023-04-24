const routes = [
  {
    path: "/",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      { path: "home", component: () => import("pages/Home.vue"), name: "Home" },
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
      {
        path: "form/:id",
        component: () => import("pages/Form.vue"),
        name: "Form",
        props:true
      },
      {
        path: "prototype",
        component: () => import("src/pages/Prototype.vue"),
        name: "Prototype",
      },
    ],
  },
  // Always leave this as last one,
  // but you can also remove it
  {
    path: "/:catchAll(.*)",
    component: () => import("pages/ErrorNotFound.vue"),
    name : "Error404"
  },
];

export default routes;