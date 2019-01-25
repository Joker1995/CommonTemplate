import request from '@/utils/request'
import { getEncyptSalt } from '@/utils'
import md5 from 'js-md5'

export function doLogin(userName, password) {
  const encyptPassword = md5(md5(password + getEncyptSalt()))
  return request({
    url: '/user/login',
    method: 'post',
    params: {
      userName: userName,
      password: encyptPassword
    }
  })
}

export function doRegister(username, password) {
  const encyptPassword = md5(md5(password + getEncyptSalt()))
  return request({
    url: '/user/register',
    method: 'post',
    data: {
      username,
      encyptPassword
    }
  })
}

export function doGetUserInfo() {
  return request({
    url: '/user/userInfo',
    method: 'get'
  })
}

export function doLogout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}

export function doGetAccessPage() {
  return request({
    url: '/user/accessPages',
    method: 'get'
  })
}

export function doGetUserList(query) {
  return request({
    url: '/user/usersList',
    method: 'post',
    data: query
  })
}

export function doCreateUser() {
  return request({
    url: '/user/',
    method: 'post'
  })
}

export function doUpdateUser(userData) {
  return request({
    url: '/user/',
    method: 'put',
    data: userData
  })
}

export function doGetOrganizationList() {
  return request({
    url: '/user/organization',
    method: 'get'
  })
}

export function doGetRoleList() {
  return request({
    url: '/user/roleList',
    method: 'get'
  })
}

export function doGetResourceList() {
  return request({
    url: '/user/resourceList',
    method: 'get'
  })
}

export function doGetUserResourcesList(id) {
  return request({
    url: '/user/resource/' + id,
    method: 'get'
  })
}

export function doGetUserRolesList(id) {
  return request({
    url: '/user/role/' + id,
    method: 'get'
  })
}

export function doGetUserAccessPagesList(userId) {
  return request({
    url: '/user/accessPage/' + userId,
    method: 'get'
  })
}

export function doUpdateUserRoleList(userId, roleIdList) {
  const requestParam = {}
  requestParam.id = userId
  requestParam.roleIds = roleIdList
  return request({
    url: '/user/role',
    method: 'put',
    data: requestParam
  })
}

export function doUpdateUserResourceList(userId, resouceIdList) {
  const requestParam = {}
  requestParam.id = userId
  requestParam.resourceIds = resouceIdList
  return request({
    url: '/user/resource',
    method: 'put',
    data: requestParam
  })
}

export function doUpdateUserAccessPageList(userId, accessPageIdList) {
  const requestParam = {}
  requestParam.id = userId
  requestParam.accessPageIds = accessPageIdList
  return request({
    url: '/user/accessPage',
    method: 'put',
    data: requestParam
  })
}

export function doGetUserTokenList(query) {
  return request({
    url: '/user/ssoToken',
    method: 'post',
    data: query
  })
}

export function doKickOutUserToken(query) {
  return request({
    url: '/user/ssoToken/kickOut',
    method: 'post',
    data: query
  })
}

export function doRollBackUserToken(query) {
  return request({
    url: '/user/ssoToken/rollBack',
    method: 'post',
    data: query
  })
}

export function doDownloadUserList(data, fileName) {
  return request.download('/user/downloadUserList', data, fileName)
}
