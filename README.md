# CommonTemplate

该项目是一个简单的通用平台代码,提供简单的登录、注册、用户管理、权限管理以及前端访问等.

该项目基于 Java8 开发, 缓存采用使用 redis 作为缓存,权限检验采用 jwt + shiro 实现, hikari 作为数据库连接池, logback 作为日志系统, 如若需要使用其他组件, 请自行定义 exclusion.

此处是简单的项目介绍.

### CommonTemplateBackEnd(后端实现代码)
代码结构

```
project
│   │
│   └───DB
|   │   │
|   │   └───ssm_shiro.sql(数据库文件)
│   │
│   └───template.zip(代码文件模板)
└───src/main/java
│   │
│   └───com.tisson.demo
|       |
|       └───CommonTemplateApplication.java(启动类)
|       |
│       └───com.tisson.demo.aspect(切面类)
|       |
│       └───com.tisson.demo.common.base(通用类)
│       │
│       └───com.tisson.demo.common.base.expt(通用异常类)
│       │
|       └───com.tisson.demo.common.base.shiro(shiro权限处理)
|       |
|       └───com.tisson.demo.common.codeGenerate(代码生成处理)
|       |
|       └───com.tisson.demo.common.controller(通用前端处理器)
|       |
|       └───com.tisson.demo.common.util(通用工具类)
|       |
|       └───com.tisson.demo.configuration(配置类)
|       |
|       └───....(业务处理等)
└───src/main/resources
|   │
|   └─── mapper(mybatis 接口类mapper)
|   │
|   └─── props (配置文件properties)
|   |
|   └───templates (模板文件)
|   |
|   └───application.yml、logback-spring.xml
```


### CommonTemplateFrontEnd(前端实现代码)
代码结构

```
project
│   │
│   └───build(webpack配置文件)
│   │
│   └───config(启动或打包配置文件)
│   │
└───└───node_modules(npm 依赖)
    │
    └───src
        |
        └───api(页面请求接口url配置)
        |
        └───assets(静态资源)
        |
        └───components(组件)
        │
        └───icons(图标和字体文件)
        │
        └───mock(本地mock接口文件数据)
        |
        └───router(路由配置)
        |
        └───store(会话信息)
        |
        └───styles(css文件)
        |
        └───utils(工具类)
        |       |
        |       └───index.js(简单工具)
        |       |
        |       └───cookie.js(cookie操作)
        |       |
        |       └───permisson.js(路由权限处理)
        |       |
        |       └───request.js(api请求处理)
        |       |
        |       └───...
        |
        └───views(业务处理界面)
```
