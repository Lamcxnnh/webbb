<template>
  <div class="tab-bar">
    <router-link to="/" class="tab-item" :class="{ active: route.path === '/' }">
      <span class="icon">&#127968;</span>
      <span>找房</span>
    </router-link>
    <router-link to="/appointments" class="tab-item" :class="{ active: route.path === '/appointments' }">
      <span class="icon">&#128197;</span>
      <span>预约</span>
    </router-link>
    <router-link to="/leases" class="tab-item" :class="{ active: route.path === '/leases' }">
      <span class="icon">&#128221;</span>
      <span>租约</span>
    </router-link>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
const route = useRoute()
</script>
