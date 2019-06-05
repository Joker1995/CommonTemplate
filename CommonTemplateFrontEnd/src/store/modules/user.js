import { getToken, setToken, removeToken } from '@/utils/cookie'
import { doLogin, doRegister, doGetUserInfo, doLogout } from '@/api/user/user'

const user = {
  state: {
    avatar: '',
    name: '',
    password: '',
    token: getToken()
  },
  mutations: {
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_PASSWORD: (state, password) => {
      state.password = password
    },
    SET_ACCESS_PAGES: (state, accessPage) => {
      state.accessPage = accessPage
    }
  },
  actions: {
    // 登陆
    Login({ commit }, userInfo) {
      const username = userInfo.username.trim()
      return new Promise((resolve, reject) => {
        doLogin(username, userInfo.password, userInfo.captcha, userInfo.captchaToken).then(resp => {
          const data = resp.data
          setToken(data)
          commit('SET_TOKEN', data)
          commit('SET_NAME', username)
          commit('SET_PASSWORD', userInfo.password)
          resolve()
        }, error => {
          reject(error)
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 注册
    Register({ commit }, userInfo) {
      const username = userInfo.username.trim()
      return new Promise((resolve, reject) => {
        doRegister(username, userInfo.password).then(resp => {
          commit('SET_NAME', username)
          commit('SET_PASSWORD', userInfo.password)
          resolve()
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        doGetUserInfo(state.token).then(response => {
          const data = response.data
          commit('SET_NAME', data.name)
          commit('SET_AVATAR', 'http://tx.haiqq.com/uploads/allimg/170507/104F41W9-1.jpg')
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    LogOut({ commit, state }) { // 登出
      return new Promise((resolve, reject) => {
        doLogout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          removeToken()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    FedLogOut({ commit }) { // 前端登出
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    }
  }
}
export default user
