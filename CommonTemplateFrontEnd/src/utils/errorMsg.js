export default function handleErrorCode(errorCode) {
  const handle = { msg: '', reLogin: false }
  switch (errorCode) {
    case 1001: handle.msg = 'Token失效,请重新登录'; handle.reLogin = true; break
    case 1002: handle.msg = '用户名和密码校验失败,请重新登录'; handle.reLogin = true; break
    case 1003: handle.msg = '接口未授权或无授权码,请稍候重试'; break
    case 1004: handle.msg = '会话无效,请重新登录'; handle.reLogin = true; break
    case 1007: handle.msg = '在线会话超出限制,请稍候重试'; break
    case 1008: handle.msg = '参数检验失败,请重试'; break
    case 1009: handle.msg = '验证码校验失败,请重试'; break
    case 1010: handle.msg = '流程文件异常,请重试'; break
    case 1011: handle.msg = '注册用户名已存在或注册参数格式错误,请重试'; break
  }
  return handle
}
