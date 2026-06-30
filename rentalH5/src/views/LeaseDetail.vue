<template>
  <div>
    <div class="header">
      <button class="back" @click="$router.back()">&larr;</button>
      <h1>租约详情</h1>
      <span></span>
    </div>

    <div class="page" v-if="lease">
      <div class="card">
        <div style="text-align:center;margin-bottom:16px">
          <span style="font-size:24px;font-weight:700;color:#e74c3c">¥{{ lease.rentAmount }}</span>
          <span style="color:#999"> 元/月</span>
        </div>
        <div style="display:flex;justify-content:center">
          <span :style="{background:statusBg(lease.status),color:'#fff',padding:'4px 16px',borderRadius:'12px',fontSize:'14px'}">
            {{ statusText(lease.status) }}
          </span>
        </div>
      </div>

      <div class="card">
        <div class="info-row"><span class="label">租约编号</span><span>#{{ lease.id }}</span></div>
        <div class="info-row"><span class="label">租赁周期</span><span>{{ lease.startDate }} 至 {{ lease.endDate }}</span></div>
        <div class="info-row"><span class="label">月租金</span><span class="value">¥{{ lease.rentAmount }}</span></div>
        <div class="info-row"><span class="label">押金</span><span class="value">¥{{ lease.depositAmount || 0 }}</span></div>
        <div class="info-row"><span class="label">签订时间</span><span>{{ lease.signTime ? new Date(lease.signTime).toLocaleDateString() : '-' }}</span></div>
      </div>
    </div>
    <div v-else class="page" style="text-align:center;padding:80px 0">加载中...</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getLeaseDetail } from '../api'

const route = useRoute()
const lease = ref(null)

onMounted(async () => {
  try {
    const res = await getLeaseDetail(route.params.id)
    lease.value = res.data
  } catch (e) {
    alert('加载失败')
  }
})

function statusText(s) {
  const m = { signed: '已签订', active: '执行中', expired: '已到期', terminated: '已解约' }
  return m[s] || s
}

function statusBg(s) {
  const m = { signed: '#f39c12', active: '#2ecc71', expired: '#999', terminated: '#e74c3c' }
  return m[s] || '#999'
}
</script>

<style scoped>
.info-row { display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid #f0f0f0; font-size: 14px; }
.info-row:last-child { border-bottom: none; }
.info-row .label { color: #999; }
.info-row .value { font-weight: 600; color: #e74c3c; }
</style>
