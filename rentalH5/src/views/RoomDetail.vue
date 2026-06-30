<template>
  <div>
    <div class="header">
      <router-link to="/" class="back">&larr;</router-link>
      <h1>房间详情</h1>
      <span></span>
    </div>

    <div v-if="room">
      <!-- 封面图 -->
      <img :src="room.coverUrl || fallbackImg" class="detail-img" />

      <!-- 基本信息 -->
      <div class="detail-section">
        <h3>{{ room.name }}</h3>
        <div class="detail-price">{{ room.price }}<span> 元/月</span></div>
        <p style="color:#999;margin-top:8px">
          {{ room.roomType || '' }} {{ room.area ? room.area + '㎡' : '' }} {{ room.floor || '' }}
        </p>
        <p style="margin-top:8px;line-height:1.6" v-if="room.intro">{{ room.intro }}</p>

        <!-- 标签 -->
        <div style="display:flex;gap:8px;flex-wrap:wrap;margin-top:12px" v-if="room.labels?.length">
          <span class="tag" v-for="l in room.labels" :key="l.id">{{ l.name }}</span>
        </div>
      </div>

      <!-- 配套 -->
      <div class="detail-section" v-if="room.facilities?.length">
        <h3>配套设施</h3>
        <div style="display:flex;gap:8px;flex-wrap:wrap">
          <span class="tag" v-for="f in room.facilities" :key="f.id">{{ f.name }}</span>
        </div>
      </div>

      <!-- 支付方式 -->
      <div class="detail-section" v-if="room.paymentTypes?.length">
        <h3>支付方式</h3>
        <div style="display:flex;gap:8px;flex-wrap:wrap">
          <span class="tag" v-for="p in room.paymentTypes" :key="p.id">{{ p.name }}</span>
        </div>
      </div>

      <!-- 租期 -->
      <div class="detail-section" v-if="room.leaseTerms?.length">
        <h3>可选租期</h3>
        <div style="display:flex;gap:8px;flex-wrap:wrap">
          <span class="tag" v-for="t in room.leaseTerms" :key="t.id">{{ t.monthCount }}个月</span>
        </div>
      </div>

      <!-- 所属公寓 -->
      <div class="detail-section" v-if="room.apartmentId">
        <h3>所属公寓</h3>
        <router-link :to="'/apartment/' + room.apartmentId" style="color:#4e6ef2">
          {{ room.apartmentName || '查看公寓详情' }} &rarr;
        </router-link>
      </div>

      <!-- 操作按钮 -->
      <div class="detail-section" style="display:flex;gap:12px">
        <button class="btn btn-primary" @click="showAppoint = true">预约看房</button>
      </div>

      <!-- 预约弹窗 -->
      <div v-if="showAppoint" style="position:fixed;inset:0;background:rgba(0,0,0,.5);z-index:999;display:flex;align-items:flex-end" @click.self="showAppoint = false">
        <div style="width:100%;background:#fff;border-radius:16px 16px 0 0;padding:20px">
          <h3 style="margin-bottom:16px">预约看房</h3>
          <div class="form-item">
            <label>联系人</label>
            <input v-model="appoint.name" placeholder="请输入姓名" />
          </div>
          <div class="form-item">
            <label>手机号</label>
            <input v-model="appoint.phone" placeholder="请输入手机号" />
          </div>
          <div class="form-item">
            <label>预约时间</label>
            <input v-model="appoint.time" type="datetime-local" />
          </div>
          <div class="form-item">
            <label>备注</label>
            <textarea v-model="appoint.remark" rows="2" placeholder="可选"></textarea>
          </div>
          <div style="display:flex;gap:12px">
            <button class="btn btn-outline" style="flex:1" @click="showAppoint = false">取消</button>
            <button class="btn btn-primary" style="flex:1" @click="submitAppoint">提交预约</button>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="page" style="text-align:center;padding:80px 0">加载中...</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getRoomDetail, saveAppointment } from '../api'

const route = useRoute()
const room = ref(null)
const showAppoint = ref(false)
const appoint = ref({ name: '', phone: '', time: '', remark: '' })
const fallbackImg = 'data:image/svg+xml,<svg xmlns=%22http://www.w3.org/2000/svg%22 width=%22375%22 height=%22240%22><rect fill=%22%23ddd%22 width=%22375%22 height=%22240%22/><text fill=%22%23999%22 x=%22120%22 y=%22128%22 font-size=%2216%22>暂无图片</text></svg>'

onMounted(async () => {
  try {
    const res = await getRoomDetail(route.params.id)
    room.value = res.data
  } catch (e) {
    alert('加载失败')
  }
})

async function submitAppoint() {
  if (!appoint.value.name || !appoint.value.phone || !appoint.value.time) {
    return alert('请填写完整信息')
  }
  try {
    await saveAppointment({
      roomId: room.value.id,
      apartmentId: room.value.apartmentId,
      name: appoint.value.name,
      phone: appoint.value.phone,
      appointmentTime: appoint.value.time,
      remark: appoint.value.remark,
      status: 'pending'
    })
    alert('预约成功')
    showAppoint.value = false
  } catch (e) {
    alert('预约失败: ' + (e.response?.data?.msg || e.message))
  }
}
</script>
