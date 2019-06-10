/* 校验验证码 */
export function validateCaptcha(captchaVal) {
  if (!captchaVal) {
    return false
  } else if (captchaVal && captchaVal.length !== 4) {
    return false
  } else {
    return true
  }
}

/* 校验邮箱 */
export function validateEmail(emailVal) {
  if (!emailVal) {
    return false
  } else if (!emailVal.includes('@')) {
    return false
  } else {
    return true
  }
}

/* 校验密码 */
export function validatePwd(pwdVal) {
  let num = 0
  const rule1 = /\d+/
  const rule2 = /[a-z]+/
  const rule3 = /[A-Z]+/
  const rule4 = /[~!@#\$%^&*\{\};,.\?\/'"]/
  const rule5 = /^.{8,16}$/

  const flag1 = rule1.test(pwdVal)
  const flag2 = rule2.test(pwdVal)
  const flag3 = rule3.test(pwdVal)
  const flag4 = rule4.test(pwdVal)
  const flag5 = rule5.test(pwdVal)
  if (flag1) {
    num = num + 1
  }
  if (flag2) {
    num = num + 1
  }
  if (flag3) {
    num = num + 1
  }
  if (flag4) {
    num = num + 1
  }
  if (flag5) {
    num = num + 1
  }
  if (num > 2 && flag5) {
    return true
  }
  return false
}
/* 合法uri*/
export function validateURL(textval) {
  const urlregex = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return urlregex.test(textval)
}

/* 小写字母*/
export function validateLowerCase(str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/* 大写字母*/
export function validateUpperCase(str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/* 大小写字母*/
export function validatAlphabets(str) {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}
