<template>
  <div>
    <div class="header"><h1>我的预约</h1></div>
    <div class="page" style="padding-top:8px">
      <div v-if="list.length === 0" style="text-align:center;padding:60px 0;color:#999">暂无预约</div>
      <div v-for="a in list" :key="a.id" class="card">
        <div style="display:flex;justify-content:space-between;align-items:center">
          <div>
            <div style="font-weight:600">{{ a.name }}</div>
            <div style="font-size:13px;color:#999;margin-top:4px">{{ a.phone }}</div>
            <div style="font-size:13px;color:#999;margin-top:2px">{{ formatTime(a.appointmentTime) }}</div>
          </div>
          <span :style="{color: statusColor(a.status), fontWeight:600}">{{ statusText(a.status) }}</span>
        </div>
        <div v-if="a.remark" style="margin-top:8px;font-size:13px;color:#999">备注: {{ a.remark }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyAppointments } from '../api'

const list = ref([])

onMounted(async () => {
  try {
    const res = await getMyAppointments()
    list.value = res.data.records || []
  } catch (e) { console.error(e) }
})

function formatTime(t) {
  return t ? new Date(t).toLocaleString('zh-CN') : ''
}

function statusText(s) {
  const m = { pending: '待确认', confirmed: '已确认', cancelled: '已取消', completed: '已完成' }
  return m[s] || s
}

function statusColor(s) {
  const m = { pending: '#f39c12', confirmed: '#2ecc71', cancelled: '#999', completed: '#4e6ef2' }
  return m[s] || '#999'
}

const statusMap = { pending: '待确认', confirmed: '已确认', cancelled: '已取消', completed: '已完成' }
</script>
