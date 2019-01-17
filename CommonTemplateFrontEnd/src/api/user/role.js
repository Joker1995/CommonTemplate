import request from '@/utils/request'

export function doCreateRole(role) {
  return request({
    url: '/role/',
    method: 'post',
    data: role
  })
}

export function doUpdateRole(role) {
  return request({
    url: '/role/',
    method: 'put',
    data: role
  })
}

export function doGetRoleList(query) {
  return request({
    url: '/role/roleList',
    method: 'post',
    data: query
  })
}

export function doGetRoleAccessPageList(roleId) {
  return request({
    url: '/role/accessPage/' + roleId,
    method: 'get'
  })
}

export function doUpdateAccessPageList(requestParam) {
  return request({
    url: '/role/accessPage',
    method: 'put',
    data: requestParam
  })
}

export function doUpdateResourceList(requestParam) {
  return request({
    url: '/role/resource',
    method: 'put',
    data: requestParam
  })
}

export function doDeleteRole(role) {
  return request({
    url: '/role',
    method: 'delete',
    data: role
  })
}
