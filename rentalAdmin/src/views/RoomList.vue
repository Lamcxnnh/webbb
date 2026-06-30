<template>
  <AdminLayout>
    <div class="main-header"><h2>房间管理</h2><button class="btn btn-primary" @click="openForm()">新增房间</button></div>

    <div class="table-wrap" style="margin-bottom:16px">
      <div class="search-bar">
        <input v-model="search.name" placeholder="房间名称" @keyup.enter="fetchList" />
        <select v-model="search.isRelease" @change="fetchList">
          <option value="">全部状态</option><option value="1">已发布</option><option value="0">未发布</option>
        </select>
        <button class="btn btn-primary btn-sm" @click="fetchList">搜索</button>
      </div>
    </div>

    <div class="table-wrap">
      <table>
        <thead><tr><th>ID</th><th>名称</th><th>公寓ID</th><th>价格</th><th>面积</th><th>户型</th><th>状态</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="r in list" :key="r.id">
            <td>{{ r.id }}</td><td>{{ r.name }}</td><td>{{ r.apartmentId }}</td>
            <td>¥{{ r.price }}</td><td>{{ r.area }}㎡</td><td>{{ r.roomType }}</td>
            <td><span :class="r.isRelease?'tag tag-green':'tag tag-gray'">{{ r.isRelease?'已发布':'未发布' }}</span></td>
            <td>
              <button class="btn btn-default btn-sm" @click="openForm(r)">编辑</button>
              <button class="btn btn-sm" :class="r.isRelease?'btn-default':'btn-success'" @click="toggleRelease(r)">{{ r.isRelease?'下架':'发布' }}</button>
              <button class="btn btn-danger btn-sm" @click="doDelete(r.id)">删除</button>
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
      <div class="modal" style="width:680px">
        <h3>{{ form.id ? '编辑房间' : '新增房间' }}</h3>
        <div class="form-item"><label>名称</label><input v-model="form.name" /></div>
        <div style="display:flex;gap:12px">
          <div class="form-item" style="flex:1"><label>所属公寓</label>
            <select v-model="form.apartmentId"><option value="">请选择</option>
              <option v-for="a in apartments" :key="a.id" :value="a.id">{{ a.name }}</option>
            </select>
          </div>
          <div class="form-item" style="flex:1"><label>户型</label><input v-model="form.roomType" placeholder="如: 1室1厅" /></div>
        </div>
        <div style="display:flex;gap:12px">
          <div class="form-item" style="flex:1"><label>租金(元/月)</label><input v-model="form.price" type="number" /></div>
          <div class="form-item" style="flex:1"><label>面积(㎡)</label><input v-model="form.area" type="number" /></div>
          <div class="form-item" style="flex:1"><label>楼层</label><input v-model="form.floor" placeholder="如: 8/20层" /></div>
        </div>
        <div class="form-item"><label>介绍</label><textarea v-model="form.intro" rows="2" /></div>
        <div class="form-item"><label>封面图</label>
          <div style="display:flex;gap:8px;align-items:center">
            <input type="file" accept="image/*" @change="e=>coverFile=e.target.files[0]" ref="coverInput" style="flex:1" />
            <button class="btn btn-primary btn-sm" @click="uploadCover" :disabled="!coverFile || uploading">上传</button>
          </div>
          <img v-if="form.coverUrl" :src="form.coverUrl" style="margin-top:8px;width:200px;height:120px;object-fit:cover;border-radius:6px;border:1px solid #eee" />
        </div>
        <div class="form-item"><label>图片列表</label>
          <div style="display:flex;gap:8px;align-items:center;margin-bottom:8px">
            <input type="file" accept="image/*" @change="onImageFile" ref="imageInput" style="flex:1" />
            <button class="btn btn-primary btn-sm" @click="uploadImage" :disabled="!imageFile || uploading">上传</button>
          </div>
          <div v-if="imageList.length" style="display:flex;gap:8px;flex-wrap:wrap">
            <div v-for="(url,i) in imageList" :key="i" style="position:relative">
              <img :src="url" style="width:80px;height:60px;object-fit:cover;border-radius:4px" />
              <button @click="imageList.splice(i,1)" style="position:absolute;top:-6px;right:-6px;width:20px;height:20px;border-radius:50%;background:#e74c3c;color:#fff;font-size:12px;line-height:20px;text-align:center">×</button>
            </div>
          </div>
        </div>
        <div class="form-item"><label>支付方式</label>
          <div class="check-group">
            <label v-for="p in payTypes" :key="p.id"><input type="checkbox" :value="p.id" v-model="form.paymentTypeIds" />{{ p.name }}</label>
          </div>
        </div>
        <div class="form-item"><label>租期</label>
          <div class="check-group">
            <label v-for="t in leaseTerms" :key="t.id"><input type="checkbox" :value="t.id" v-model="form.leaseTermIds" />{{ t.monthCount }}个月</label>
          </div>
        </div>
        <div class="form-item"><label>标签</label>
          <div class="check-group">
            <label v-for="l in labels" :key="l.id"><input type="checkbox" :value="l.id" v-model="form.labelIds" />{{ l.name }}</label>
          </div>
        </div>
        <div class="form-item"><label>配套</label>
          <div class="check-group">
            <label v-for="f in facilities" :key="f.id"><input type="checkbox" :value="f.id" v-model="form.facilityIds" />{{ f.name }}</label>
          </div>
        </div>
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
import { ref, reactive, onMounted, watch } from 'vue'
import AdminLayout from '../components/AdminLayout.vue'
import { getRoomPage, saveRoom, deleteRoom, updateRoomRelease, getApartmentPage, getPaymentTypes, getLeaseTerms, getLabels, getFacilities, uploadFile } from '../api'

