import request from '@/utils/request'

export function doGetProcessList(query) {
  return request({
    url: '/process/processList',
    method: 'post',
    data: query
  })
}

export function doDeployProcess(data) {
  return request({
    url: '/process/deploy',
    method: 'post',
    params: data
  })
}

export function doUnDeployProcess(data) {
  return request({
    url: '/process/undeploy',
    method: 'post',
    params: data
  })
}

export function doPreviewProcessImg(data) {
  return request({
    url: '/process/preview',
    method: 'post',
    params: data
  })
}
