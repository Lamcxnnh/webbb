<template>
  <div class="login-page">
    <h1>拾光公寓</h1>
    <p class="sub">手机验证码登录</p>

    <div style="width:100%">
      <div class="form-item">
        <label>手机号</label>
        <input v-model="phone" type="tel" maxlength="11" placeholder="请输入手机号" />
      </div>
      <div class="form-item">
        <label>验证码</label>
        <div class="sms-row">
          <input v-model="code" type="text" maxlength="6" placeholder="6位验证码" />
          <button class="sms-btn" :disabled="countdown > 0" @click="sendCode">
            {{ countdown > 0 ? countdown + 's' : '获取验证码' }}
          </button>
        </div>
      </div>
      <button class="btn btn-primary" @click="doLogin" :disabled="loading">
        {{ loading ? '登录中...' : '登录 / 注册' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { sendSmsCode, login } from '../api'

const router = useRouter()
const phone = ref('')
const code = ref('')
const countdown = ref(0)
const loading = ref(false)

async function sendCode() {
  if (!phone.value || phone.value.length < 11) {
    return alert('请输入正确的手机号')
  }
  try {
    const res = await sendSmsCode(phone.value)
    alert(res.data || res.msg || '验证码已发送')
    countdown.value = 60
    const timer = setInterval(() => {
      countdown.value--
      if (countdown.value <= 0) clearInterval(timer)
    }, 1000)
  } catch (e) {
    alert('发送失败: ' + (e.response?.data?.msg || e.message))
  }
}

async function doLogin() {
  if (!phone.value || !code.value) return alert('请填写完整信息')
  loading.value = true
  try {
    const res = await login(phone.value, code.value)
    localStorage.setItem('token', res.data.token)
    router.push('/')
  } catch (e) {
    alert('登录失败: ' + (e.response?.data?.msg || e.message))
  } finally {
    loading.value = false
  }
}
</script>
