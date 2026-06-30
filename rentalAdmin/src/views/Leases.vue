<template>
  <AdminLayout>
    <div class="main-header"><h2>租约管理</h2><button class="btn btn-primary" @click="openForm">新增租约</button></div>

    <div class="table-wrap" style="margin-bottom:16px">
      <div class="search-bar">
        <select v-model="search.status" @change="fetchList">
          <option value="">全部状态</option><option value="signed">已签订</option><option value="active">执行中</option><option value="expired">已到期</option><option value="terminated">已解约</option>
        </select>
        <button class="btn btn-primary btn-sm" @click="fetchList">搜索</button>
      </div>
    </div>

    <div class="table-wrap">
      <table>
        <thead><tr><th>ID</th><th>用户</th><th>房间</th><th>公寓</th><th>租期</th><th>支付方式</th><th>月租金</th><th>起止日期</th><th>状态</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="l in list" :key="l.id">
            <td>{{ l.id }}</td>
            <td>{{ userMap[l.userId] || '#'+l.userId }}</td>
            <td>{{ roomMap[l.roomId] || '#'+l.roomId }}</td>
            <td>{{ apartmentMap[l.apartmentId] || '#'+l.apartmentId }}</td>
            <td>{{ termMap[l.leaseTermId] || '#'+l.leaseTermId }}</td>
            <td>{{ payMap[l.paymentTypeId] || '#'+l.paymentTypeId }}</td>
            <td style="font-weight:600;color:#e74c3c">¥{{ l.rentAmount }}</td>
            <td>{{ l.startDate }} ~ {{ l.endDate }}</td>
            <td><span :class="leaseStatusClass(l.status)">{{ leaseStatusText(l.status) }}</span></td>
            <td>
              <button v-if="l.status==='signed'" class="btn btn-success btn-sm" @click="updateLeaseStatus(l.id,'active')">开始执行</button>
              <button v-if="l.status==='active'" class="btn btn-default btn-sm" @click="updateLeaseStatus(l.id,'terminated')">解约</button>
              <button class="btn btn-danger btn-sm" @click="doDelete(l.id)">删除</button>
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

    <!-- 新增租约弹窗 -->
    <div v-if="showForm" class="modal-overlay" @click.self="showForm=false">
      <div class="modal" style="width:560px">
        <h3>新增租约</h3>

        <div class="form-item"><label>选择用户</label>
          <select v-model="leaseForm.userId">
            <option value="">-- 请选择 --</option>
            <option v-for="u in userOptions" :key="u.id" :value="u.id">{{ u.phone }} {{ u.nickname ? '('+u.nickname+')' : '' }}</option>
          </select>
        </div>

        <div class="form-item"><label>选择房间</label>
          <select v-model="leaseForm.roomId" @change="onRoomChange">
            <option value="">-- 请选择 --</option>
            <option v-for="r in roomOptions" :key="r.id" :value="r.id">{{ r.name }} (¥{{ r.price }}/月, {{ r.apartmentName }})</option>
          </select>
        </div>

        <div class="form-item"><label>所属公寓</label>
          <input :value="leaseForm.apartmentName" disabled style="background:#f5f5f5;color:#999" />
        </div>

        <div style="display:flex;gap:12px">
          <div class="form-item" style="flex:1"><label>租期</label>
            <select v-model="leaseForm.leaseTermId" @change="onTermChange">
              <option value="">-- 请选择 --</option>
              <option v-for="t in termOptions" :key="t.id" :value="t.id">{{ t.monthCount }}个月</option>
            </select>
          </div>
          <div class="form-item" style="flex:1"><label>支付方式</label>
            <select v-model="leaseForm.paymentTypeId">
              <option value="">-- 请选择 --</option>
              <option v-for="p in payOptions" :key="p.id" :value="p.id">{{ p.name }}</option>
            </select>
          </div>
        </div>

        <div style="display:flex;gap:12px">
          <div class="form-item" style="flex:1"><label>开始日期</label>
            <input v-model="leaseForm.startDate" type="date" @change="onTermChange" />
          </div>
          <div class="form-item" style="flex:1"><label>结束日期</label>
            <input :value="leaseForm.endDate" disabled style="background:#f5f5f5;color:#999" />
          </div>
        </div>

        <div style="display:flex;gap:12px">
          <div class="form-item" style="flex:1"><label>月租金 (元)</label>
            <input v-model="leaseForm.rentAmount" type="number" />
          </div>
          <div class="form-item" style="flex:1"><label>押金 (元)</label>
            <input v-model="leaseForm.depositAmount" type="number" />
          </div>
        </div>

        <div style="display:flex;gap:12px;justify-content:flex-end;margin-top:8px">
          <button class="btn btn-default" @click="showForm=false">取消</button>
          <button class="btn btn-primary" @click="saveForm" :disabled="!canSave">保存</button>
        </div>
      </div>
    </div>
  </AdminLayout>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import AdminLayout from '../components/AdminLayout.vue'
