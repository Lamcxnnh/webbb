<template>
  <div class="container">
    <router-view />
    <TabBar v-if="showTab" />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import TabBar from './components/TabBar.vue'

const route = useRoute()
// 不在底部导航显示的页面
const hideTabPages = ['Login', 'RoomDetail', 'ApartmentDetail', 'LeaseDetail']
const showTab = computed(() => !hideTabPages.includes(route.name))
</script>
