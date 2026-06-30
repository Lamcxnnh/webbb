<template>
  <AdminLayout>
    <div class="main-header">
      <h2>工作台</h2>
      <span style="color:#999">{{ adminInfo?.name || '' }}</span>
    </div>
    <div style="display:grid;grid-template-columns:repeat(4,1fr);gap:16px;margin-bottom:16px">
      <div class="stat-card" v-for="s in stats" :key="s.label">
        <div class="stat-num">{{ s.count }}</div>
        <div class="stat-label">{{ s.label }}</div>
      </div>
    </div>
    <p style="color:#999;text-align:center;padding:40px">欢迎使用拾光公寓后台管理系统</p>
  </AdminLayout>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import AdminLayout from '../components/AdminLayout.vue'
import { getAdminInfo, getApartmentPage, getRoomPage, getAppointmentPage, getLeasePage } from '../api'

const adminInfo = ref(null)
const stats = reactive([
  { label: '公寓总数', count: 0 },
  { label: '房间总数', count: 0 },
  { label: '待确认预约', count: 0 },
  { label: '执行中租约', count: 0 }
])

onMounted(async () => {
  try {
    const [info, apt, room, appt, lease] = await Promise.all([
      getAdminInfo(),
      getApartmentPage({ page: 1, size: 1 }),
      getRoomPage({ page: 1, size: 1 }),
      getAppointmentPage({ page: 1, size: 1, status: 'pending' }),
      getLeasePage({ page: 1, size: 1, status: 'active' })
    ])
    adminInfo.value = info.data
    stats[0].count = apt.data.total || 0
    stats[1].count = room.data.total || 0
    stats[2].count = appt.data.total || 0
    stats[3].count = lease.data.total || 0
  } catch (e) { console.error(e) }
})
</script>

<style scoped>
.stat-card { background: #fff; border-radius: 8px; padding: 24px; text-align: center; }
.stat-num { font-size: 32px; font-weight: 700; color: #1890ff; }
.stat-label { font-size: 14px; color: #999; margin-top: 8px; }
</style>
