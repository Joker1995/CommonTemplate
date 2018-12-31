import Cookies from 'js-cookie'

const TokenKey = 'request-token'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function getCookie(cookieKey) {
  return Cookies.get(cookieKey)
}

export function setCookie(cookieKey, cookieVal) {
  return Cookies.set(cookieKey, cookieVal)
}

export function removeCookie(cookieKey) {
  return Cookies.remove(cookieKey)
}
