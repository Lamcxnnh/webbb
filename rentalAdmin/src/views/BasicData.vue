<template>
  <AdminLayout>
    <div class="main-header"><h2>基础数据管理</h2></div>
    <div style="display:flex;gap:12px;margin-bottom:16px;flex-wrap:wrap">
      <button class="btn" :class="tab==='payment'?'btn-primary':'btn-default'" @click="tab='payment'">支付方式</button>
      <button class="btn" :class="tab==='leaseTerm'?'btn-primary':'btn-default'" @click="tab='leaseTerm'">租期</button>
      <button class="btn" :class="tab==='label'?'btn-primary':'btn-default'" @click="tab='label'">标签</button>
      <button class="btn" :class="tab==='facility'?'btn-primary':'btn-default'" @click="tab='facility'">配套</button>
      <button class="btn" :class="tab==='attr'?'btn-primary':'btn-default'" @click="tab='attr'">属性</button>
    </div>

    <div class="table-wrap">
      <!-- 支付方式 -->
      <div v-if="tab==='payment'">
        <div style="margin-bottom:12px;display:flex;gap:8px">
          <input v-model="paymentForm.name" placeholder="名称" style="width:200px" />
          <input v-model="paymentForm.sortOrder" type="number" placeholder="排序" style="width:80px" />
          <button class="btn btn-primary btn-sm" @click="savePaymentType()">保存</button>
        </div>
        <table><thead><tr><th>ID</th><th>名称</th><th>排序</th><th>操作</th></tr></thead>
          <tbody><tr v-for="p in paymentList" :key="p.id"><td>{{p.id}}</td><td>{{p.name}}</td><td>{{p.sortOrder}}</td>
            <td><button class="btn btn-default btn-sm" @click="paymentForm={...p}">编辑</button>
            <button class="btn btn-danger btn-sm" @click="deletePaymentType(p.id)">删除</button></td></tr></tbody></table>
      </div>
      <!-- 租期 -->
      <div v-if="tab==='leaseTerm'">
        <div style="margin-bottom:12px;display:flex;gap:8px">
          <input v-model="leaseTermForm.monthCount" type="number" placeholder="月数" style="width:100px" />
          <input v-model="leaseTermForm.sortOrder" type="number" placeholder="排序" style="width:80px" />
          <button class="btn btn-primary btn-sm" @click="saveLeaseTerm()">保存</button>
        </div>
        <table><thead><tr><th>ID</th><th>月数</th><th>排序</th><th>操作</th></tr></thead>
          <tbody><tr v-for="t in leaseTermList" :key="t.id"><td>{{t.id}}</td><td>{{t.monthCount}}个月</td><td>{{t.sortOrder}}</td>
            <td><button class="btn btn-default btn-sm" @click="leaseTermForm={...t}">编辑</button>
            <button class="btn btn-danger btn-sm" @click="deleteLeaseTerm(t.id)">删除</button></td></tr></tbody></table>
      </div>
      <!-- 标签 -->
      <div v-if="tab==='label'">
        <div style="margin-bottom:12px;display:flex;gap:8px">
          <input v-model="labelForm.name" placeholder="名称" style="width:200px" />
          <select v-model="labelForm.type"><option value="room">房间</option><option value="apartment">公寓</option></select>
          <input v-model="labelForm.sortOrder" type="number" placeholder="排序" style="width:80px" />
          <button class="btn btn-primary btn-sm" @click="saveLabel()">保存</button>
        </div>
        <table><thead><tr><th>ID</th><th>名称</th><th>类型</th><th>排序</th><th>操作</th></tr></thead>
          <tbody><tr v-for="l in labelList" :key="l.id"><td>{{l.id}}</td><td>{{l.name}}</td><td>{{l.type}}</td><td>{{l.sortOrder}}</td>
            <td><button class="btn btn-default btn-sm" @click="labelForm={...l}">编辑</button>
            <button class="btn btn-danger btn-sm" @click="deleteLabel(l.id)">删除</button></td></tr></tbody></table>
      </div>
      <!-- 配套 -->
      <div v-if="tab==='facility'">
        <div style="margin-bottom:12px;display:flex;gap:8px">
          <input v-model="facilityForm.name" placeholder="名称" style="width:200px" />
          <input v-model="facilityForm.sortOrder" type="number" placeholder="排序" style="width:80px" />
          <button class="btn btn-primary btn-sm" @click="saveFacility()">保存</button>
        </div>
        <table><thead><tr><th>ID</th><th>名称</th><th>排序</th><th>操作</th></tr></thead>
          <tbody><tr v-for="f in facilityList" :key="f.id"><td>{{f.id}}</td><td>{{f.name}}</td><td>{{f.sortOrder}}</td>
            <td><button class="btn btn-default btn-sm" @click="facilityForm={...f}">编辑</button>
            <button class="btn btn-danger btn-sm" @click="deleteFacility(f.id)">删除</button></td></tr></tbody></table>
      </div>
      <!-- 属性 -->
      <div v-if="tab==='attr'">
        <div style="display:flex;gap:16px">
          <div style="flex:1">
            <h4 style="margin-bottom:8px">属性名称</h4>
            <div style="margin-bottom:8px;display:flex;gap:8px">
              <input v-model="attrKeyForm.name" placeholder="名称" />
              <button class="btn btn-primary btn-sm" @click="saveAttrKey()">保存</button>
            </div>
            <table><thead><tr><th>ID</th><th>名称</th><th>操作</th></tr></thead>
              <tbody><tr v-for="k in attrKeyList" :key="k.id" @click="selectAttrKey(k)" style="cursor:pointer" :style="{background:selectedKey?.id===k.id?'#e6f7ff':''}">
                <td>{{k.id}}</td><td>{{k.name}}</td>
                <td><button class="btn btn-danger btn-sm" @click.stop="deleteAttrKey(k.id)">删除</button></td></tr></tbody></table>
          </div>
          <div style="flex:1">
            <h4 style="margin-bottom:8px">属性值 <span v-if="selectedKey" style="font-weight:400;color:#999">({{ selectedKey.name }})</span></h4>
            <div style="margin-bottom:8px;display:flex;gap:8px" v-if="selectedKey">
              <input v-model="attrValueForm.name" placeholder="值名称" />
              <button class="btn btn-primary btn-sm" @click="saveAttrValue()">保存</button>
            </div>
            <table><thead><tr><th>ID</th><th>值</th><th>操作</th></tr></thead>
              <tbody><tr v-for="v in attrValueList" :key="v.id"><td>{{v.id}}</td><td>{{v.name}}</td>
                <td><button class="btn btn-danger btn-sm" @click="deleteAttrValue(v.id)">删除</button></td></tr></tbody></table>
          </div>
        </div>
      </div>
    </div>
  </AdminLayout>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import AdminLayout from '../components/AdminLayout.vue'
