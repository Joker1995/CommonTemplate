import Mock from 'mockjs'
/* user界面假接口数据 */
Mock.mock(process.env.BASE_API + '/user/accessPages', 'get', {
  'code': 200,
  'data': {
    "accessPages": [
      {
        'id': 'f7d84f3059d547ea8dcfa3cc7b264fa3',
        'name': '角色维护',
        'url': '/system/role',
        'parentId': 'ea274f595fbd4476aade23c1e7d481ea',
        'memo': null,
        'subPageList': null
      },
      {
        'id': 'f935b89a12b64b1786a39236c17774be',
        'name': '部门维护',
        'url': '/system/organization',
        'parentId': 'ea274f595fbd4476aade23c1e7d481ea',
        'memo': null,
        'subPageList': null
      },
      {
        'id': 'aeeff46aee4f46b0b62d90486860e442',
        'name': '用户维护',
        'url': '/system/user',
        'parentId': 'ea274f595fbd4476aade23c1e7d481ea',
        'memo': null,
        'subPageList': null
      },
      {
        'id': 'ea274f595fbd4476aade23c1e7d481ea',
        'name': '组织维护',
        'url': '/system',
        'parentId': null,
        'memo': null,
        'subPageList': null
      },
      {
        'id': '7c8220f4af114dbf9d41308786fef51f',
        'name': '操作日志',
        'url': '/logger/operation',
        'parentId': '47e9f17990284132949af957e1a336d9',
        'memo': null,
        'subPageList': null
      },
      {
        'id': 'a4c521627a454054bc19fc45ceb1cdfb',
        'name': '登陆日志',
        'url': '/logger/login',
        'parentId': '47e9f17990284132949af957e1a336d9',
        'memo': null,
        'subPageList': null
      },
      {
        'id': '142cad4c42964648b6945a4f92a2084b',
        'name': '接口权限维护',
        'url': '/system/resource',
        'parentId': 'ea274f595fbd4476aade23c1e7d481ea',
        'memo': null,
        'subPageList': null
      },
      {
        'id': '47e9f17990284132949af957e1a336d9',
        'name': '日志查看',
        'url': '/logger',
        'parentId': null,
        'memo': null,
        'subPageList': null
      },
      {
        'id': '3bddc06ee3fc4d5aa23c95fdecc9bc3b',
        'name': '页面权限维护',
        'url': '/system/accessPage',
        'parentId': 'ea274f595fbd4476aade23c1e7d481ea',
        'memo': null,
        'subPageList': null
      },
      {
        'id': '12e9f17990284132949af957e1a336d9',
        'name': '代码生成',
        'url': '/code',
        'parentId': null,
        'memo': null,
        'subPageList': null
      },
      {
        'id': '13e9f17990284132949af957e1a336d9',
        'name': '数据源',
        'url': '/code/dataSource',
        'parentId': '12e9f17990284132949af957e1a336d9',
        'memo': null,
        'subPageList': null
      },
      {
        'id': '096673c87ed347879ac51272ced4bbe3',
        'name': '数据表',
        'url': '/code/tales',
        'parentId': '12e9f17990284132949af957e1a336d9',
        'memo': null,
        'subPageList': null
      },
      {
        'id': 'e701bed5816746f897a1dc578ccb7474',
        'name': '生成项目',
        'url': '/code/project',
        'parentId': '12e9f17990284132949af957e1a336d9',
        'memo': null,
        'subPageList': null
      }
    ]
  }
})

Mock.mock(process.env.BASE_API + '/user/logout', 'post', {
  'code': 200,
  'data': {}
})

Mock.mock(process.env.BASE_API + '/user/userInfo', 'get', {
  'code': 200,
  'data': {
    'name': 'admin',
    'avatar': 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif'
  }
})

Mock.mock(process.env.BASE_API + '/user/login', 'post', {
  'code': 200,
  'data': 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6ImFkbWluIiwiZXhwIjoxNTQzODIwMTg1fQ.eKjhE_1obZUZz-a_nVEsYXCghjUZbXRFwI95uGeiUSQ'
})

Mock.mock(process.env.BASE_API + '/user/register', 'post', {
  'code': 200,
  'data': ''
})

