<template>
  <div>
    <div class="header"><h1>我的租约</h1></div>
    <div class="page" style="padding-top:8px">
      <div v-if="list.length === 0" style="text-align:center;padding:60px 0;color:#999">暂无租约</div>
      <router-link v-for="l in list" :key="l.id" :to="'/lease/' + l.id" class="card" style="display:block">
        <div style="display:flex;justify-content:space-between;align-items:center">
          <div>
            <div style="font-weight:600">租约 #{{ l.id }}</div>
            <div style="font-size:13px;color:#999;margin-top:4px">
              {{ l.startDate }} ~ {{ l.endDate }}
            </div>
            <div style="font-size:16px;color:#e74c3c;font-weight:600;margin-top:4px">
              ¥{{ l.rentAmount }} <span style="font-size:13px;font-weight:400">/月</span>
            </div>
          </div>
          <span :style="{color: leaseStatusColor(l.status), fontWeight:600}">{{ leaseStatusText(l.status) }}</span>
        </div>
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyLeases } from '../api'

const list = ref([])

onMounted(async () => {
  try {
    const res = await getMyLeases()
    list.value = res.data.records || []
  } catch (e) { console.error(e) }
})

function leaseStatusText(s) {
  const m = { signed: '已签订', active: '执行中', expired: '已到期', terminated: '已解约' }
  return m[s] || s
}

function leaseStatusColor(s) {
  const m = { signed: '#f39c12', active: '#2ecc71', expired: '#999', terminated: '#e74c3c' }
  return m[s] || '#999'
}
</script>
