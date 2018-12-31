import Mock from 'mockjs'

/* permission界面假接口数据 */
Mock.mock(process.env.BASE_API + '/resource/1/childList', 'get', {
  'code': 200,
  'data': {
    items: [{
      id: 1,
      name: '用户维护',
      url: '/user/users'
    }, {
      id: 2,
      name: '页面权限维护',
      url: '/permission/permissions'
    }, {
      id: 3,
      name: '接口权限维护',
      url: '/resource/resources'
    }, {
      id: 4,
      name: '角色维护',
      url: '/role/roles'
    }, {
      id: 4,
      name: '部门维护',
      url: '/organization/organizations'
    }],
    total: 4
  }
})

Mock.mock(process.env.BASE_API + '/resource/resources', 'get', {
  'code': 200,
  'data': [{
    id: 1,
    label: '组织维护',
    children: [{
      id: 2,
      label: '用户维护'
    }, {
      id: 3,
      label: '页面权限维护'
    }, {
      id: 4,
      label: '接口权限维护'
    }, {
      id: 5,
      label: '角色维护'
    }, {
      id: 6,
      label: '部门维护'
    }]
  }, {
    id: 6,
    label: '日志查看',
    children: [{
      id: 7,
      label: '操作日志'
    }, {
      id: 8,
      label: '登陆日志'
    }]
  }]
})

Mock.mock(process.env.BASE_API + '/resource', 'post', {
  'code': 200,
  'data': {}
})

Mock.mock(process.env.BASE_API + '/resource', 'put', {
  'code': 200,
  'data': {}
})

Mock.mock(process.env.BASE_API + '/resource', 'delete', {
  'code': 200,
  'data': {}
})
