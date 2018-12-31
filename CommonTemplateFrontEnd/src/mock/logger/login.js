import Mock from 'mockjs'
/* login界面假接口数据 */
Mock.mock(process.env.BASE_API + '/logger/loginList', 'get', {
  'code': 200,
  'msg': '',
  'data': {
    items: [{
      id: 1,
      userId: 'admin',
      userName: '系统管理员',
      ip: '127.0.0.1',
      time: '2018-01-01 00:00:00',
      code: '200',
      result: 'success'
    }, {
      id: 2,
      userId: 'admin',
      userName: '系统管理员',
      ip: '127.0.0.1',
      time: '2018-01-01 00:00:00',
      code: '200',
      result: 'success'
    }, {
      id: 3,
      userId: 'admin',
      userName: '系统管理员',
      ip: '127.0.0.1',
      time: '2018-01-01 00:00:00',
      code: '200',
      result: 'success'
    }, {
      id: 4,
      userId: 'admin',
      userName: '系统管理员',
      ip: '127.0.0.1',
      time: '2018-01-01 00:00:00',
      code: '200',
      result: 'success'
    }],
    total: 4
  }
})
