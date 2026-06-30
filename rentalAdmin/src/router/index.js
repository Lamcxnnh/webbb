import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/login', name: 'Login', component: () => import('../views/Login.vue') },
  { path: '/', name: 'Dashboard', component: () => import('../views/Dashboard.vue') },
  { path: '/apartments', name: 'Apartments', component: () => import('../views/ApartmentList.vue') },
  { path: '/rooms', name: 'Rooms', component: () => import('../views/RoomList.vue') },
  { path: '/basic-data', name: 'BasicData', component: () => import('../views/BasicData.vue') },
  { path: '/appointments', name: 'Appointments', component: () => import('../views/Appointments.vue') },
  { path: '/leases', name: 'Leases', component: () => import('../views/Leases.vue') },
  { path: '/users', name: 'Users', component: () => import('../views/Users.vue') }
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('adminToken')
  if (to.path !== '/login' && !token) next('/login')
  else next()
})

export default router
