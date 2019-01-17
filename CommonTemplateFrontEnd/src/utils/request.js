import axios from 'axios'
import { Message, MessageBox } from 'element-ui'
import store from '../store'
import { getToken, setToken } from '@/utils/cookie'

console.log('current environment:' + process.env.NODE_ENV + ',the mock mode status:' + process.env.USEING_MOCK)
if (process.env.USEING_MOCK) {
  require('@/mock/mock')
}

// 创建axios实例
const service = axios.create({
  baseURL: process.env.BASE_API, // api 的 base_url
  timeout: 5000 // 请求超时时间
})
service.all = ([...callback]) => {
  return axios.all([...callback]).then(axios.spread(function(...resp) {
    return [...resp]
  }))
}
service.download = (url, data, fileName) => {
  const param = {
    method: 'post',
    url: process.env.BASE_API + url, // 请求地址
    data: data, // 参数
    responseType: 'blob' // 表明返回服务器返回的数据类型
  }
  if (store.getters.token) {
    param.headers = {}
    param.headers['Authorization'] = getToken()// 让每个请求携带自定义token 请根据实际情况自行修改
    param.headers['Content-Type'] = 'application/json;charset=UTF-8'
  }
  return axios(param).then(response => {
    const content = response
    const blob = new Blob([content])
    if ('download' in document.createElement('a')) { // 非IE下载
      console.log('非IE下载')
      const url = window.URL.createObjectURL(new Blob([response.data]))
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', fileName) // or any other extension
      document.body.appendChild(link)
      link.click()
    } else { // IE10+下载
      console.log('IE10+下载')
      navigator.msSaveBlob(blob, fileName)
    }
  }, error => {
    // 下载错误时候处理
    console.log('error:' + error)
  }).catch((error) => {
    console.log('error:' + error)
  })
}
// request拦截器
service.interceptors.request.use(
  config => {
    if (store.getters.token) {
      config.headers['Authorization'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    }
    return config
  },
  error => {
    console.log(error) // for debug
    Promise.reject(error)
  }
)
// response 拦截器
service.interceptors.response.use(
  response => {
    const authorization = response.headers.authorization
    if (authorization) {
      console.log('authorization change to:' + authorization)
      setToken(authorization)
    }
    const headers = response.headers
    // 如果不是json请求,有可能为文件下载,请根据实际情况修改
    if (headers['content-type'] !== 'application/json;charset=UTF-8') {
      return response.data
    }
    const res = response.data
    if (res.code !== 200) {
      Message({
        message: res.msg,
        type: 'error',
        duration: 5 * 1000
      })
      if (res.code === 10001 || res.code === 10002 || res.code === 401) {
        // 10001非法token , 10002token过期
        MessageBox.confirm(
          '你已被登出，可以取消继续留在该页面，或者重新登录',
          '确定登出',
          {
            confirmButtonText: '重新登录',
            cancelButtonText: '取消',
            type: 'warning'
          }
        ).then(() => {
          store.dispatch('FedLogOut').then(() => {
            location.reload() // 为了重新实例化vue-router对象 避免bug
          })
        })
      }
      return Promise.reject('error')
    } else {
      return response.data
    }
  }, error => {
    console.log('err:' + error) // for debug
    console.log(error.response)
    const response = error.response
    Message({
      message: response.data.msg,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)
export default service
