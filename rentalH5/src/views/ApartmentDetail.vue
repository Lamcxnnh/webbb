<template>
  <div>
    <div class="header">
      <button class="back" @click="$router.back()">&larr;</button>
      <h1>公寓详情</h1>
      <span></span>
    </div>

    <div v-if="apartment">
      <img :src="apartment.coverUrl || fallbackImg" class="detail-img" />

      <div class="detail-section">
        <h3>{{ apartment.name }}</h3>
        <p style="color:#999" v-if="apartment.detailAddress">{{ apartment.detailAddress }}</p>
        <p style="margin-top:8px;line-height:1.6" v-if="apartment.intro">{{ apartment.intro }}</p>
      </div>

      <div class="detail-section" v-if="apartment.facilities?.length">
        <h3>配套设施</h3>
        <div style="display:flex;gap:8px;flex-wrap:wrap">
          <span class="tag" v-for="f in apartment.facilities" :key="f.id">{{ f.name }}</span>
        </div>
      </div>

      <div class="detail-section" v-if="apartment.phone">
        <h3>联系电话</h3>
        <a :href="'tel:' + apartment.phone" style="color:#4e6ef2;font-size:18px">{{ apartment.phone }}</a>
      </div>
    </div>
    <div v-else class="page" style="text-align:center;padding:80px 0">加载中...</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getApartmentDetail } from '../api'

const route = useRoute()
const apartment = ref(null)
const fallbackImg = 'data:image/svg+xml,<svg xmlns=%22http://www.w3.org/2000/svg%22 width=%22375%22 height=%22240%22><rect fill=%22%23ddd%22 width=%22375%22 height=%22240%22/><text fill=%22%23999%22 x=%22120%22 y=%22128%22 font-size=%2216%22>暂无图片</text></svg>'

onMounted(async () => {
  try {
    const res = await getApartmentDetail(route.params.id)
    apartment.value = res.data
  } catch (e) {
    alert('加载失败')
  }
})
</script>
