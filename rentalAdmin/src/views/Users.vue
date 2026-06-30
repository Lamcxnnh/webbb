<template>
  <AdminLayout>
    <div class="main-header"><h2>用户管理</h2></div>

    <div class="table-wrap" style="margin-bottom:16px">
      <div class="search-bar">
        <input v-model="search.phone" placeholder="手机号" />
        <input v-model="search.nickname" placeholder="昵称" />
        <select v-model="search.status" @change="fetchList">
          <option value="">全部状态</option><option value="1">正常</option><option value="0">已禁用</option>
        </select>
        <button class="btn btn-primary btn-sm" @click="fetchList">搜索</button>
      </div>
    </div>

    <div class="table-wrap">
      <table>
        <thead><tr><th>ID</th><th>手机号</th><th>昵称</th><th>真实姓名</th><th>身份证</th><th>状态</th><th>注册时间</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="u in list" :key="u.id">
            <td>{{ u.id }}</td><td>{{ u.phone }}</td><td>{{ u.nickname }}</td><td>{{ u.realName || '-' }}</td><td>{{ u.idCard || '-' }}</td>
            <td><span :class="u.status?'tag tag-green':'tag tag-red'">{{ u.status?'正常':'已禁用' }}</span></td>
            <td>{{ u.createTime }}</td>
            <td>
              <button class="btn btn-sm" :class="u.status?'btn-danger':'btn-success'" @click="toggleStatus(u)">{{ u.status?'禁用':'启用' }}</button>
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
import { getUserPage, updateUserStatus } from '../api'

const list = ref([]); const page = ref(1); const totalPages = ref(1)
const search = reactive({ phone: '', nickname: '', status: '' })

onMounted(fetchList)

async function fetchList() {
  const params = { page: page.value, size: 10 }
  if (search.phone) params.phone = search.phone
  if (search.nickname) params.nickname = search.nickname
  if (search.status) params.status = search.status
  try { const r = await getUserPage(params); list.value = r.data.records||[]; totalPages.value = r.data.pages||1 } catch (e) { console.error(e) }
}

async function toggleStatus(u) {
  const newStatus = u.status ? 0 : 1
  await updateUserStatus(u.id, newStatus); fetchList()
}
</script>
