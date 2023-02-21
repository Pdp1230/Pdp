const routes = [
  {
    path: "/",
    component: () => import("layouts/MainLayout.vue"),
    children: [
      { path: "", component: () => import("pages/Home.vue"), name: "Home" },
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
