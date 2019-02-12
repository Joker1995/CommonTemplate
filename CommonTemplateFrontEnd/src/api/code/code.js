import request from '@/utils/request'

export function doGetDataSourceList() {
  return request({
    url: '/code/dataSource',
    method: 'get'
  })
}

export function doGetDataSourceTableList(data) {
  return request({
    url: '/code/dataSource/tables',
    method: 'post',
    data: data
  })
}

export function doGetGenerateCode(data, fileName) {
  return request.download('/code/generateCode', data, fileName)
}

export function doGetGenerateProject(data, fileName) {
  return request.download('/code/generateProject', data, fileName)
}

export function doRegisterDataSource(data) {
  return request({
    url: '/code/dataSource',
    method: 'post',
    data: data
  })
}

export function doRemoveDataSource(data) {
  return request({
    url: '/code/dataSource',
    method: 'delete',
    data: data
  })
}
