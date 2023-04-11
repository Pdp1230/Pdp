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
      {
        path: "form/:id",
        component: () => import("pages/Form.vue"),
        name: "Form",
        props:true
      },
      {
        path: "submit",
        component: () => import("pages/Submit.vue"),
        name: "Submit",
      },
    ],
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: "/*",
    component: () => import("pages/ErrorNotFound.vue"),
    name : "Error404"
  },
];

export default routes;