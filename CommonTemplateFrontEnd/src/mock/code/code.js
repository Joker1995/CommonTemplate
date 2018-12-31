import Mock from 'mockjs'

Mock.mock(process.env.BASE_API + '/code/dataSource', 'get', {
  'code': 200,
  'msg': 'query ApplicationContext dataSourceList success',
  'data': [
    {
      'password': '123',
      'jdbcUrl': 'jdbc:mysql://localhost:3306/ssm_shiro?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true',
      'dataBaseName': 'masterDatasource',
      'userName': 'root'
    }
  ]
})

Mock.mock(process.env.BASE_API + '/code/dataSource/tables', 'get', {
  'code': 200,
  'msg': 'queryDataBaseTables success',
  'data': [
    {
      'jdbcName': 'sys_devices',
      'comment': '用户设备表',
      'columnNames': []
    },
    {
      'jdbcName': 'sys_dicts',
      'comment': '系统字典表',
      'columnNames': []
    },
    {
      'jdbcName': 'sys_logger_login',
      'comment': '登陆日志表',
      'columnNames': []
    },
    {
      'jdbcName': 'sys_logger_operation',
      'comment': '操作记录表',
      'columnNames': []
    },
    {
      'jdbcName': 'sys_organizations',
      'comment': '部门表',
      'columnNames': []
    },
    {
      'jdbcName': 'sys_pages',
      'comment': '前端界面权限表',
      'columnNames': []
    },
    {
      'jdbcName': 'sys_resources',
      'comment': '接口权限表',
      'columnNames': []
    },
    {
      'jdbcName': 'sys_role_pages',
      'comment': '角色授权前端界面表',
      'columnNames': []
    },
    {
      'jdbcName': 'sys_role_resources',
      'comment': '角色授权接口权限表',
      'columnNames': []
    },
    {
      'jdbcName': 'sys_roles',
      'comment': '角色表',
      'columnNames': []
    },
    {
      'jdbcName': 'sys_user_pages',
      'comment': '用户授权前端界面表',
      'columnNames': []
    },
    {
      'jdbcName': 'sys_user_resources',
      'comment': '用户授权接口权限表',
      'columnNames': []
    },
    {
      'jdbcName': 'sys_user_roles',
      'comment': '用户授权角色表',
      'columnNames': []
    },
    {
      'jdbcName': 'sys_users',
      'comment': '用户表',
      'columnNames': []
    }
  ]
})

Mock.mock(process.env.BASE_API + '/code/dataSource/test', 'get', {
  'code': 200,
  'data': true
})

Mock.mock(process.env.BASE_API + '/code/dataSource', 'post', {
  'code': 200,
  'data': {}
})

Mock.mock(process.env.BASE_API + '/code/dataSource', 'delete', {
  'code': 200,
  'data': {}
})

Mock.mock(process.env.BASE_API + '/code/dataSource', 'post', {
  'code': 200,
  'data': {}
})

Mock.mock(process.env.BASE_API + '/code/generate', 'get', {
  'code': 200,
  'data': {}
})

Mock.mock(process.env.BASE_API + '/code/generateProject', 'get', {
  'code': 200,
  'data': {}
})