import * as api from '../api'

const tab = ref('payment')
// 支付方式
const paymentList = ref([]); const paymentForm = reactive({ id: null, name: '', sortOrder: 0 })
const loadPayments = async () => { const r = await api.getPaymentTypes(); paymentList.value = r.data || [] }
const savePaymentType = async () => { await api.savePaymentType({ id: paymentForm.id||undefined, name: paymentForm.name, sortOrder: paymentForm.sortOrder }); paymentForm.id=null;paymentForm.name='';loadPayments() }
const deletePaymentType = async (id) => { if(confirm('确定删除?')){ await api.deletePaymentType(id); loadPayments() } }
// 租期
const leaseTermList = ref([]); const leaseTermForm = reactive({ id: null, monthCount: '', sortOrder: 0, unit: 'month' })
const loadLeaseTerms = async () => { const r = await api.getLeaseTerms(); leaseTermList.value = r.data || [] }
const saveLeaseTerm = async () => { await api.saveLeaseTerm({ id: leaseTermForm.id||undefined, monthCount: Number(leaseTermForm.monthCount), sortOrder: leaseTermForm.sortOrder, unit: 'month' }); leaseTermForm.id=null;leaseTermForm.monthCount='';loadLeaseTerms() }
const deleteLeaseTerm = async (id) => { if(confirm('确定删除?')){ await api.deleteLeaseTerm(id); loadLeaseTerms() } }
// 标签
const labelList = ref([]); const labelForm = reactive({ id: null, name: '', type: 'room', sortOrder: 0 })
const loadLabels = async () => { const r = await api.getLabels('room'); const r2 = await api.getLabels('apartment'); labelList.value = [...(r.data||[]), ...(r2.data||[])] }
const saveLabel = async () => { await api.saveLabel({ id: labelForm.id||undefined, name: labelForm.name, type: labelForm.type, sortOrder: labelForm.sortOrder }); labelForm.id=null;labelForm.name='';loadLabels() }
const deleteLabel = async (id) => { if(confirm('确定删除?')){ await api.deleteLabel(id); loadLabels() } }
// 配套
const facilityList = ref([]); const facilityForm = reactive({ id: null, name: '', sortOrder: 0 })
const loadFacilities = async () => { const r = await api.getFacilities(); facilityList.value = r.data || [] }
const saveFacility = async () => { await api.saveFacility({ id: facilityForm.id||undefined, name: facilityForm.name, sortOrder: facilityForm.sortOrder }); facilityForm.id=null;facilityForm.name='';loadFacilities() }
const deleteFacility = async (id) => { if(confirm('确定删除?')){ await api.deleteFacility(id); loadFacilities() } }
// 属性
const attrKeyList = ref([]); const attrValueList = ref([]); const selectedKey = ref(null)
const attrKeyForm = reactive({ id: null, name: '' }); const attrValueForm = reactive({ id: null, name: '' })
const loadAttrKeys = async () => { const r = await api.getAttrKeys(); attrKeyList.value = r.data || [] }
const selectAttrKey = async (k) => { selectedKey.value = k; attrValueForm.id=null;attrValueForm.name=''; const r = await api.getAttrValues(k.id); attrValueList.value = r.data || [] }
const saveAttrKey = async () => { await api.saveAttrKey({ id: attrKeyForm.id||undefined, name: attrKeyForm.name, sortOrder: 0 }); attrKeyForm.id=null;attrKeyForm.name='';loadAttrKeys() }
const deleteAttrKey = async (id) => { if(confirm('删除属性名称将同时删除其属性值，确定?')){ await api.deleteAttrKey(id); loadAttrKeys(); selectedKey.value=null;attrValueList.value=[] } }
const saveAttrValue = async () => { if(!selectedKey.value) return; await api.saveAttrValue({ id: attrValueForm.id||undefined, keyId: selectedKey.value.id, name: attrValueForm.name, sortOrder: 0 }); attrValueForm.id=null;attrValueForm.name='';selectAttrKey(selectedKey.value) }
const deleteAttrValue = async (id) => { if(confirm('确定删除?')){ await api.deleteAttrValue(id); selectAttrKey(selectedKey.value) } }

onMounted(() => { loadPayments(); loadLeaseTerms(); loadLabels(); loadFacilities(); loadAttrKeys() })
</script>
