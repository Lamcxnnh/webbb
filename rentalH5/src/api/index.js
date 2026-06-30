import axios from 'axios'
import router from '../router'

const http = axios.create({
  baseURL: '/app',
  timeout: 10000
})

// 请求拦截器 - 添加 token
http.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers['rental-app-token'] = token
  }
  return config
})

// 响应拦截器 - 处理通用错误
http.interceptors.response.use(
  response => response.data,
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      router.push('/login')
    }
    return Promise.reject(error)
  }
)

// ==================== API 接口 ====================

// 登录
export const sendSmsCode = (phone) => http.get('/login/sms-code', { params: { phone } })
export const login = (phone, code) => http.post('/login', { phone, code })
export const getUserInfo = () => http.get('/login/info')

// 地区
export const getProvinces = () => http.get('/district/province')
export const getCities = (parentId) => http.get('/district/city', { params: { parentId } })
export const getDistricts = (parentId) => http.get('/district/district', { params: { parentId } })

// 支付方式
export const getPaymentTypes = () => http.get('/payment-type/list')

// 房间
export const getRoomPage = (params) => http.get('/room/page', { params })
export const getRoomDetail = (id) => http.get(`/room/${id}`)

// 公寓
export const getApartmentPage = (params) => http.get('/apartment/page', { params })
export const getApartmentDetail = (id) => http.get(`/apartment/${id}`)

// 预约
export const saveAppointment = (data) => http.post('/appointment/save', data)
export const getMyAppointments = (page = 1, size = 10) =>
  http.get('/appointment/my', { params: { page, size } })
export const getAppointmentDetail = (id) => http.get(`/appointment/${id}`)

// 租约
export const getMyLeases = (page = 1, size = 10) =>
  http.get('/lease/my', { params: { page, size } })
export const getLeaseDetail = (id) => http.get(`/lease/${id}`)
export const saveLease = (data) => http.post('/lease/save', data)
export const getPaymentOptions = (roomId) => http.get('/lease/payment-options', { params: { roomId } })
export const getTermOptions = (roomId) => http.get('/lease/term-options', { params: { roomId } })

export default http
