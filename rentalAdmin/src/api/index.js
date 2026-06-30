import axios from 'axios'
import router from '../router'

const http = axios.create({ baseURL: '/admin', timeout: 10000 })

http.interceptors.request.use(config => {
  const token = localStorage.getItem('adminToken')
  if (token) config.headers['rental-admin-token'] = token
  return config
})

http.interceptors.response.use(
  res => res.data,
  err => {
    if (err.response?.status === 401) {
      localStorage.removeItem('adminToken')
      router.push('/login')
    }
    return Promise.reject(err)
  }
)

// ====== 登录 ======
export const getCaptcha = () => http.get('/login/captcha')
export const login = (data) => http.post('/login', data)
export const getAdminInfo = () => http.get('/login/info')

// ====== 公寓 ======
export const getApartmentPage = (params) => http.get('/apartment/page', { params })
export const getApartmentById = (id) => http.get(`/apartment/${id}`)
export const saveApartment = (data) => http.post('/apartment/save', data)
export const deleteApartment = (id) => http.delete(`/apartment/${id}`)
export const updateApartmentRelease = (id, isRelease) => http.put(`/apartment/${id}/release`, null, { params: { isRelease } })
export const getApartmentsByDistrict = (districtId) => http.get('/apartment/list-by-district', { params: { districtId } })

// ====== 房间 ======
export const getRoomPage = (params) => http.get('/room/page', { params })
export const getRoomById = (id) => http.get(`/room/${id}`)
export const saveRoom = (data) => http.post('/room/save', data)
export const deleteRoom = (id) => http.delete(`/room/${id}`)
export const updateRoomRelease = (id, isRelease) => http.put(`/room/${id}/release`, null, { params: { isRelease } })

// ====== 基础数据 ======
export const getPaymentTypes = () => http.get('/payment-type/list')
export const savePaymentType = (data) => http.post('/payment-type/save', data)
export const deletePaymentType = (id) => http.delete(`/payment-type/${id}`)

export const getLeaseTerms = () => http.get('/lease-term/list')
export const saveLeaseTerm = (data) => http.post('/lease-term/save', data)
export const deleteLeaseTerm = (id) => http.delete(`/lease-term/${id}`)

export const getLabels = (type) => http.get('/label/list', { params: { type } })
export const saveLabel = (data) => http.post('/label/save', data)
export const deleteLabel = (id) => http.delete(`/label/${id}`)

export const getFacilities = () => http.get('/facility/list')
export const saveFacility = (data) => http.post('/facility/save', data)
export const deleteFacility = (id) => http.delete(`/facility/${id}`)

export const getAttrKeys = () => http.get('/attr/key/list')
export const saveAttrKey = (data) => http.post('/attr/key/save', data)
export const deleteAttrKey = (id) => http.delete(`/attr/key/${id}`)
export const getAttrValues = (keyId) => http.get('/attr/value/list', { params: { keyId } })
export const saveAttrValue = (data) => http.post('/attr/value/save', data)
export const deleteAttrValue = (id) => http.delete(`/attr/value/${id}`)

// ====== 地区 ======
export const getProvinces = () => http.get('/district/province')
export const getCities = (parentId) => http.get('/district/city', { params: { parentId } })
export const getDistricts = (parentId) => http.get('/district/district', { params: { parentId } })

// ====== 预约 ======
export const getAppointmentPage = (params) => http.get('/appointment/page', { params })
export const updateAppointmentStatus = (id, status, cancelReason) =>
  http.put(`/appointment/${id}/status`, null, { params: { status, cancelReason } })

// ====== 租约 ======
export const getLeasePage = (params) => http.get('/lease/page', { params })
export const getLeaseById = (id) => http.get(`/lease/${id}`)
export const saveLease = (data) => http.post('/lease/save', data)
export const deleteLease = (id) => http.delete(`/lease/${id}`)
export const updateLeaseStatus = (id, status) => http.put(`/lease/${id}/status`, null, { params: { status } })

// ====== 用户 ======
export const getUserPage = (params) => http.get('/user/page', { params })
export const updateUserStatus = (id, status) => http.put(`/user/${id}/status`, null, { params: { status } })

// ====== 文件上传 ======
export const uploadFile = (file, dir = 'common') => {
  const fd = new FormData()
  fd.append('file', file)
  fd.append('dir', dir)
  return http.post('/file/upload', fd, { headers: { 'Content-Type': 'multipart/form-data' } })
}

export default http
