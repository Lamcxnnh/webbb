<template>
  <AdminLayout>
    <div class="main-header">
      <h2>公寓管理</h2>
      <button class="btn btn-primary" @click="openForm()">新增公寓</button>
    </div>

    <!-- 搜索 -->
    <div class="table-wrap" style="margin-bottom:16px">
      <div class="search-bar">
        <input v-model="search.name" placeholder="公寓名称" @keyup.enter="fetchList" />
        <select v-model="search.province" @change="onProvinceChange">
          <option value="">全部省份</option>
          <option v-for="p in provinces" :key="p.id" :value="p.id">{{ p.name }}</option>
        </select>
        <select v-model="search.city" @change="onCityChange">
          <option value="">全部城市</option>
          <option v-for="c in cities" :key="c.id" :value="c.id">{{ c.name }}</option>
        </select>
        <select v-model="search.districtId" @change="fetchList">
          <option value="">全部区县</option>
          <option v-for="d in districts" :key="d.id" :value="d.id">{{ d.name }}</option>
        </select>
        <select v-model="search.isRelease" @change="fetchList">
          <option value="">全部状态</option>
          <option value="1">已发布</option>
          <option value="0">未发布</option>
        </select>
        <button class="btn btn-primary btn-sm" @click="fetchList">搜索</button>
      </div>
    </div>

    <!-- 列表 -->
    <div class="table-wrap">
      <table>
        <thead><tr><th>ID</th><th>名称</th><th>区域</th><th>地址</th><th>电话</th><th>状态</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="a in list" :key="a.id">
            <td>{{ a.id }}</td>
            <td>{{ a.name }}</td>
            <td>{{ a.districtId }}</td>
            <td style="max-width:160px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">{{ a.detailAddress }}</td>
            <td>{{ a.phone }}</td>
            <td><span :class="a.isRelease?'tag tag-green':'tag tag-gray'">{{ a.isRelease?'已发布':'未发布' }}</span></td>
            <td>
              <button class="btn btn-default btn-sm" @click="openForm(a)">编辑</button>
              <button class="btn btn-sm" :class="a.isRelease?'btn-default':'btn-success'" @click="toggleRelease(a)">{{ a.isRelease?'下架':'发布' }}</button>
              <button class="btn btn-danger btn-sm" @click="doDelete(a.id)">删除</button>
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

    <!-- 表单弹窗 -->
    <div v-if="showForm" class="modal-overlay" @click.self="showForm=false">
      <div class="modal">
        <h3>{{ form.id ? '编辑公寓' : '新增公寓' }}</h3>
        <div class="form-item"><label>名称</label><input v-model="form.name" /></div>
        <div class="form-item"><label>介绍</label><textarea v-model="form.intro" rows="3" /></div>
        <div class="form-item"><label>区县</label>
          <select v-model="form.districtId"><option value="">请选择</option>
            <option v-for="d in allDistricts" :key="d.id" :value="d.id">{{ d.name }}</option>
          </select>
        </div>
        <div class="form-item"><label>详细地址</label><input v-model="form.detailAddress" /></div>
        <div class="form-item"><label>联系电话</label><input v-model="form.phone" /></div>
        <div class="form-item"><label>封面图</label>
          <div style="display:flex;gap:8px;align-items:center">
            <input type="file" accept="image/*" @change="onCoverFile" ref="coverInput" style="flex:1" />
            <button class="btn btn-primary btn-sm" @click="uploadCover" :disabled="!coverFile || uploading">上传</button>
          </div>
          <img v-if="form.coverUrl" :src="form.coverUrl" style="margin-top:8px;width:200px;height:120px;object-fit:cover;border-radius:6px;border:1px solid #eee" />
        </div>
        <div class="form-item"><label>经度</label><input v-model="form.longitude" /></div>
        <div class="form-item"><label>纬度</label><input v-model="form.latitude" /></div>
        <div class="form-item"><label>发布状态</label>
          <select v-model="form.isRelease"><option :value="1">已发布</option><option :value="0">未发布</option></select>
        </div>
        <div style="display:flex;gap:12px;justify-content:flex-end">
          <button class="btn btn-default" @click="showForm=false">取消</button>
          <button class="btn btn-primary" @click="saveForm">保存</button>
        </div>
      </div>
    </div>
  </AdminLayout>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import AdminLayout from '../components/AdminLayout.vue'
import { getApartmentPage, saveApartment, deleteApartment, updateApartmentRelease, getProvinces, getCities, getDistricts, uploadFile } from '../api'

const coverFile = ref(null)
const coverInput = ref(null)
const uploading = ref(false)

const list = ref([])
const page = ref(1)
const totalPages = ref(1)
const search = reactive({ name: '', province: '', city: '', districtId: '', isRelease: '' })
const provinces = ref([])
const cities = ref([])
const districts = ref([])
const allDistricts = ref([])
const showForm = ref(false)
const form = reactive({ id: null, name: '', intro: '', districtId: '', detailAddress: '', phone: '', coverUrl: '', longitude: '', latitude: '', isRelease: 1 })

onMounted(async () => {
  const [prov] = await Promise.all([getProvinces()])
  provinces.value = prov.data || []
  // 预加载所有区县
  const allD = []
  for (const p of provinces.value) {
    const citiesRes = await getCities(p.id)
    for (const c of (citiesRes.data || [])) {
      const distRes = await getDistricts(c.id)
      for (const d of (distRes.data || [])) allD.push(d)
    }
  }
  allDistricts.value = allD
  fetchList()
})

async function onProvinceChange() { search.city = ''; search.districtId = ''; cities.value = []; districts.value = []
  if (search.province) { const res = await getCities(search.province); cities.value = res.data || [] } fetchList() }
async function onCityChange() { search.districtId = ''; districts.value = []
  if (search.city) { const res = await getDistricts(search.city); districts.value = res.data || [] } fetchList() }

async function fetchList() {
  const params = { page: page.value, size: 10 }
  if (search.name) params.name = search.name
  if (search.districtId) params.districtId = search.districtId
  if (search.isRelease) params.isRelease = search.isRelease
  try {
    const res = await getApartmentPage(params)
    list.value = res.data.records || []
    totalPages.value = res.data.pages || 1
  } catch (e) { console.error(e) }
}

function onCoverFile(e) { coverFile.value = e.target.files[0] }
async function uploadCover() {
  if (!coverFile.value) return
  uploading.value = true
  try {
    const res = await uploadFile(coverFile.value, 'apartment')
    form.coverUrl = res.data
    coverFile.value = null
    if (coverInput.value) coverInput.value.value = ''
  } catch (e) { alert('上传失败: ' + (e.response?.data?.msg || e.message)) }
  finally { uploading.value = false }
}

function openForm(a) {
  if (a) { Object.assign(form, { ...a }) }
  else { Object.keys(form).forEach(k => form[k] = k === 'isRelease' ? 1 : null) }
  showForm.value = true
}

async function saveForm() {
  try {
    await saveApartment({ ...form, latitude: Number(form.latitude)||null, longitude: Number(form.longitude)||null })
    showForm.value = false; fetchList()
  } catch (e) { alert('保存失败: ' + (e.response?.data?.msg || e.message)) }
}

async function toggleRelease(a) {
  const newStatus = a.isRelease ? 0 : 1
  await updateApartmentRelease(a.id, newStatus)
  fetchList()
}

async function doDelete(id) {
  if (!confirm('确定删除？')) return
  await deleteApartment(id); fetchList()
}
</script>
