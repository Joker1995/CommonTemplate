import axios from 'axios'
import { Message } from 'element-ui'
import store from '../store'
import { getToken, setToken } from '@/utils/cookie'
import msgOfErrorCode from '@/utils/errorMsg'

console.log('current environment:' + process.env.NODE_ENV + ',current baseUrl:' + process.env.BASE_API +
',the mock mode status:' + process.env.USEING_MOCK)
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
  }).catch((error) => {
    console.log('error:' + error)
    if (error.response && error.response.data &&
      error.response.data.type === 'application/json') {
      console.log(error.response)
      const data = error.response.data
      console.log(data)
      const filereader = new FileReader()
      filereader.onload = e => {
        const resultStr = e.target.result
        const resultObj = JSON.parse(resultStr)
        console.log(resultObj)
        if (resultObj.code && resultObj.msg) {
          const code = resultObj.code
          const message = resultObj.msg
          const msg = msgOfErrorCode(code)
          if (msg !== '') {
            if (getToken() !== '') {
              const msgComponent = Message({
                message: msg,
                type: 'error',
                duration: 5 * 1000
              })
              msgComponent.close = () => {
                store.dispatch('FedLogOut').then(() => {
                  const delayTime = Math.floor(Math.random() * 2000 + 3000)
                  setTimeout(() => {
                    const hashRouter = location.hash
                    if (hashRouter !== '/#/login') {
                      location.reload() // 为了重新实例化vue-router对象 避免bug
                    }
                  }, delayTime)
                })
              }
              setToken('')
            }
          } else {
            Message({
              message: message,
              type: 'error',
              duration: 5 * 1000
            })
          }
        }
      }
      filereader.readAsText(data)
    }
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
      return Promise.reject(response)
    } else {
      return response.data
    }
  }, error => {
    console.log('err:' + error) // for debug
    if (error && error.response && error.response.data) {
      console.log(error.response)
      const response = error.response
      const data = response.data
      const code = response.data.code
      const msg = msgOfErrorCode(code)
      if (msg !== '') {
        if (getToken() !== '') {
          const msgComponent = Message({
            message: msg,
            type: 'error',
            duration: 5 * 1000
          })
          msgComponent.close = () => {
            store.dispatch('FedLogOut').then(() => {
              const delayTime = Math.floor(Math.random() * 2000 + 3000)
              setTimeout(() => {
                const hashRouter = location.hash
                if (hashRouter !== '/#/login') {
                  location.reload() // 为了重新实例化vue-router对象 避免bug
                }
              }, delayTime)
            })
          }
          setToken('')
        }
      } else if (typeof (data.message) !== 'undefined') {
        Message({
          message: data.message,
          type: 'error',
          duration: 5 * 1000
        })
      } else {
        Message({
          message: data.msg,
          type: 'error',
          duration: 5 * 1000
        })
      }
    }
    return Promise.reject(error)
  }
)
export default service
