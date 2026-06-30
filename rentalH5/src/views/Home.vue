<template>
  <div>
    <div class="header"><h1>拾光公寓</h1></div>
    <!-- 三级联动筛选 -->
    <div class="filter-bar">
      <select v-model="selectedProvince" @change="onProvinceChange">
        <option value="">全部省份</option>
        <option v-for="p in provinces" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>
      <select v-model="selectedCity" @change="onCityChange" :disabled="!cities.length">
        <option value="">全部城市</option>
        <option v-for="c in cities" :key="c.id" :value="c.id">{{ c.name }}</option>
      </select>
      <select v-model="search.districtId" @change="onSearch">
        <option value="">全部区县</option>
        <option v-for="d in districts" :key="d.id" :value="d.id">{{ d.name }}</option>
      </select>
    </div>
    <div style="padding:6px 16px;display:flex;gap:8px">
      <input v-model="search.name" placeholder="搜索房间名称" @keyup.enter="onSearch" style="flex:1;padding:8px 10px;border:1px solid #ddd;border-radius:6px;font-size:13px" />
    </div>
    <!-- 列表 -->
    <div class="page" style="padding-top:8px">
      <div v-if="list.length === 0" style="text-align:center;padding:60px 0;color:#999">暂无房源</div>
      <router-link v-for="room in list" :key="room.id" :to="'/room/' + room.id" class="room-card">
        <img :src="room.coverUrl || fallbackImg" alt="" />
        <div class="info">
          <div class="name">{{ room.name }}</div>
          <div class="meta">{{ room.floor || '' }} {{ room.area ? room.area + '㎡' : '' }}</div>
          <div class="price">{{ room.price }}<span>元/月</span></div>
        </div>
      </router-link>
      <div v-if="list.length >= search.size" style="text-align:center;padding:12px">
        <button class="btn btn-outline btn-sm" @click="loadMore">加载更多</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getRoomPage, getProvinces, getCities, getDistricts } from '../api'

const list = ref([])
const provinces = ref([])
const cities = ref([])
const districts = ref([])
const selectedProvince = ref('')
const selectedCity = ref('')
const search = reactive({ page: 1, size: 10, districtId: '', name: '' })
const fallbackImg = 'data:image/svg+xml,<svg xmlns=%22http://www.w3.org/2000/svg%22 width=%22120%22 height=%2290%22><rect fill=%22%23ddd%22 width=%22120%22 height=%2290%22/><text fill=%22%23999%22 x=%2230%22 y=%2252%22 font-size=%2214%22>暂无图片</text></svg>'

onMounted(async () => {
  try {
    const res = await getProvinces()
    provinces.value = res.data || []
  } catch (e) { console.error(e) }
  fetchRooms()
})

async function onProvinceChange() {
  selectedCity.value = ''
  search.districtId = ''
  cities.value = []
  districts.value = []
  if (selectedProvince.value) {
    try {
      const res = await getCities(selectedProvince.value)
      cities.value = res.data || []
    } catch (e) { console.error(e) }
  }
  onSearch()
}

async function onCityChange() {
  search.districtId = ''
  districts.value = []
  if (selectedCity.value) {
    try {
      const res = await getDistricts(selectedCity.value)
      districts.value = res.data || []
    } catch (e) { console.error(e) }
  }
  onSearch()
}

async function fetchRooms() {
  const params = { page: search.page, size: search.size }
  if (search.districtId) params.districtId = search.districtId
  if (search.name) params.name = search.name
  try {
    const res = await getRoomPage(params)
    const pageData = res.data
    if (search.page === 1) {
      list.value = pageData.records || []
    } else {
      list.value.push(...(pageData.records || []))
    }
  } catch (e) { console.error(e) }
}

function onSearch() {
  search.page = 1
  fetchRooms()
}

function loadMore() {
  search.page++
  fetchRooms()
}
</script>
