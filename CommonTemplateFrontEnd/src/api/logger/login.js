import request from '@/utils/request'

export function getLoginLoggerList() {
  return request({
    url: '/logger/loginList',
    method: 'get'
  })
}
