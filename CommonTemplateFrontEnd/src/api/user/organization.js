import request from '@/utils/request'

export function doGetOrganizationList() {
  return request({
    url: '/organization/organizationList',
    method: 'get'
  })
}
