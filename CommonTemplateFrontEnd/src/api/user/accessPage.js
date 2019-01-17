import request from '@/utils/request'

export function doGetAccessPageChildNodes(parentId, query) {
  return request({
    url: '/accessPage/' + parentId + '/childList',
    method: 'post',
    data: query
  })
}

export function doGetAccessPageList(query) {
  return request({
    url: '/accessPage/accessPages',
    method: 'post',
    data: query
  })
}

export function doCreateAccessPage(accessPage) {
  return request({
    url: '/accessPage',
    method: 'post',
    data: accessPage
  })
}

export function doDeleteAccessPage(accessPage) {
  return request({
    url: '/accessPage',
    method: 'delete',
    data: accessPage
  })
}

export function doUpdateAccessPage(accessPage) {
  return request({
    url: '/accessPage',
    method: 'put',
    data: accessPage
  })
}