Mock.mock(process.env.BASE_API + '/user/', 'post', {
  'code': 200,
  'data': {}
})

Mock.mock(process.env.BASE_API + '/user/', 'put', {
  'code': 200,
  'data': {}
})

Mock.mock(process.env.BASE_API + '/user/organizationList', 'get', {
  'code': 200,
  'data': [{ value: 0, label: '全省' }, { value: 15, label: '省公司' },
    { value: 16, label: '传送网络运营中心' }, { value: 17, label: '网络运营部' },
    { value: 19, label: '汕头海缆站' }, { value: 20, label: '综合部' },
    { value: 21, label: '中心领导' }, { value: 22, label: '网络优化项目部' },
    { value: 23, label: '高端装维项目部' }, { value: 24, label: '应急响应和网络优化支撑中心' },
    { value: 25, label: '客户服务部' }, { value: 26, label: '安全保卫部' },
    { value: 27, label: '纪检监察部/党群工作部' }, { value: 28, label: '工会' },
    { value: 29, label: '汕头分公司' }]
})

Mock.mock(process.env.BASE_API + '/user/roleList', 'get', {
  'code': 200,
  'data': [{ value: '205e04601efb4e90bd1cab77e131420b', label: '系统管理员' },
    { value: '205e04601efb4e90bd1cab77e131420a', label: '帐号管理员' },
    { value: '205e04601efb4e90bd1cab77e131420c', label: '普通用户' },
    { value: 4, label: '移动端用户' }]
})

Mock.mock(process.env.BASE_API + '/user/resourceList', 'get', {
  'code': 200,
  'data': [
    {
      id: 'f7d84f3059d547ea8dcfa3cc7b264fa3',
      name: '角色维护',
      url: '/system/role',
      parentId: 'ea274f595fbd4476aade23c1e7d481ea',
      memo: null
    },
    {
      id: 'f935b89a12b64b1786a39236c17774be',
      name: '部门维护',
      url: '/system/organization',
      parentId: 'ea274f595fbd4476aade23c1e7d481ea',
      memo: null
    },
    {
      id: 'aeeff46aee4f46b0b62d90486860e442',
      name: '用户维护',
      url: '/system/user',
      parentId: 'ea274f595fbd4476aade23c1e7d481ea',
      memo: null,
      subPageList: null
    },
    {
      id: 'ea274f595fbd4476aade23c1e7d481ea',
      name: '组织维护',
      url: '/system',
      parentId: null,
      memo: null,
      subPageList: null
    },
    {
      id: '7c8220f4af114dbf9d41308786fef51f',
      name: '操作日志',
      url: '/logger/operation',
      parentId: '47e9f17990284132949af957e1a336d9',
      memo: null
    },
    {
      id: 'a4c521627a454054bc19fc45ceb1cdfb',
      name: '登陆日志',
      url: '/logger/login',
      parentId: '47e9f17990284132949af957e1a336d9',
      memo: null
    },
    {
      id: '142cad4c42964648b6945a4f92a2084b',
      name: '接口权限维护',
      url: '/system/resource',
      parentId: 'ea274f595fbd4476aade23c1e7d481ea',
      memo: null
    },
    {
      id: '47e9f17990284132949af957e1a336d9',
      name: '日志查看',
      url: '/logger',
      parentId: null,
      memo: null
    },
    {
      id: '3bddc06ee3fc4d5aa23c95fdecc9bc3b',
      name: '页面权限维护',
      url: '/system/accessPage',
      parentId: 'ea274f595fbd4476aade23c1e7d481ea',
      memo: null
    }
  ]
})

