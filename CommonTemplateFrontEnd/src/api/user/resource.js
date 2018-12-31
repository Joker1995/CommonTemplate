import request from '@/utils/request'

export function doGetResourceChildNodes(id) {
  return request({
    url: '/resource/' + id + '/childList',
    method: 'get'
  })
}

export function doGetResourceList(query) {
  return request({
    url: '/resource/resources',
    method: 'post',
    data: query
  })
}

export function doCreateResource(resource) {
  return request({
    url: '/resource',
    method: 'post',
    data: resource
  })
}

export function doUpdateResource(resource) {
  return request({
    url: '/resource',
    method: 'put',
    data: resource
  })
}

export function doDeleteResource(resource) {
  return request({
    url: '/resource',
    method: 'delete',
    data: resource
  })
}
