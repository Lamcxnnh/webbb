<template>
  <div class="login-wrap">
    <div class="login-box">
      <h1>拾光公寓</h1>
      <p class="sub">后台管理系统</p>
      <div class="form-item">
        <label>用户名</label>
        <input v-model="form.username" placeholder="请输入用户名" @keyup.enter="doLogin" />
      </div>
      <div class="form-item">
        <label>密码</label>
        <input v-model="form.password" type="password" placeholder="请输入密码" @keyup.enter="doLogin" />
      </div>
      <div class="form-item">
        <label>验证码</label>
        <div class="captcha-row">
          <input v-model="form.captcha" placeholder="验证码" style="flex:1" @keyup.enter="doLogin" />
          <img v-if="captchaImg" :src="captchaImg" @click="refreshCaptcha" title="点击刷新" />
        </div>
      </div>
      <button class="btn btn-primary" style="width:100%;padding:12px" @click="doLogin" :disabled="loading">
        {{ loading ? '登录中...' : '登 录' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getCaptcha, login } from '../api'

const router = useRouter()
const form = reactive({ username: 'admin', password: 'admin123', captcha: '', captchaKey: '' })
const captchaImg = ref('')
const loading = ref(false)

onMounted(refreshCaptcha)

async function refreshCaptcha() {
  try {
    const res = await getCaptcha()
    // Hutool 已自带 data:image/... 前缀
    const img = res.data.captchaImage
    captchaImg.value = img.startsWith('data:') ? img : 'data:image/png;base64,' + img
    form.captchaKey = res.data.captchaKey
    form.captcha = ''
  } catch (e) { console.error(e) }
}

async function doLogin() {
  if (!form.username || !form.password || !form.captcha) return alert('请填写完整')
  loading.value = true
  try {
    const res = await login(form)
    localStorage.setItem('adminToken', res.data.token)
    router.push('/')
  } catch (e) {
    alert('登录失败: ' + (e.response?.data?.msg || e.message))
    refreshCaptcha()
  } finally { loading.value = false }
}
</script>
