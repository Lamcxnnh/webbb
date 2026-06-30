import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/room/:id',
    name: 'RoomDetail',
    component: () => import('../views/RoomDetail.vue')
  },
  {
    path: '/apartment/:id',
    name: 'ApartmentDetail',
    component: () => import('../views/ApartmentDetail.vue')
  },
  {
    path: '/appointments',
    name: 'MyAppointments',
    component: () => import('../views/MyAppointments.vue')
  },
  {
    path: '/leases',
    name: 'MyLeases',
    component: () => import('../views/MyLeases.vue')
  },
  {
    path: '/lease/:id',
    name: 'LeaseDetail',
    component: () => import('../views/LeaseDetail.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫：未登录跳转登录页
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
