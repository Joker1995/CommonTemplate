import Vue from 'vue'
import Router from 'vue-router'

import Layout from '@/views/layout/Layout'
import User from '@/views/user/User'
import Role from '@/views/user/Role'
import Organization from '@/views/user/Organization'
import AccessPage from '@/views/user/AccessPage'
import Resource from '@/views/user/Resource'
import RegisterTask from '@/views/user/RegisterTask'

import Login from '@/views/logger/Login'
import Operation from '@/views/logger/Operation'

import Process from '@/views/process/Process'

import DataSource from '@/views/code/DataSource'
import Tables from '@/views/code/Tables'
import Project from '@/views/code/Project'

Vue.use(Router)

export const constantRouterMap = [
  { path: '/login', component: () => import('@/views/login/Login'), hidden: true },
  { path: '/register', component: () => import('@/views/login/Register'), hidden: true },
  { path: '/404', component: () => import('@/views/exception/404'), hidden: true },
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    name: 'Home',
    hidden: true,
    children: [{
      path: 'home',
      component: () => import('@/views/home/Home')
    }]
  }
]

export const asyncRouterMap = [
  {
    path: '/system',
    component: Layout,
    redirect: '/system/user',
    name: '系统设置',
    meta: { title: '系统设置', icon: 'system', permission: '/system' },
    children: [
      {
        path: 'user',
        name: 'User',
        component: User,
        meta: { title: '用户维护', icon: 'user', permission: '/system/user' }
      },
      {
        path: 'role',
        name: 'Role',
        component: Role,
        meta: { title: '角色维护', icon: 'role', permission: '/system/role' }
      },
      {
        path: 'organization',
        name: 'Organization',
        component: Organization,
        meta: { title: '部门维护', icon: 'organization', permission: '/system/organization' }
      },
      {
        path: 'accessPages',
        name: 'AccessPage',
        component: AccessPage,
        meta: { title: '页面权限维护', icon: 'accessPage', permission: '/system/accessPages' }
      },
      {
        path: 'resource',
        name: 'Resource',
        component: Resource,
        meta: { title: '接口权限维护', icon: 'resource', permission: '/system/resource' }
      },
      {
        path: 'registerTask',
        name: 'RegisterTask',
        component: RegisterTask,
        meta: { title: '注册任务审批', icon: 'registerTask', permission: '/system/registerTask' }
      },
      {
        path: 'process',
        name: 'Process',
        component: Process,
        meta: { title: '流程维护', icon: 'process', permission: '/system/process' }
      }
    ]
  },
  {
    path: '/logger',
    component: Layout,
    redirect: '/logger/operation',
    name: '日志查看',
    meta: { title: '日志查看', icon: 'logger', permission: '/logger' },
    children: [{
      path: 'operation',
      name: 'Operation',
      component: Operation,
      meta: { title: '操作日志', icon: 'operation', permission: '/logger/operation' }
    }, {
      path: 'login',
      name: 'Login',
      component: Login,
      meta: { title: '登陆日志', icon: 'login', permission: '/logger/login' }
    }]
  },
  {
    path: '/code',
    component: Layout,
    redirect: '/code/dataSource',
    name: '代码生成',
    meta: { title: '代码生成', icon: 'code', permission: '/code' },
    children: [{
      path: 'project',
      name: 'Prject',
      component: Project,
      meta: { title: '初始化项目', icon: 'project', permission: '/code/project' }
    }, {
      path: 'dataSource',
      name: 'DataSource',
      component: DataSource,
      meta: { title: '数据源', icon: 'dataSource', permission: '/code/dataSource' }
    }, {
      path: 'tables',
      name: 'Tables',
      component: Tables,
      meta: { title: '数据表', icon: 'dataSource', permission: '/code/tables' },
      hidden: true
    }]
  }
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})
