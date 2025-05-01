import { createRouter, createWebHistory } from 'vue-router'

//Define the routes for the application
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),

  routes: [
    {
      //route for the register view
      path: '/register',
      name: 'register',
      component: () => import('../views/RegisterView.vue'),
    },
    {
      //route for the login view
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
    },
    {
      //route for the home view
      path: '/',
      name: 'home',
      component: () => import('../views/HomeView.vue'),
    },
    {
      //route for the private home view
      path: '/report',
      name: 'report',
      component: () => import('../views/PrivateHomeView.vue'),
      meta: {
        requiresAuth: true,
      },
    },
  ],
})

export default router
