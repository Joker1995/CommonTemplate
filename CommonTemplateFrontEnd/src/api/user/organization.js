import request from '@/utils/request'

export function doGetOrganizationList(query) {
  return request({
    url: '/organization/organizationList',
    method: 'post',
    data: query
  })
}

export function doCreateOrganization(data) {
  return request({
    url: '/organization',
    method: 'post',
    data: data
  })
}

export function doUpdateOrganization(data) {
  return request({
    url: '/organization',
    method: 'put',
    data: data
  })
}

export function doDeleteOrganization(data) {
  return request({
    url: '/organization',
    method: 'delete',
    data: data
  })
}
