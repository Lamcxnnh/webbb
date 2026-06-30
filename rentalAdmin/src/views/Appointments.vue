<template>
  <AdminLayout>
    <div class="main-header"><h2>看房预约</h2></div>

    <div class="table-wrap" style="margin-bottom:16px">
      <div class="search-bar">
        <input v-model="search.name" placeholder="联系人" />
        <input v-model="search.phone" placeholder="电话" />
        <select v-model="search.status" @change="fetchList">
          <option value="">全部状态</option><option value="pending">待确认</option><option value="confirmed">已确认</option><option value="completed">已完成</option><option value="cancelled">已取消</option>
        </select>
        <button class="btn btn-primary btn-sm" @click="fetchList">搜索</button>
      </div>
    </div>

    <div class="table-wrap">
      <table>
        <thead><tr><th>ID</th><th>联系人</th><th>电话</th><th>房间ID</th><th>公寓ID</th><th>预约时间</th><th>状态</th><th>备注</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="a in list" :key="a.id">
            <td>{{ a.id }}</td><td>{{ a.name }}</td><td>{{ a.phone }}</td><td>{{ a.roomId }}</td><td>{{ a.apartmentId }}</td>
            <td>{{ a.appointmentTime }}</td>
            <td><span :class="statusClass(a.status)">{{ statusText(a.status) }}</span></td>
            <td style="max-width:100px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">{{ a.remark }}</td>
            <td>
              <button v-if="a.status==='pending'" class="btn btn-success btn-sm" @click="updateStatus(a.id,'confirmed')">确认</button>
              <button v-if="a.status==='confirmed'" class="btn btn-primary btn-sm" @click="updateStatus(a.id,'completed')">完成</button>
              <button v-if="a.status==='pending'" class="btn btn-danger btn-sm" @click="cancelAppt(a.id)">取消</button>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="pagination">
        <button :disabled="page<=1" @click="page--;fetchList()">上一页</button>
        <span style="padding:6px 12px">{{ page }} / {{ totalPages || 1 }}</span>
        <button :disabled="page>=totalPages" @click="page++;fetchList()">下一页</button>
      </div>
    </div>
  </AdminLayout>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import AdminLayout from '../components/AdminLayout.vue'
import { getAppointmentPage, updateAppointmentStatus } from '../api'

const list = ref([]); const page = ref(1); const totalPages = ref(1)
const search = reactive({ name: '', phone: '', status: '' })

onMounted(fetchList)

async function fetchList() {
  const params = { page: page.value, size: 10 }
  if (search.name) params.name = search.name
  if (search.phone) params.phone = search.phone
  if (search.status) params.status = search.status
  try { const r = await getAppointmentPage(params); list.value = r.data.records||[]; totalPages.value = r.data.pages||1 } catch (e) { console.error(e) }
}

async function updateStatus(id, status) { await updateAppointmentStatus(id, status); fetchList() }
async function cancelAppt(id) { const reason = prompt('取消原因:'); if (reason !== null) { await updateAppointmentStatus(id, 'cancelled', reason); fetchList() } }

function statusText(s) { return { pending:'待确认',confirmed:'已确认',completed:'已完成',cancelled:'已取消' }[s] || s }
function statusClass(s) { return { pending:'tag tag-orange',confirmed:'tag tag-green',completed:'tag tag-blue',cancelled:'tag tag-gray' }[s] || 'tag' }
</script>