Mock.mock(process.env.BASE_API + '/user/usersList', 'get', {
  'code': 200,
  'msg': '所有用户',
  'data': {
    'pageNum': 1,
    'pageSize': 16,
    'size': 16,
    'orderBy': null,
    'startRow': 0,
    'endRow': 15,
    'total': 16,
    'pages': 1,
    'list': [
      {
        'id': '0076a75c65874d3baffb526409bfe951',
        'name': 'test2',
        'password': null,
        'label': '管理员',
        'status': 'S0A',
        'mobilePhone': null,
        'sex': null,
        'photo': null,
        'memo': null,
        'effDate': null,
        'expDate': null,
        'roles': null,
        'resources': null
      },
      {
        'id': '1b3074af7bf644109603539e85dd3402',
        'name': 'test13',
        'password': null,
        'label': '管理员',
        'status': 'S0A',
        'mobilePhone': null,
        'sex': null,
        'photo': null,
        'memo': null,
        'effDate': null,
        'expDate': null,
        'roles': null,
        'resources': null
      },
      {
        'id': '1e0d950aca374099875dd847fe559fee',
        'name': 'test5',
        'password': null,
        'label': '管理员',
        'status': 'S0A',
        'mobilePhone': null,
        'sex': null,
        'photo': null,
        'memo': null,
        'effDate': null,
        'expDate': null,
        'roles': null,
        'resources': null
      },
      {
        'id': '2770c3d17e6e4b218eeaf8e4489a368a',
        'name': 'test4',
        'password': null,
        'label': '管理员',
        'status': 'S0A',
        'mobilePhone': null,
        'sex': null,
        'photo': null,
        'memo': null,
        'effDate': null,
        'expDate': null,
        'roles': null,
        'resources': null
      },
      {
        'id': '3280ee36b0c9409098bdccbaf675e6b1',
        'name': 'test1',
        'password': null,
        'label': '管理员',
        'status': 'S0A',
        'mobilePhone': null,
        'sex': null,
        'photo': null,
        'memo': null,
        'effDate': null,
        'expDate': null,
        'roles': null,
        'resources': null
      },
      {
        'id': '4e3fa47a599943cab25bf1507bcf7acc',
        'name': 'test',
        'password': null,
        'label': '测试',
        'status': 'S0A',
        'mobilePhone': null,
        'sex': null,
        'photo': null,
        'memo': null,
        'effDate': null,
        'expDate': null,
        'roles': null,
        'resources': null
      },
      {
        'id': '5c7b1fc18fe94b67a89630d89c480ca6',
        'name': 'test11',
        'password': null,
        'label': '管理员',
        'status': 'S0A',
        'mobilePhone': null,
        'sex': null,
        'photo': null,
        'memo': null,
        'effDate': null,
        'expDate': null,
        'roles': null,
        'resources': null
      },
      {
        'id': '600b8c980eca4716aac43e7e124ec4d2',
        'name': 'test3',
        'password': null,
        'label': '管理员',
        'status': 'S0A',
        'mobilePhone': null,
        'sex': null,
        'photo': null,
        'memo': null,
        'effDate': null,
        'expDate': null,
        'roles': null,
        'resources': null
      },
      {
        'id': '9a1e066af6404e4ca182879a9758802d',
        'name': 'test6',
        'password': null,
        'label': '管理员',
        'status': 'S0A',
        'mobilePhone': null,
        'sex': null,
        'photo': null,
        'memo': null,
        'effDate': null,
        'expDate': null,
        'roles': null,
        'resources': null
      }
    ],
    'prePage': 0,
    'nextPage': 0,
    'isFirstPage': true,
    'isLastPage': true,
    'hasPreviousPage': false,
    'hasNextPage': false,
    'navigatePages': 8,
    'navigatepageNums': [1],
    'navigateFirstPage': 1,
    'navigateLastPage': 1,
    'firstPage': 1,
    'lastPage': 1
  },
  'jsonMap': null
})