import { getLeasePage, saveLease, deleteLease, updateLeaseStatus as apiUpdateLeaseStatus, getUserPage, getRoomPage, getApartmentPage, getLeaseTerms, getPaymentTypes } from '../api'

// 列表
const list = ref([]); const page = ref(1); const totalPages = ref(1)
const search = reactive({ status: '' })

// ID→名称映射
const userMap = reactive({}); const roomMap = reactive({}); const apartmentMap = reactive({})
const termMap = reactive({}); const payMap = reactive({})

// 下拉选项
const userOptions = ref([]); const roomOptions = ref([]); const termOptions = ref([]); const payOptions = ref([])
// 房间额外信息（用于选择后自动填充）
const roomExtra = reactive({})

// 表单
const showForm = ref(false)
const leaseForm = reactive({ userId:'', roomId:'', apartmentId:'', apartmentName:'', leaseTermId:'', paymentTypeId:'', startDate:'', endDate:'', rentAmount:'', depositAmount:0, status:'signed' })

const canSave = computed(() => leaseForm.userId && leaseForm.roomId && leaseForm.leaseTermId && leaseForm.paymentTypeId && leaseForm.startDate && leaseForm.rentAmount)

onMounted(async () => {
  // 并行加载所有选项
  const [users, rooms, terms, pays, apartments] = await Promise.all([
    getUserPage({ page:1, size:500 }),
    getRoomPage({ page:1, size:500 }),
    getLeaseTerms(),
    getPaymentTypes(),
    getApartmentPage({ page:1, size:500 })
  ])
  userOptions.value = (users.data.records || []).map(u => ({ id: u.id, phone: u.phone, nickname: u.nickname }))
  roomOptions.value = (rooms.data.records || []).map(r => {
    const apt = (apartments.data.records||[]).find(a => a.id === r.apartmentId)
    const name = apt ? apt.name : '未知公寓'
    roomExtra[r.id] = { apartmentId: r.apartmentId, apartmentName: name, rentAmount: r.price }
    return { id: r.id, name: r.name, price: r.price, apartmentName: name }
  })
  termOptions.value = terms.data || []
  payOptions.value = pays.data || []

  // 建立映射表
  ;(users.data.records||[]).forEach(u => userMap[u.id] = u.phone)
  ;(rooms.data.records||[]).forEach(r => roomMap[r.id] = r.name)
  ;(apartments.data.records||[]).forEach(a => apartmentMap[a.id] = a.name)
  ;(terms.data||[]).forEach(t => termMap[t.id] = t.monthCount+'个月')
  ;(pays.data||[]).forEach(p => payMap[p.id] = p.name)

  fetchList()
})

async function fetchList() {
  const params = { page: page.value, size: 10 }
  if (search.status) params.status = search.status
  try { const r = await getLeasePage(params); list.value = r.data.records||[]; totalPages.value = r.data.pages||1 } catch (e) { console.error(e) }
}

function openForm() {
  Object.keys(leaseForm).forEach(k => leaseForm[k] = (k === 'status' ? 'signed' : (k === 'depositAmount' ? 0 : '')))
  showForm.value = true
}

// 选房间 → 自动填公寓 + 租金
function onRoomChange() {
  const extra = roomExtra[leaseForm.roomId]
  if (extra) {
    leaseForm.apartmentId = extra.apartmentId
    leaseForm.apartmentName = extra.apartmentName
    leaseForm.rentAmount = extra.rentAmount
  }
  onTermChange()
}

// 选租期/改开始日期 → 自动算结束日期
function onTermChange() {
  if (leaseForm.leaseTermId && leaseForm.startDate) {
    const term = termOptions.value.find(t => t.id == leaseForm.leaseTermId)
    if (term) {
      const start = new Date(leaseForm.startDate)
      const end = new Date(start)
      end.setMonth(end.getMonth() + term.monthCount)
      leaseForm.endDate = end.toISOString().slice(0, 10)
    }
  }
}

async function saveForm() {
  try {
    await saveLease({
      userId: leaseForm.userId, roomId: leaseForm.roomId, apartmentId: leaseForm.apartmentId,
      leaseTermId: leaseForm.leaseTermId, paymentTypeId: leaseForm.paymentTypeId,
      startDate: leaseForm.startDate, endDate: leaseForm.endDate,
      rentAmount: Number(leaseForm.rentAmount), depositAmount: Number(leaseForm.depositAmount)||0,
      status: 'signed'
    })
    showForm.value = false; fetchList()
  } catch (e) { alert('保存失败: ' + (e.response?.data?.msg || e.message)) }
}

async function updateLeaseStatus(id, status) { await apiUpdateLeaseStatus(id, status); fetchList() }
async function doDelete(id) { if (!confirm('确定删除？')) return; await deleteLease(id); fetchList() }

function leaseStatusText(s) { return { signed:'已签订',active:'执行中',expired:'已到期',terminated:'已解约' }[s] || s }
function leaseStatusClass(s) { return { signed:'tag tag-orange',active:'tag tag-green',expired:'tag tag-gray',terminated:'tag tag-red' }[s] || 'tag' }
</script>
