import request from '@/utils/request'

export function doGetDataSourceList() {
  return request({
    url: '/code/dataSource',
    method: 'get'
  })
}

export function doGetDataSourceTableList() {
  return request({
    url: '/code/dataSource/tables',
    method: 'get'
  })
}

export function doGetGenerateCode(params) {
  return request({
    url: '/code/generate',
    method: 'get',
    data: params
  })
}

export function doGetGenerateProject(params) {
  return request({
    url: '/code/generateProject',
    method: 'get',
    data: params
  })
}
