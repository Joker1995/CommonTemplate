import request from '@/utils/request'

export function doGetResourceChildNodes(id, query) {
  return request({
    url: '/resource/' + id + '/childList',
    method: 'post',
    data: query
  })
}

export function doGetResourceList(query) {
  return request({
    url: '/resource/resources',
    method: 'post',
    data: query
  })
}

export function doCreateResource(data) {
  return request({
    url: '/resource',
    method: 'post',
    data: data
  })
}

export function doUpdateResource(data) {
  return request({
    url: '/resource',
    method: 'put',
    data: data
  })
}

export function doDeleteResource(data) {
  return request({
    url: '/resource',
    method: 'delete',
    data: data
  })
}