const list = ref([]); const page = ref(1); const totalPages = ref(1)
const search = reactive({ name: '', isRelease: '' })
const apartments = ref([]); const payTypes = ref([]); const leaseTerms = ref([]); const labels = ref([]); const facilities = ref([])
const showForm = ref(false)
const coverFile = ref(null); const coverInput = ref(null)
const imageFile = ref(null); const imageInput = ref(null)
const uploading = ref(false)
const imageList = ref([])
const form = reactive({ id: null, apartmentId: '', name: '', roomType: '', price: '', area: '', floor: '', intro: '', coverUrl: '', isRelease: 1, paymentTypeIds: [], leaseTermIds: [], labelIds: [], facilityIds: [] })

onMounted(async () => {
  const [apt, pt, lt, lb, fc] = await Promise.all([
    getApartmentPage({ page:1, size:100 }), getPaymentTypes(), getLeaseTerms(), getLabels('room'), getFacilities()
  ])
  apartments.value = apt.data.records || []
  payTypes.value = pt.data || []; leaseTerms.value = lt.data || []; labels.value = lb.data || []; facilities.value = fc.data || []
  fetchList()
})

async function fetchList() {
  const params = { page: page.value, size: 10 }
  if (search.name) params.name = search.name
  if (search.isRelease) params.isRelease = search.isRelease
  try {
    const res = await getRoomPage(params)
    list.value = res.data.records || []; totalPages.value = res.data.pages || 1
  } catch (e) { console.error(e) }
}

// 上传封面图
async function uploadCover() {
  if (!coverFile.value) return
  uploading.value = true
  try { const res = await uploadFile(coverFile.value, 'room'); form.coverUrl = res.data; coverFile.value = null; if (coverInput.value) coverInput.value.value = '' }
  catch (e) { alert('上传失败: ' + (e.response?.data?.msg || e.message)) }
  finally { uploading.value = false }
}
// 上传图片到列表
function onImageFile(e) { imageFile.value = e.target.files[0] }
async function uploadImage() {
  if (!imageFile.value) return
  uploading.value = true
  try { const res = await uploadFile(imageFile.value, 'room'); imageList.value.push(res.data); imageFile.value = null; if (imageInput.value) imageInput.value.value = '' }
  catch (e) { alert('上传失败: ' + (e.response?.data?.msg || e.message)) }
  finally { uploading.value = false }
}

function openForm(r) {
  if (r) {
    Object.assign(form, {
      ...r, price: String(r.price||''), area: String(r.area||''),
      paymentTypeIds: [], leaseTermIds: [], labelIds: [], facilityIds: []
    })
    // 加载已有图片列表
    imageList.value = parseImages(r.images)
  } else {
    Object.keys(form).forEach(k => form[k] = Array.isArray(form[k]) ? [] : (k === 'isRelease' ? 1 : null))
    imageList.value = []
  }
  showForm.value = true
}

function parseImages(images) {
  if (!images) return []
  if (Array.isArray(images)) return images
  try { return JSON.parse(images) } catch { return [] }
}

async function saveForm() {
  try {
    await saveRoom({
      ...form,
      price: Number(form.price)||0, area: Number(form.area)||null,
      images: JSON.stringify(imageList.value)
    })
    showForm.value = false; fetchList()
  } catch (e) { alert('保存失败: ' + (e.response?.data?.msg || e.message)) }
}

async function toggleRelease(r) { await updateRoomRelease(r.id, r.isRelease ? 0 : 1); fetchList() }
async function doDelete(id) { if (!confirm('确定删除？')) return; await deleteRoom(id); fetchList() }
</script>
