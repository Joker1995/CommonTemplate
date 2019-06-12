import request from '@/utils/request'
import { getEncyptSalt } from '@/utils'
import md5 from 'js-md5'

export function doLogin(userName, password, captcha, captchaToken) {
  const encyptPassword = md5(md5(password + getEncyptSalt()))
  return request({
    url: '/user/login',
    method: 'post',
    params: {
      userName: userName,
      password: encyptPassword,
      captcha: captcha,
      captchaToken: captchaToken
    }
  })
}

export function doRegister(username, password, email) {
  const encyptPassword = md5(md5(password + getEncyptSalt()))
  return request({
    url: '/user/register',
    method: 'post',
    params: {
      userName: username,
      password: encyptPassword,
      email: email
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

export function doGetUserList(query) {
  return request({
    url: '/user/usersList',
    method: 'post',
    data: query
  })
}

export function doCreateUser(data) {
  return request({
    url: '/user/',
    method: 'post',
    data: data
  })
}

export function doUpdateUser(data) {
  return request({
    url: '/user/',
    method: 'put',
    data: data
  })
}

export function doDeleteUser(data) {
  return request({
    url: '/user/',
    method: 'delete',
    data: data
  })
}

export function doGetAccessPage() {
  return request({
    url: '/user/accessPages',
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

export function doUpdateSelfPwd(data) {
  return request({
    url: '/user/self/password',
    method: 'post',
    data: data
  })
}

export function doUpdateUserPwd(data) {
  return request({
    url: '/user/' + data.id + '/password',
    method: 'post',
    data: data
  })
}

export function doGetCaptcha() {
  return request({
    url: '/user/captcha',
    method: 'get'
  })
}

export function doGetRegisterTask() {
  return request({
    url: '/user/registerTask',
    method: 'get'
  })
}

export function doApprovalRegisterTask(data) {
  return request({
    url: '/user/approvalRegisterTask',
    method: 'post',
    params: data
  })
}
