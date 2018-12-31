import Mock from 'mockjs'

/* organization界面假接口数据 */
Mock.mock(process.env.BASE_API + '/organization/organizationList', 'get', {
  'code': 200,
  'msg': '',
  'data': {
    'pageNum': 1,
    'pageSize': 13,
    'size': 13,
    'orderBy': null,
    'startRow': 0,
    'endRow': 12,
    'total': 13,
    'pages': 1,
    'list': [
      {
        'id': '01ac6ed2113a4c0290d84403970e4157',
        'name': '综合部',
        'memo': null
      },
      {
        'id': '1f3e35fdc109413f805154c711e1db14',
        'name': '传送网络运营中心',
        'memo': null
      },
      {
        'id': '1fe2036049e143db8345ec35fd55f750',
        'name': '中心领导',
        'memo': null
      },
      {
        'id': '36bf4670ee694ca5b5ed46fb37304791',
        'name': '省公司',
        'memo': null
      },
      {
        'id': '4e4e17ab75cf4aea96507f320c0bf811',
        'name': '客户服务部',
        'memo': null
      },
      {
        'id': '628c95464947448788ff991a59abb879',
        'name': '网络运营部',
        'memo': null
      },
      {
        'id': '7836d2bac65144688f6edbba1fcf64b2',
        'name': '高端装维项目部',
        'memo': null
      },
      {
        'id': '7bd400fb74404913a7c1f0ca78d716eb',
        'name': '纪检监察部/党群工作部',
        'memo': null
      },
      {
        'id': '7ff57d368e6c471f996eecdb5dd54c49',
        'name': '应急响应和网络优化支撑中心',
        'memo': null
      },
      {
        'id': '8cdb172291c144c291e7ae4c3d3e10b8',
        'name': '汕头海缆站',
        'memo': null
      },
      {
        'id': '9cc2734e32d44d9a953784ff854e622c',
        'name': '网络优化项目部',
        'memo': null
      },
      {
        'id': 'd5cfc084328f42d19f6be63d34da8299',
        'name': '工会',
        'memo': null
      },
      {
        'id': 'd7126e6fefac4237ab932cf58c416bc5',
        'name': '安全保卫部',
        'memo': null
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