Mock.mock(process.env.BASE_API + '/user/resource/1', 'get', {
  'code': 200,
  'data': [
    {
      'id': 'f7d84f3059d547ea8dcfa3cc7b264fa3',
      'name': '角色维护',
      'url': '/system/role',
      'parentId': 'ea274f595fbd4476aade23c1e7d481ea',
      'memo': null,
      'subPageList': null
    },
    {
      'id': 'aeeff46aee4f46b0b62d90486860e442',
      'name': '用户维护',
      'url': '/system/user',
      'parentId': 'ea274f595fbd4476aade23c1e7d481ea',
      'memo': null,
      'subPageList': null
    },
    {
      'id': '7c8220f4af114dbf9d41308786fef51f',
      'name': '操作日志',
      'url': '/logger/operation',
      'parentId': '47e9f17990284132949af957e1a336d9',
      'memo': null,
      'subPageList': null
    },
    {
      'id': 'a4c521627a454054bc19fc45ceb1cdfb',
      'name': '登陆日志',
      'url': '/logger/login',
      'parentId': '47e9f17990284132949af957e1a336d9',
      'memo': null,
      'subPageList': null
    },
    {
      'id': '142cad4c42964648b6945a4f92a2084b',
      'name': '接口权限维护',
      'url': '/system/resource',
      'parentId': 'ea274f595fbd4476aade23c1e7d481ea',
      'memo': null,
      'subPageList': null
    },
    {
      'id': '47e9f17990284132949af957e1a336d9',
      'name': '日志查看',
      'url': '/logger',
      'parentId': null,
      'memo': null,
      'subPageList': null
    },
    {
      'id': '3bddc06ee3fc4d5aa23c95fdecc9bc3b',
      'name': '页面权限维护',
      'url': '/system/accessPage',
      'parentId': 'ea274f595fbd4476aade23c1e7d481ea',
      'memo': null,
      'subPageList': null
    }
  ]
})

Mock.mock(process.env.BASE_API + '/user/role/1', 'get', {
  'code': 200,
  'data': [
    {
      'id': '205e04601efb4e90bd1cab77e131420b',
      'name': 'admin',
      'label': '系统管理员',
      'memo': null,
      'accessPageList': null,
      'resourceList': null,
      'accessPageIds': null,
      'resourceIds': null
    }
  ]
})

Mock.mock(process.env.BASE_API + '/user/accessPage/1', 'get', {
  'code': 200,
  'data': [
    {
      'id': 'f7d84f3059d547ea8dcfa3cc7b264fa3',
      'name': '角色维护',
      'url': '/system/role',
      'parentId': 'ea274f595fbd4476aade23c1e7d481ea',
      'memo': null,
      'subPageList': null
    },
    {
      'id': 'f935b89a12b64b1786a39236c17774be',
      'name': '部门维护',
      'url': '/system/organization',
      'parentId': 'ea274f595fbd4476aade23c1e7d481ea',
      'memo': null,
      'subPageList': null
    },
    {
      'id': 'aeeff46aee4f46b0b62d90486860e442',
      'name': '用户维护',
      'url': '/system/user',
      'parentId': 'ea274f595fbd4476aade23c1e7d481ea',
      'memo': null,
      'subPageList': null
    },
    {
      'id': 'ea274f595fbd4476aade23c1e7d481ea',
      'name': '组织维护',
      'url': '/system',
      'parentId': null,
      'memo': null,
      'subPageList': null
    },
    {
      'id': '7c8220f4af114dbf9d41308786fef51f',
      'name': '操作日志',
      'url': '/logger/operation',
      'parentId': '47e9f17990284132949af957e1a336d9',
      'memo': null,
      'subPageList': null
    },
    {
      'id': 'a4c521627a454054bc19fc45ceb1cdfb',
      'name': '登陆日志',
      'url': '/logger/login',
      'parentId': '47e9f17990284132949af957e1a336d9',
      'memo': null,
      'subPageList': null
    },
    {
      'id': '142cad4c42964648b6945a4f92a2084b',
      'name': '接口权限维护',
      'url': '/system/resource',
      'parentId': 'ea274f595fbd4476aade23c1e7d481ea',
      'memo': null,
      'subPageList': null
    },
    {
      'id': '47e9f17990284132949af957e1a336d9',
      'name': '日志查看',
      'url': '/logger',
      'parentId': null,
      'memo': null,
      'subPageList': null
    },
    {
      'id': '3bddc06ee3fc4d5aa23c95fdecc9bc3b',
      'name': '页面权限维护',
      'url': '/system/accessPage',
      'parentId': 'ea274f595fbd4476aade23c1e7d481ea',
      'memo': null,
      'subPageList': null
    }
  ]
})

Mock.mock(process.env.BASE_API + '/user/role', 'put', {
  'code': 200,
  'data': {}
})

Mock.mock(process.env.BASE_API + '/user/resource', 'put', {
  'code': 200,
  'data': {}
})

Mock.mock(process.env.BASE_API + '/user/accessPage', 'put', {
  'code': 200,
  'data': {}
})
