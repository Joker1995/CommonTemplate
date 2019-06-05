export default function msgOfErrorCode(errorCode) {
  let msg = ''
  switch (errorCode) {
    case 1001: msg = 'Token失效,请重新登录,正在退出中......'; break
    case 1002: msg = '用户名和密码校验失败,请重新登录,正在退出中......'; break
    case 1003: msg = '接口未授权或无授权码,请重新登录,正在退出中......'; break
    case 1004: msg = '会话无效,请重新登录,正在退出中......'; break
    case 1007: msg = '在线会话超出限制,请稍候重试,正在退出中......'; break
    case 1008: msg = '参数检验失败,请重试'; break
  }
  return msg
}
