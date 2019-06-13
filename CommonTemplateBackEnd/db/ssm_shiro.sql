/*
 Navicat Premium Data Transfer

 Source Server         : 172.16.28.152
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : 172.16.28.152:3306
 Source Schema         : template

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 13/06/2019 14:35:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_common_resources
-- ----------------------------
DROP TABLE IF EXISTS `sys_common_resources`;
CREATE TABLE `sys_common_resources`  (
  `id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件名',
  `used` tinyint(1) NULL DEFAULT NULL COMMENT '是否使用到,0为未使用,1为使用中',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文件路径',
  `type` tinyint(2) NULL DEFAULT NULL COMMENT '文件类型',
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '公用文件资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_devices
-- ----------------------------
DROP TABLE IF EXISTS `sys_devices`;
CREATE TABLE `sys_devices`  (
  `id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '设备标识',
  `user_id` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  `device_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备ID',
  `version` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登陆版本',
  `status` char(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `memo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` date NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` date NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户设备表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sys_dicts
-- ----------------------------
DROP TABLE IF EXISTS `sys_dicts`;
CREATE TABLE `sys_dicts`  (
  `id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典标识',
  `key` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典项标识',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典项值',
  `state` char(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `memo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `order` int(2) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统字典表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sys_logger_login
-- ----------------------------
DROP TABLE IF EXISTS `sys_logger_login`;
CREATE TABLE `sys_logger_login`  (
  `id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
  `user_name` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登陆用户名',
  `time` datetime(0) NULL DEFAULT NULL COMMENT '登陆时间',
  `code` char(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态码',
  `result` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登陆结果',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '登陆日志表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sys_logger_operation
-- ----------------------------
DROP TABLE IF EXISTS `sys_logger_operation`;
CREATE TABLE `sys_logger_operation`  (
  `id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
  `method` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `url` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求URL',
  `params` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `user_name` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登陆用户名',
  `time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  `code` char(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态码',
  `result` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结果信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '操作记录表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sys_organizations
-- ----------------------------
DROP TABLE IF EXISTS `sys_organizations`;
CREATE TABLE `sys_organizations`  (
  `id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `memo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_user` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_organizations
-- ----------------------------
INSERT INTO `sys_organizations` VALUES ('01ac6ed2113a4c0290d84403970e4157', '综合部', '111', 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('1f3e35fdc109413f805154c711e1db14', '传送网络运营中心', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('1fe2036049e143db8345ec35fd55f750', '中心领导', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('36bf4670ee694ca5b5ed46fb37304791', '省公司', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('4e4e17ab75cf4aea96507f320c0bf811', '客户服务部', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('628c95464947448788ff991a59abb879', '网络运营部', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('7836d2bac65144688f6edbba1fcf64b2', '高端装维项目部', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('7bd400fb74404913a7c1f0ca78d716eb', '纪检监察部/党群工作部', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('7ff57d368e6c471f996eecdb5dd54c49', '应急响应和网络优化支撑中心', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('8cdb172291c144c291e7ae4c3d3e10b8', '汕头海缆站', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('9cc2734e32d44d9a953784ff854e622c', '网络优化项目部', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('d5cfc084328f42d19f6be63d34da8299', '工会', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('d7126e6fefac4237ab932cf58c416bc5', '安全保卫部', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');

-- ----------------------------
-- Table structure for sys_pages
-- ----------------------------
DROP TABLE IF EXISTS `sys_pages`;
CREATE TABLE `sys_pages`  (
  `id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端界面权限名称',
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端界面权限链接',
  `parent_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端界面权限父节点标识',
  `memo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '前端界面权限表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_pages
-- ----------------------------
INSERT INTO `sys_pages` VALUES ('096673c87ed347879ac51272ced4bbe3', '数据表', '/code/tables', '3aed6fd74f434dec874fdd4e8749257e', NULL, 'admin', '2018-12-29 09:21:25', 'admin', '2018-12-29 09:21:25');
INSERT INTO `sys_pages` VALUES ('142cad4c42964648b6945a4f92a2084b', '接口权限维护', '/system/resource', 'ea274f595fbd4476aade23c1e7d481ea', NULL, 'admin', '2018-12-29 09:21:25', 'admin', '2018-12-29 09:21:25');
INSERT INTO `sys_pages` VALUES ('3aed6fd74f434dec874fdd4e8749257e', '代码生成', '/code', NULL, NULL, 'admin', '2018-12-29 09:21:25', 'admin', '2018-12-29 09:21:25');
INSERT INTO `sys_pages` VALUES ('3bddc06ee3fc4d5aa23c95fdecc9bc3b', '页面权限维护', '/system/accessPage', 'ea274f595fbd4476aade23c1e7d481ea', NULL, 'admin', '2018-12-29 09:21:25', 'admin', '2018-12-29 09:21:25');
INSERT INTO `sys_pages` VALUES ('45559a8377844b88832bf53a5cea965c', '注册任务审批', '/system/registerTask', 'ea274f595fbd4476aade23c1e7d481ea', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_pages` VALUES ('46b001ba94224dab8b90ca32944962bd', '数据源', '/code/dataSource', '3aed6fd74f434dec874fdd4e8749257e', NULL, 'admin', '2018-12-29 09:21:25', 'admin', '2018-12-29 09:21:25');
INSERT INTO `sys_pages` VALUES ('47e9f17990284132949af957e1a336d9', '日志查看', '/logger', NULL, NULL, 'admin', '2018-12-29 09:21:25', 'admin', '2018-12-29 09:21:25');
INSERT INTO `sys_pages` VALUES ('7c8220f4af114dbf9d41308786fef51f', '操作日志', '/logger/operation', '47e9f17990284132949af957e1a336d9', NULL, 'admin', '2018-12-29 09:21:25', 'admin', '2018-12-29 09:21:25');
INSERT INTO `sys_pages` VALUES ('a4c521627a454054bc19fc45ceb1cdfb', '登陆日志', '/logger/login', '47e9f17990284132949af957e1a336d9', NULL, 'admin', '2018-12-29 09:21:25', 'admin', '2018-12-29 09:21:25');
INSERT INTO `sys_pages` VALUES ('aeeff46aee4f46b0b62d90486860e442', '用户维护', '/system/user', 'ea274f595fbd4476aade23c1e7d481ea', NULL, 'admin', '2018-12-29 09:21:25', 'admin', '2018-12-29 09:21:25');
INSERT INTO `sys_pages` VALUES ('e701bed5816746f897a1dc578ccb7474', '初始化项目', '/code/project', '3aed6fd74f434dec874fdd4e8749257e', NULL, 'admin', '2018-12-29 09:21:25', 'admin', '2018-12-29 09:21:25');
INSERT INTO `sys_pages` VALUES ('e8357f638a11455687b2ec289add7956', '流程维护', '/system/process', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_pages` VALUES ('ea274f595fbd4476aade23c1e7d481ea', '组织维护', '/system', NULL, NULL, 'admin', '2018-12-29 09:21:25', 'admin', '2018-12-29 09:21:25');
INSERT INTO `sys_pages` VALUES ('f7d84f3059d547ea8dcfa3cc7b264fa3', '角色维护', '/system/role', 'ea274f595fbd4476aade23c1e7d481ea', NULL, 'admin', '2018-12-29 09:21:25', 'admin', '2018-12-29 09:21:25');
INSERT INTO `sys_pages` VALUES ('f935b89a12b64b1786a39236c17774be', '部门维护', '/system/organization', 'ea274f595fbd4476aade23c1e7d481ea', NULL, 'admin', '2018-12-29 09:21:25', 'admin', '2018-12-29 09:21:25');

-- ----------------------------
-- Table structure for sys_resources
-- ----------------------------
DROP TABLE IF EXISTS `sys_resources`;
CREATE TABLE `sys_resources`  (
  `id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接口权限标识',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口权限名称',
  `label` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口权限描述',
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口权限链接',
  `parent_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口权限父节点标识',
  `memo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '接口权限表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_resources
-- ----------------------------
INSERT INTO `sys_resources` VALUES ('04f3167e3c5f409d89e3a8752b4b4dc4', '前端路由下用户列表', '前端路由下用户列表', '/user/loadUserAccessPagesById', 'b5d0550521234bf68cb607e9df21beeb', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('08bd620623df48f7b63b67e46233d7a1', '数据源测试', '数据源测试', '/code/dataSource/test', '47f5cc2c8ab740748ab2e7edf09fc5df', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('0b69bcff0caf46a5af09ed65aa5b0cb0', '审批登录用户注册任务', NULL, '/user/approvalRegisterTask', 'b5d0550521234bf68cb607e9df21beeb', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_resources` VALUES ('0fb29d7676c343eca37eeb9c74760158', '角色维护', '角色维护', '/role', NULL, NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('189f607d50b74886b090e3dea54f5e85', '权限资源维护', '权限资源维护', '/resource', NULL, NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('1a8aa9f1a77c4b14bb6e66b9baab5aaf', '移除数据源', '移除数据源', '/code/deleteDataSource', '47f5cc2c8ab740748ab2e7edf09fc5df', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('1ac1e34861fb4bb6b4e8ced9d40a28eb', '单个用户信息', '单个用户信息', '/user/loadUserById', 'b5d0550521234bf68cb607e9df21beeb', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('1ebcfd263c854ef1b5fe9e737fff1413', '权限资源下用户列表', '权限资源下用户列表', '/user/loadUserResourcesById', 'b5d0550521234bf68cb607e9df21beeb', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('228a2b3f77cc48d7a1076bcff9ee8130', '用户列表', '用户列表', '/user/usersList', 'b5d0550521234bf68cb607e9df21beeb', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('26f4118721c040f3a06a8b4e62d7a4c1', '修改用户角色', '修改用户角色', '/user/updateUserRoles', 'b5d0550521234bf68cb607e9df21beeb', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('415edaa4e4fe439fa3474b5db6855207', '获取登录用户注册任务信息', NULL, '/user/registerTask', 'b5d0550521234bf68cb607e9df21beeb', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_resources` VALUES ('4296d361cf1c4ff399c5ebca7d6e30cc', '新增用户', '新增用户', '/user/addSysUsers', 'b5d0550521234bf68cb607e9df21beeb', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('47f5cc2c8ab740748ab2e7edf09fc5df', '代码生成', '代码生成', '/code', NULL, NULL, 'admin', '2018-12-29 00:00:00', 'admin', '2018-12-29 00:00:00');
INSERT INTO `sys_resources` VALUES ('6280fc300441453a8b85a349b562f63d', '部署流程压缩包', NULL, '/process/deploy', '9036585724ca419c8bbc0e581a7a2bde', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_resources` VALUES ('6a82f337cad6422495ad37e873178a32', '登陆用户可访问前端路由列表', '登陆用户可访问前端路由列表', '/user/queryAccessPages', 'b5d0550521234bf68cb607e9df21beeb', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('718783518e1e41e5a71d89980cbbbdef', '部门维护', '部门维护', '/organization', NULL, NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('73d2e21429a348a3bbb81673f1479333', '更新用户', '更新用户', '/user/updateSysUsers', 'b5d0550521234bf68cb607e9df21beeb', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('76876e5e01c246b2998753dd9c206a2a', '删除用户', '删除用户', '/user/deleteSysUsers', 'b5d0550521234bf68cb607e9df21beeb', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('7e3b6acc33b047ceb06c94bc613393a7', '生成简易项目', '生成简易项目', '/code/generateProject', '47f5cc2c8ab740748ab2e7edf09fc5df', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('842f7b68238246b8a9cb617589fc0817', '前端路由维护', '前端路由维护', '/accessPage', NULL, NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('9036585724ca419c8bbc0e581a7a2bde', '流程维护', NULL, '/process', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_resources` VALUES ('91c3840e2dc24853be5190798e3cc54b', '修改用户可访问前端路由', '修改用户可访问前端路由', '/user/updateUserAccessPages', 'b5d0550521234bf68cb607e9df21beeb', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('abd806c092c54fd2bbb35d0045bb5f06', '数据源', '数据源', '/code/dataSource', '47f5cc2c8ab740748ab2e7edf09fc5df', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('b5d0550521234bf68cb607e9df21beeb', '用户信息维护', '用户信息维护', '/user', NULL, NULL, 'admin', '2018-12-29 00:00:00', 'admin', '2018-12-29 00:00:00');
INSERT INTO `sys_resources` VALUES ('d1298fd5ebc24f02820c7216a4c51215', '上传部署流程压缩包', NULL, '/process/resources', '9036585724ca419c8bbc0e581a7a2bde', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_resources` VALUES ('d87abd8dcef6419b980e1d414f1342c4', '角色下用户列表', '角色下用户列表', '/user/loadUserRolesById', 'b5d0550521234bf68cb607e9df21beeb', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('e7a8d0aeaaba458b92109f601fc4a5a1', '取消部署流程', NULL, '/process/undeploy', '9036585724ca419c8bbc0e581a7a2bde', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_resources` VALUES ('e8dfe9d2c1cb4a51921f0f228a2a4a80', '生成简易代码', '生成简易代码', '/code/generateSimpleCode', '47f5cc2c8ab740748ab2e7edf09fc5df', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('ec62a2a880884946b494c2d16e0c15d9', '数据表', '数据表', '/code/dataSource/tables', '47f5cc2c8ab740748ab2e7edf09fc5df', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('edac34841553453280c5efcf84a1c6f0', '修改用户可访问权限资源', '修改用户可访问权限资源', '/user/updateUserResources', 'b5d0550521234bf68cb607e9df21beeb', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('f0159b6ee8d244b98ca3b2ff767836cd', '登陆用户信息', '登陆用户信息', '/user/queryLoginUserInfo', 'b5d0550521234bf68cb607e9df21beeb', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('f03fa781ea3f407bb8095e85a5da0d88', 'Token列表', 'Token列表', '/user/ssoToken', 'b5d0550521234bf68cb607e9df21beeb', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('f8354b87fcd7462582e230d32dcc21f6', '新增数据源', '新增数据源', '/code/addDataSource', '47f5cc2c8ab740748ab2e7edf09fc5df', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');

-- ----------------------------
-- Table structure for sys_role_pages
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_pages`;
CREATE TABLE `sys_role_pages`  (
  `id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
  `role_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色标识',
  `page_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端界面权限标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色授权前端界面表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_role_pages
-- ----------------------------
INSERT INTO `sys_role_pages` VALUES ('3565a6638b6311e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '3aed6fd74f434dec874fdd4e8749257e');
INSERT INTO `sys_role_pages` VALUES ('3565adc58b6311e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '096673c87ed347879ac51272ced4bbe3');
INSERT INTO `sys_role_pages` VALUES ('3565b12c8b6311e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '46b001ba94224dab8b90ca32944962bd');
INSERT INTO `sys_role_pages` VALUES ('3565b5188b6311e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', 'e701bed5816746f897a1dc578ccb7474');
INSERT INTO `sys_role_pages` VALUES ('3565b8c38b6311e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '47e9f17990284132949af957e1a336d9');
INSERT INTO `sys_role_pages` VALUES ('3565bbf18b6311e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '7c8220f4af114dbf9d41308786fef51f');
INSERT INTO `sys_role_pages` VALUES ('3565bf568b6311e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', 'a4c521627a454054bc19fc45ceb1cdfb');
INSERT INTO `sys_role_pages` VALUES ('3565c2cd8b6311e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', 'ea274f595fbd4476aade23c1e7d481ea');
INSERT INTO `sys_role_pages` VALUES ('3565c65c8b6311e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '142cad4c42964648b6945a4f92a2084b');
INSERT INTO `sys_role_pages` VALUES ('3565c95a8b6311e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '3bddc06ee3fc4d5aa23c95fdecc9bc3b');
INSERT INTO `sys_role_pages` VALUES ('3565ccc08b6311e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '45559a8377844b88832bf53a5cea965c');
INSERT INTO `sys_role_pages` VALUES ('3565cfce8b6311e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', 'aeeff46aee4f46b0b62d90486860e442');
INSERT INTO `sys_role_pages` VALUES ('3565d3c18b6311e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', 'f7d84f3059d547ea8dcfa3cc7b264fa3');
INSERT INTO `sys_role_pages` VALUES ('3565d6aa8b6311e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', 'f935b89a12b64b1786a39236c17774be');
INSERT INTO `sys_role_pages` VALUES ('7301b34234ef11e9881a005056bff07f', '98646ff14439414383ceb3e28d4377c1', '3aed6fd74f434dec874fdd4e8749257e');
INSERT INTO `sys_role_pages` VALUES ('7301b83634ef11e9881a005056bff07f', '98646ff14439414383ceb3e28d4377c1', '096673c87ed347879ac51272ced4bbe3');
INSERT INTO `sys_role_pages` VALUES ('7301bbb634ef11e9881a005056bff07f', '98646ff14439414383ceb3e28d4377c1', '46b001ba94224dab8b90ca32944962bd');
INSERT INTO `sys_role_pages` VALUES ('7301bf3e34ef11e9881a005056bff07f', '98646ff14439414383ceb3e28d4377c1', 'e701bed5816746f897a1dc578ccb7474');
INSERT INTO `sys_role_pages` VALUES ('7301c28234ef11e9881a005056bff07f', '98646ff14439414383ceb3e28d4377c1', '47e9f17990284132949af957e1a336d9');
INSERT INTO `sys_role_pages` VALUES ('7301c5c334ef11e9881a005056bff07f', '98646ff14439414383ceb3e28d4377c1', '7c8220f4af114dbf9d41308786fef51f');
INSERT INTO `sys_role_pages` VALUES ('7301c9a234ef11e9881a005056bff07f', '98646ff14439414383ceb3e28d4377c1', 'a4c521627a454054bc19fc45ceb1cdfb');

-- ----------------------------
-- Table structure for sys_role_resources
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_resources`;
CREATE TABLE `sys_role_resources`  (
  `id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
  `role_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色标识',
  `resource_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接口权限标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色授权接口权限表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_role_resources
-- ----------------------------
INSERT INTO `sys_role_resources` VALUES ('4e9760b734ef11e9881a005056bff07f', '98646ff14439414383ceb3e28d4377c1', '47f5cc2c8ab740748ab2e7edf09fc5df');
INSERT INTO `sys_role_resources` VALUES ('4e9765b834ef11e9881a005056bff07f', '98646ff14439414383ceb3e28d4377c1', '08bd620623df48f7b63b67e46233d7a1');
INSERT INTO `sys_role_resources` VALUES ('4e976a3134ef11e9881a005056bff07f', '98646ff14439414383ceb3e28d4377c1', '1a8aa9f1a77c4b14bb6e66b9baab5aaf');
INSERT INTO `sys_role_resources` VALUES ('4e976e8334ef11e9881a005056bff07f', '98646ff14439414383ceb3e28d4377c1', '7e3b6acc33b047ceb06c94bc613393a7');
INSERT INTO `sys_role_resources` VALUES ('4e97726c34ef11e9881a005056bff07f', '98646ff14439414383ceb3e28d4377c1', 'abd806c092c54fd2bbb35d0045bb5f06');
INSERT INTO `sys_role_resources` VALUES ('4e97765a34ef11e9881a005056bff07f', '98646ff14439414383ceb3e28d4377c1', 'e8dfe9d2c1cb4a51921f0f228a2a4a80');
INSERT INTO `sys_role_resources` VALUES ('4e977a9634ef11e9881a005056bff07f', '98646ff14439414383ceb3e28d4377c1', 'f8354b87fcd7462582e230d32dcc21f6');
INSERT INTO `sys_role_resources` VALUES ('4e977e1034ef11e9881a005056bff07f', '98646ff14439414383ceb3e28d4377c1', '842f7b68238246b8a9cb617589fc0817');
INSERT INTO `sys_role_resources` VALUES ('4e97820034ef11e9881a005056bff07f', '98646ff14439414383ceb3e28d4377c1', '1ac1e34861fb4bb6b4e8ced9d40a28eb');
INSERT INTO `sys_role_resources` VALUES ('4e97858934ef11e9881a005056bff07f', '98646ff14439414383ceb3e28d4377c1', '1ebcfd263c854ef1b5fe9e737fff1413');
INSERT INTO `sys_role_resources` VALUES ('4e97890734ef11e9881a005056bff07f', '98646ff14439414383ceb3e28d4377c1', '6a82f337cad6422495ad37e873178a32');
INSERT INTO `sys_role_resources` VALUES ('4e978d0234ef11e9881a005056bff07f', '98646ff14439414383ceb3e28d4377c1', 'f0159b6ee8d244b98ca3b2ff767836cd');
INSERT INTO `sys_role_resources` VALUES ('4e97906834ef11e9881a005056bff07f', '98646ff14439414383ceb3e28d4377c1', 'ec62a2a880884946b494c2d16e0c15d9');
INSERT INTO `sys_role_resources` VALUES ('550a70e08c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '0fb29d7676c343eca37eeb9c74760158');
INSERT INTO `sys_role_resources` VALUES ('550a75e68c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '189f607d50b74886b090e3dea54f5e85');
INSERT INTO `sys_role_resources` VALUES ('550a797c8c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '47f5cc2c8ab740748ab2e7edf09fc5df');
INSERT INTO `sys_role_resources` VALUES ('550a7c6b8c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '08bd620623df48f7b63b67e46233d7a1');
INSERT INTO `sys_role_resources` VALUES ('550a7f658c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '1a8aa9f1a77c4b14bb6e66b9baab5aaf');
INSERT INTO `sys_role_resources` VALUES ('550a82448c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '7e3b6acc33b047ceb06c94bc613393a7');
INSERT INTO `sys_role_resources` VALUES ('550a85068c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', 'abd806c092c54fd2bbb35d0045bb5f06');
INSERT INTO `sys_role_resources` VALUES ('550a87e18c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', 'e8dfe9d2c1cb4a51921f0f228a2a4a80');
INSERT INTO `sys_role_resources` VALUES ('550a8aac8c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', 'ec62a2a880884946b494c2d16e0c15d9');
INSERT INTO `sys_role_resources` VALUES ('550a8d608c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', 'f8354b87fcd7462582e230d32dcc21f6');
INSERT INTO `sys_role_resources` VALUES ('550a90258c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '718783518e1e41e5a71d89980cbbbdef');
INSERT INTO `sys_role_resources` VALUES ('550a92838c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '842f7b68238246b8a9cb617589fc0817');
INSERT INTO `sys_role_resources` VALUES ('550a94d78c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '9036585724ca419c8bbc0e581a7a2bde');
INSERT INTO `sys_role_resources` VALUES ('550a971c8c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '6280fc300441453a8b85a349b562f63d');
INSERT INTO `sys_role_resources` VALUES ('550a994b8c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', 'd1298fd5ebc24f02820c7216a4c51215');
INSERT INTO `sys_role_resources` VALUES ('550a9b958c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', 'e7a8d0aeaaba458b92109f601fc4a5a1');
INSERT INTO `sys_role_resources` VALUES ('550a9dd78c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', 'b5d0550521234bf68cb607e9df21beeb');
INSERT INTO `sys_role_resources` VALUES ('550aa0868c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '04f3167e3c5f409d89e3a8752b4b4dc4');
INSERT INTO `sys_role_resources` VALUES ('550aa4078c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '0b69bcff0caf46a5af09ed65aa5b0cb0');
INSERT INTO `sys_role_resources` VALUES ('550aa65f8c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '1ac1e34861fb4bb6b4e8ced9d40a28eb');
INSERT INTO `sys_role_resources` VALUES ('550aa8a08c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '1ebcfd263c854ef1b5fe9e737fff1413');
INSERT INTO `sys_role_resources` VALUES ('550aaad78c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '228a2b3f77cc48d7a1076bcff9ee8130');
INSERT INTO `sys_role_resources` VALUES ('550aad148c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '26f4118721c040f3a06a8b4e62d7a4c1');
INSERT INTO `sys_role_resources` VALUES ('550aaf4c8c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '415edaa4e4fe439fa3474b5db6855207');
INSERT INTO `sys_role_resources` VALUES ('550ab1f48c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '4296d361cf1c4ff399c5ebca7d6e30cc');
INSERT INTO `sys_role_resources` VALUES ('550ab4b78c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '6a82f337cad6422495ad37e873178a32');
INSERT INTO `sys_role_resources` VALUES ('550ab7f38c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '73d2e21429a348a3bbb81673f1479333');
INSERT INTO `sys_role_resources` VALUES ('550abad58c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '76876e5e01c246b2998753dd9c206a2a');
INSERT INTO `sys_role_resources` VALUES ('550abe278c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', '91c3840e2dc24853be5190798e3cc54b');
INSERT INTO `sys_role_resources` VALUES ('550ac1008c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', 'd87abd8dcef6419b980e1d414f1342c4');
INSERT INTO `sys_role_resources` VALUES ('550ac4588c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', 'edac34841553453280c5efcf84a1c6f0');
INSERT INTO `sys_role_resources` VALUES ('550ac76b8c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', 'f0159b6ee8d244b98ca3b2ff767836cd');
INSERT INTO `sys_role_resources` VALUES ('550aca488c3011e9924a005056bff07f', '205e04601efb4e90bd1cab77e131420b', 'f03fa781ea3f407bb8095e85a5da0d88');

-- ----------------------------
-- Table structure for sys_roles
-- ----------------------------
DROP TABLE IF EXISTS `sys_roles`;
CREATE TABLE `sys_roles`  (
  `id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `label` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `memo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_roles
-- ----------------------------
INSERT INTO `sys_roles` VALUES ('205e04601efb4e90bd1cab77e131420b', 'admin', '管理员', NULL, 'admin', '2018-12-29 09:04:05', 'admin', '2018-12-29 09:04:08');
INSERT INTO `sys_roles` VALUES ('98646ff14439414383ceb3e28d4377c1', 'test', '测试', NULL, 'admin', '2018-12-29 09:04:08', 'admin', '2018-12-29 09:04:08');

-- ----------------------------
-- Table structure for sys_user_organizations
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_organizations`;
CREATE TABLE `sys_user_organizations`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  `organization_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户部门关系表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_user_organizations
-- ----------------------------
INSERT INTO `sys_user_organizations` VALUES ('da9e7e118cd911e9924a005056bff07f', '60367faced7748379a45c9bfc908a3b0', '01ac6ed2113a4c0290d84403970e4157');
INSERT INTO `sys_user_organizations` VALUES ('f606119e18a111e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', '36bf4670ee694ca5b5ed46fb37304791');
INSERT INTO `sys_user_organizations` VALUES ('fab17ddb6c84401aa261d0c286d0cb6a', 'd1181ed8cea8485287bbdd5bea266908', '36bf4670ee694ca5b5ed46fb37304791');

-- ----------------------------
-- Table structure for sys_user_pages
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_pages`;
CREATE TABLE `sys_user_pages`  (
  `id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标志',
  `user_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户标识',
  `page_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '前端界面权限标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户授权前端界面表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_user_pages
-- ----------------------------
INSERT INTO `sys_user_pages` VALUES ('a8ccb403192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', '718783518e1e41e5a71d89980cbbbdef');
INSERT INTO `sys_user_pages` VALUES ('a8ccc4fd192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', '1ac1e34861fb4bb6b4e8ced9d40a28eb');
INSERT INTO `sys_user_pages` VALUES ('a8cccb24192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', 'f0159b6ee8d244b98ca3b2ff767836cd');
INSERT INTO `sys_user_pages` VALUES ('a8cccf89192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', '47f5cc2c8ab740748ab2e7edf09fc5df');
INSERT INTO `sys_user_pages` VALUES ('a8ccd335192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', '7e3b6acc33b047ceb06c94bc613393a7');
INSERT INTO `sys_user_pages` VALUES ('a8ccd6b9192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', 'e8dfe9d2c1cb4a51921f0f228a2a4a80');
INSERT INTO `sys_user_pages` VALUES ('a8ccda74192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', 'abd806c092c54fd2bbb35d0045bb5f06');
INSERT INTO `sys_user_pages` VALUES ('a8ccde5f192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', 'f8354b87fcd7462582e230d32dcc21f6');
INSERT INTO `sys_user_pages` VALUES ('a8cce265192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', '08bd620623df48f7b63b67e46233d7a1');
INSERT INTO `sys_user_pages` VALUES ('a8cce615192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', '1a8aa9f1a77c4b14bb6e66b9baab5aaf');
INSERT INTO `sys_user_pages` VALUES ('a8cce9b0192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', 'ec62a2a880884946b494c2d16e0c15d9');

-- ----------------------------
-- Table structure for sys_user_resources
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_resources`;
CREATE TABLE `sys_user_resources`  (
  `id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
  `user_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户标识',
  `resource_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口权限标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户授权接口权限表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_user_resources
-- ----------------------------
INSERT INTO `sys_user_resources` VALUES ('8e69e086192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', '718783518e1e41e5a71d89980cbbbdef');
INSERT INTO `sys_user_resources` VALUES ('8e69e941192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', '1ac1e34861fb4bb6b4e8ced9d40a28eb');
INSERT INTO `sys_user_resources` VALUES ('8e69ef15192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', 'f0159b6ee8d244b98ca3b2ff767836cd');
INSERT INTO `sys_user_resources` VALUES ('8e69f6de192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', '47f5cc2c8ab740748ab2e7edf09fc5df');
INSERT INTO `sys_user_resources` VALUES ('8e69fdfa192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', '7e3b6acc33b047ceb06c94bc613393a7');
INSERT INTO `sys_user_resources` VALUES ('8e6a0460192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', 'e8dfe9d2c1cb4a51921f0f228a2a4a80');
INSERT INTO `sys_user_resources` VALUES ('8e6a08d9192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', 'abd806c092c54fd2bbb35d0045bb5f06');
INSERT INTO `sys_user_resources` VALUES ('8e6a0d99192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', 'f8354b87fcd7462582e230d32dcc21f6');
INSERT INTO `sys_user_resources` VALUES ('8e6a188c192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', '08bd620623df48f7b63b67e46233d7a1');
INSERT INTO `sys_user_resources` VALUES ('8e6a1d87192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', '1a8aa9f1a77c4b14bb6e66b9baab5aaf');
INSERT INTO `sys_user_resources` VALUES ('8e6a223b192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', 'ec62a2a880884946b494c2d16e0c15d9');

-- ----------------------------
-- Table structure for sys_user_roles
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_roles`;
CREATE TABLE `sys_user_roles`  (
  `id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
  `user_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户标识',
  `role_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户授权角色表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_user_roles
-- ----------------------------
INSERT INTO `sys_user_roles` VALUES ('8a718caa192a11e9a22675e315bc7100', '266908ea02765d49094d72a76a0e5fc2', '98646ff14439414383ceb3e28d4377c1');
INSERT INTO `sys_user_roles` VALUES ('ed8e56c458cc4402959391170636fd09', 'd1181ed8cea8485287bbdd5bea266908', '205e04601efb4e90bd1cab77e131420b');

-- ----------------------------
-- Table structure for sys_users
-- ----------------------------
DROP TABLE IF EXISTS `sys_users`;
CREATE TABLE `sys_users`  (
  `id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '帐号标识',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '帐号登陆标识',
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录密码',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱名称',
  `mobile_phone` char(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '移动电话',
  `label` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '帐号名称',
  `status` char(3) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态',
  `memo` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `sex` smallint(1) NULL DEFAULT NULL COMMENT '性别',
  `photo` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `eff_date` datetime(0) NULL DEFAULT NULL COMMENT '账号生效时间',
  `max_online_count` tinyint(1) NULL DEFAULT -1 COMMENT '最大在线人数',
  `exp_date` datetime(0) NULL DEFAULT NULL COMMENT '账号失效时间',
  `create_user` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_users
-- ----------------------------
INSERT INTO `sys_users` VALUES ('266908ea02765d49094d72a76a0e5fc2', 'test', '9b22bcdea4a09a59382323a479982c10', 'chenweiqiao@tisson.cn', '18912345678', '测试', 'S0A', NULL, 0, NULL, '2018-12-29 00:00:00', -1, '2058-12-29 00:00:00', 'admin', '2018-12-29 00:00:00', 'admin', '2018-12-29 00:00:00');
INSERT INTO `sys_users` VALUES ('d1181ed8cea8485287bbdd5bea266908', 'admin', '9b22bcdea4a09a59382323a479982c10', 'chenweiqiao@tisson.cn', '18912345678', '管理员', 'S0A', NULL, 0, NULL, '2018-12-29 09:02:44', -1, '2058-12-29 09:03:00', 'admin', '2018-12-29 09:03:28', 'admin', '2018-12-29 09:03:37');

SET FOREIGN_KEY_CHECKS = 1;
