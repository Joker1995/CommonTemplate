import Mock from 'mockjs'

/* role界面假接口数据 */
Mock.mock(process.env.BASE_API + '/role/roleList', 'get', {
  'code': 200,
  'msg': '',
  'data': {
    'pageNum': 1,
    'pageSize': 1,
    'size': 1,
    'orderBy': null,
    'startRow': 0,
    'endRow': 0,
    'total': 1,
    'pages': 1,
    'list': [
      {
        'id': '205e04601efb4e90bd1cab77e131420b',
        'name': 'admin',
        'label': '管理员',
        'memo': null,
        'accessPageList': [
          {
            'id': '142cad4c42964648b6945a4f92a2084b',
            'name': '接口权限维护',
            'url': '/system/resource',
            'parentId': 'ea274f595fbd4476aade23c1e7d481ea',
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
            'id': '47e9f17990284132949af957e1a336d9',
            'name': '日志查看',
            'url': '/logger',
            'parentId': null,
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
          }
        ],
        'resourceList': [
          {
            'id': 'f7d84f3059d547ea8dcfa3cc7b264fa3',
            'name': 'roleDetail',
            'label': '角色详情',
            'url': '/roles/*',
            'parentId': null,
            'memo': null
          },
          {
            'id': 'aeeff46aee4f46b0b62d90486860e442',
            'name': 'userList',
            'label': '用户列表',
            'url': '/user/usersList',
            'parentId': null,
            'memo': null
          },
          {
            'id': 'f7d84f3059d547ea8dcfa3cc7b264fa3',
            'name': 'roleList',
            'label': '角色列表',
            'url': '/roles/rolesList',
            'parentId': null,
            'memo': null
          }
        ],
        'accessPageIds': null,
        'resourceIds': null
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
  }
})

Mock.mock(process.env.BASE_API + '/role/accessPageList', 'get', {
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

Mock.mock(process.env.BASE_API + '/role/resourceList', 'get', {
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

Mock.mock(process.env.BASE_API + '/role/resource/1', 'get', {
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

Mock.mock(process.env.BASE_API + '/role/accessPage/1', 'get', {
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

Mock.mock(process.env.BASE_API + '/role/resource/1', 'put', {
  'code': 200,
  'data': {}
})

Mock.mock(process.env.BASE_API + '/role/accessPage/1', 'put', {
  'code': 200,
  'data': {}
})

Mock.mock(process.env.BASE_API + '/role', 'delete', {
  'code': 200,
  'data': {}
})
