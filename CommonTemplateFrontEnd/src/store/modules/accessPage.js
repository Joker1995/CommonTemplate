import { doGetAccessPage } from '@/api/user/user'
import { constantRouterMap, asyncRouterMap } from '@/router'

/**
 * 通过meta.permission判断是否与当前用户权限匹配
 * @param roles
 * @param route
 */
function hasPermission(accessPages, route) {
  if (route.meta && route.meta.permission) {
    return accessPages.some(page => route.meta.permission.includes(page))
  } else {
    return false
  }
}

/**
 * 递归过滤异步路由表，返回符合用户角色权限的路由表
 * @param routes asyncRouterMap
 * @param roles
 */
function filterAsyncRouter(routes, accessPages) {
  const res = []
  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(accessPages, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRouter(tmp.children, accessPages)
      }
      res.push(tmp)
    }
  })
  return res
}

const accessPage = {
  state: {
    accessPages: constantRouterMap,
    addRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers
      state.accessPages = constantRouterMap.concat(routers)
    }
  },
  actions: {
    FetchAccessPages({ commit }) {
      return new Promise((resolve, reject) => {
        doGetAccessPage().then(resp => {
          const accessPageData = resp.data
          const userAccessRouter = []
          for (const item of accessPageData) {
            userAccessRouter.push(item.url)
          }
          const accessedRouters = filterAsyncRouter(asyncRouterMap, userAccessRouter)
          commit('SET_ROUTERS', accessedRouters)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}
export default accessPage
