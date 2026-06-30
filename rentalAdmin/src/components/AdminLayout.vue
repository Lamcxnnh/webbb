<template>
  <div class="admin-layout">
    <div class="sidebar">
      <div class="logo">拾光公寓 · 后台</div>
      <router-link to="/" class="nav-item" :class="{ active: route.path === '/' }">工作台</router-link>
      <router-link to="/apartments" class="nav-item" :class="{ active: route.path.startsWith('/apartment') }">公寓管理</router-link>
      <router-link to="/rooms" class="nav-item" :class="{ active: route.path.startsWith('/room') }">房间管理</router-link>
      <router-link to="/basic-data" class="nav-item" :class="{ active: route.path === '/basic-data' }">基础数据</router-link>
      <router-link to="/appointments" class="nav-item" :class="{ active: route.path === '/appointments' }">看房预约</router-link>
      <router-link to="/leases" class="nav-item" :class="{ active: route.path === '/leases' }">租约管理</router-link>
      <router-link to="/users" class="nav-item" :class="{ active: route.path === '/users' }">用户管理</router-link>
      <div class="nav-item" @click="logout" style="margin-top:40px;color:rgba(255,255,255,.4)">退出登录</div>
    </div>
    <div class="main">
      <slot />
    </div>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
const route = useRoute()
const router = useRouter()
function logout() {
  localStorage.removeItem('adminToken')
  router.push('/login')
}
</script>
