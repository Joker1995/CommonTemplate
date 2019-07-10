/*
 Navicat Premium Data Transfer

 Source Server         : 172.16.28.152
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : 172.16.28.152:3306
 Source Schema         : smcc

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 04/07/2019 10:44:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_common_resources
-- ----------------------------
DROP TABLE IF EXISTS `sys_common_resources`;
CREATE TABLE `sys_common_resources`  (
  `id` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
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
  `id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '设备标识',
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
  `id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典标识',
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
  `id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
  `user_name` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登陆用户名',
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
  `id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
  `method` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `url` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求URL',
  `params` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `user_name` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登陆用户名',
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
  `id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `memo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_user` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_organizations
-- ----------------------------
INSERT INTO `sys_organizations` VALUES ('656636605313847296', '综合部', '111', 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('656636605313847297', '传送网络运营中心', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('656636605313847298', '中心领导', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('656636605313847299', '省公司', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('656636605313847300', '客户服务部', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('656636605313847301', '网络运营部', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('656636605313847302', '高端装维项目部', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('656636605318041600', '纪检监察部/党群工作部', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('656636605318041601', '应急响应和网络优化支撑中心', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('656636605318041602', '汕头海缆站', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('656636605318041603', '网络优化项目部', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('656636605318041604', '工会', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');
INSERT INTO `sys_organizations` VALUES ('656636605318041605', '安全保卫部', NULL, 'admin', 'admin', '2019-01-15 16:43:03', '2019-01-15 16:43:03');

-- ----------------------------
-- Table structure for sys_pages
-- ----------------------------
DROP TABLE IF EXISTS `sys_pages`;
CREATE TABLE `sys_pages`  (
  `id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端界面权限名称',
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端界面权限链接',
  `parent_id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端界面权限父节点标识',
  `memo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '前端界面权限表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_pages
-- ----------------------------
INSERT INTO `sys_pages` VALUES ('656636605318041607', '接口权限维护', '/system/resource', '656636605318041618', NULL, 'admin', '2018-12-29 09:21:25', 'admin', '2018-12-29 09:21:25');
INSERT INTO `sys_pages` VALUES ('656636605318041609', '页面权限维护', '/system/accessPage', '656636605318041618', NULL, 'admin', '2018-12-29 09:21:25', 'admin', '2018-12-29 09:21:25');
INSERT INTO `sys_pages` VALUES ('656636605318041612', '日志查看', '/logger', NULL, NULL, 'admin', '2018-12-29 09:21:25', 'admin', '2018-12-29 09:21:25');
INSERT INTO `sys_pages` VALUES ('656636605318041613', '操作日志', '/logger/operation', '656636605318041612', NULL, 'admin', '2018-12-29 09:21:25', 'admin', '2018-12-29 09:21:25');
INSERT INTO `sys_pages` VALUES ('656636605318041614', '登陆日志', '/logger/login', '656636605318041612', NULL, 'admin', '2018-12-29 09:21:25', 'admin', '2018-12-29 09:21:25');
INSERT INTO `sys_pages` VALUES ('656636605318041615', '用户维护', '/system/user', '656636605318041618', NULL, 'admin', '2018-12-29 09:21:25', 'admin', '2018-12-29 09:21:25');
INSERT INTO `sys_pages` VALUES ('656636605318041618', '系统设置', '/system', NULL, NULL, 'admin', '2018-12-29 09:21:25', 'admin', '2018-12-29 09:21:25');
INSERT INTO `sys_pages` VALUES ('656636605318041619', '角色维护', '/system/role', '656636605318041618', NULL, 'admin', '2018-12-29 09:21:25', 'admin', '2018-12-29 09:21:25');
INSERT INTO `sys_pages` VALUES ('656636605318041620', '部门维护', '/system/organization', '656636605318041618', NULL, 'admin', '2018-12-29 09:21:25', 'admin', '2018-12-29 09:21:25');

-- ----------------------------
-- Table structure for sys_resources
-- ----------------------------
DROP TABLE IF EXISTS `sys_resources`;
CREATE TABLE `sys_resources`  (
  `id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接口权限标识',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口权限名称',
  `label` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口权限描述',
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口权限链接',
  `parent_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口权限父节点标识',
  `memo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '接口权限表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_resources
-- ----------------------------
INSERT INTO `sys_resources` VALUES ('656636605318041623', '前端路由下用户列表', '前端路由下用户列表', '/user/loadUserAccessPagesById', '656637382149279748', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('656636605318041626', '角色维护', '角色维护', '/role', NULL, NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('656636605318041627', '权限资源维护', '权限资源维护', '/resource', NULL, NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('656636605318041629', '单个用户信息', '单个用户信息', '/user/loadUserById', '656637382149279748', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('656636605318041630', '权限资源下用户列表', '权限资源下用户列表', '/user/loadUserResourcesById', '656637382149279748', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('656636605318041631', '用户列表', '用户列表', '/user/usersList', '656637382149279748', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('656636605318041632', '修改用户角色', '修改用户角色', '/user/updateUserRoles', '656637382149279748', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('656636605318041634', '新增用户', '新增用户', '/user/addSysUsers', '656637382149279748', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('656636605322235906', '登陆用户可访问前端路由列表', '登陆用户可访问前端路由列表', '/user/queryAccessPages', '656637382149279745', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('656636605322235907', '部门维护', '部门维护', '/organization', NULL, NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('656636605322235908', '更新用户', '更新用户', '/user/updateSysUsers', '656637382149279748', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('656636605322235909', '删除用户', '删除用户', '/user/deleteSysUsers', '656637382149279748', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('656637382149279744', '前端路由维护', '前端路由维护', '/accessPage', NULL, NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('656637382149279746', '修改用户可访问前端路由', '修改用户可访问前端路由', '/user/updateUserAccessPages', '656637382149279748', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('656637382149279748', '用户信息维护', '用户信息维护', '/user', NULL, NULL, 'admin', '2018-12-29 00:00:00', 'admin', '2018-12-29 00:00:00');
INSERT INTO `sys_resources` VALUES ('656637382149279750', '角色下用户列表', '角色下用户列表', '/user/loadUserRolesById', '656636605318041626', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('656637382149279754', '修改用户可访问权限资源', '修改用户可访问权限资源', '/user/updateUserResources', '656637382149279748', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('656637382149279755', '登陆用户信息', '登陆用户信息', '/user/queryLoginUserInfo', '656637382149279748', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');
INSERT INTO `sys_resources` VALUES ('656637382153474048', 'Token列表', 'Token列表', '/user/ssoToken', '656637382149279748', NULL, 'admin', '2018-12-29 09:55:27', 'admin', '2018-12-29 09:55:27');

-- ----------------------------
-- Table structure for sys_role_pages
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_pages`;
CREATE TABLE `sys_role_pages`  (
  `id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
  `role_id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色标识',
  `page_id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端界面权限标识',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_page_idx`(`role_id`, `page_id`) USING BTREE COMMENT 'role_id/page_id索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色授权前端界面表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_role_pages
-- ----------------------------
INSERT INTO `sys_role_pages` VALUES ('', NULL, NULL);
INSERT INTO `sys_role_pages` VALUES ('656637382153474059', '656637382153474050', '656636605318041609');
INSERT INTO `sys_role_pages` VALUES ('656637382153474062', '656637382153474050', '656636605318041612');
INSERT INTO `sys_role_pages` VALUES ('656637382153474063', '656637382153474050', '656636605318041613');
INSERT INTO `sys_role_pages` VALUES ('656637382153474064', '656637382153474050', '656636605318041614');
INSERT INTO `sys_role_pages` VALUES ('656637382153474065', '656637382153474050', '656636605318041615');
INSERT INTO `sys_role_pages` VALUES ('656637382153474068', '656637382153474050', '656636605318041618');
INSERT INTO `sys_role_pages` VALUES ('656637382153474069', '656637382153474050', '656636605318041619');
INSERT INTO `sys_role_pages` VALUES ('656637382153474070', '656637382153474050', '656636605318041620');
INSERT INTO `sys_role_pages` VALUES ('656637382153474074', '656637382153474051', '656636605318041612');
INSERT INTO `sys_role_pages` VALUES ('656637382153474075', '656637382153474051', '656636605318041613');
INSERT INTO `sys_role_pages` VALUES ('656637382153474076', '656637382153474051', '656636605318041614');

-- ----------------------------
-- Table structure for sys_role_resources
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_resources`;
CREATE TABLE `sys_role_resources`  (
  `id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
  `role_id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色标识',
  `resource_id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接口权限标识',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_resource_idx`(`role_id`, `resource_id`) USING BTREE COMMENT 'role_id/resource_id索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色授权接口权限表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_role_resources
-- ----------------------------
INSERT INTO `sys_role_resources` VALUES ('656637382153474078', '656637382153474050', '656636605318041623');
INSERT INTO `sys_role_resources` VALUES ('656637382153474081', '656637382153474050', '656636605318041626');
INSERT INTO `sys_role_resources` VALUES ('656637382153474082', '656637382153474050', '656636605318041627');
INSERT INTO `sys_role_resources` VALUES ('656637382153474084', '656637382153474050', '656636605318041629');
INSERT INTO `sys_role_resources` VALUES ('656637382153474085', '656637382153474050', '656636605318041630');
INSERT INTO `sys_role_resources` VALUES ('656637382153474086', '656637382153474050', '656636605318041631');
INSERT INTO `sys_role_resources` VALUES ('656637382153474087', '656637382153474050', '656636605318041632');
INSERT INTO `sys_role_resources` VALUES ('656637382153474089', '656637382153474050', '656636605318041634');
INSERT INTO `sys_role_resources` VALUES ('656637382153474092', '656637382153474050', '656636605322235906');
INSERT INTO `sys_role_resources` VALUES ('656637382157668352', '656637382153474050', '656636605322235907');
INSERT INTO `sys_role_resources` VALUES ('656637382157668353', '656637382153474050', '656636605322235908');
INSERT INTO `sys_role_resources` VALUES ('656637382157668354', '656637382153474050', '656636605322235909');
INSERT INTO `sys_role_resources` VALUES ('656637382157668356', '656637382153474050', '656637382149279744');
INSERT INTO `sys_role_resources` VALUES ('656637382157668358', '656637382153474050', '656637382149279746');
INSERT INTO `sys_role_resources` VALUES ('656637382157668360', '656637382153474050', '656637382149279748');
INSERT INTO `sys_role_resources` VALUES ('656637382157668362', '656637382153474050', '656637382149279750');
INSERT INTO `sys_role_resources` VALUES ('656637382157668366', '656637382153474050', '656637382149279754');
INSERT INTO `sys_role_resources` VALUES ('656637382157668367', '656637382153474050', '656637382149279755');
INSERT INTO `sys_role_resources` VALUES ('656637382157668368', '656637382153474050', '656637382153474048');
INSERT INTO `sys_role_resources` VALUES ('656637382157668370', '656637382153474051', '656636605318041623');
INSERT INTO `sys_role_resources` VALUES ('656637382157668372', '656637382153474051', '656636605318041629');
INSERT INTO `sys_role_resources` VALUES ('656637382157668373', '656637382153474051', '656636605318041630');
INSERT INTO `sys_role_resources` VALUES ('656637382157668376', '656637382153474051', '656636605322235906');
INSERT INTO `sys_role_resources` VALUES ('656637382157668377', '656637382153474051', '656636605322235908');
INSERT INTO `sys_role_resources` VALUES ('656637382157668380', '656637382153474051', '656637382149279748');
INSERT INTO `sys_role_resources` VALUES ('656637382157668381', '656637382153474051', '656637382149279750');
INSERT INTO `sys_role_resources` VALUES ('656637382157668384', '656637382153474051', '656637382149279755');

-- ----------------------------
-- Table structure for sys_roles
-- ----------------------------
DROP TABLE IF EXISTS `sys_roles`;
CREATE TABLE `sys_roles`  (
  `id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `label` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `memo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_roles
-- ----------------------------
INSERT INTO `sys_roles` VALUES ('656637382153474050', 'admin', '管理员', NULL, 'admin', '2018-12-29 09:04:05', 'admin', '2018-12-29 09:04:08');
INSERT INTO `sys_roles` VALUES ('656637382153474051', 'test', '测试', NULL, 'admin', '2018-12-29 09:04:08', 'admin', '2018-12-29 09:04:08');

-- ----------------------------
-- Table structure for sys_user_organizations
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_organizations`;
CREATE TABLE `sys_user_organizations`  (
  `id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `user_id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  `organization_id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_organization_idx`(`user_id`, `organization_id`) USING BTREE COMMENT 'user_id/organization_id索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户部门关系表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_user_organizations
-- ----------------------------
INSERT INTO `sys_user_organizations` VALUES ('656637382153474052', '656636605318041622', '656636605313847299');
INSERT INTO `sys_user_organizations` VALUES ('656637382153474053', '9b22bcdea4a09a5938', '656636605313847299');

-- ----------------------------
-- Table structure for sys_user_pages
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_pages`;
CREATE TABLE `sys_user_pages`  (
  `id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标志',
  `user_id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户标识',
  `page_id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '前端界面权限标识',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_page_idx`(`user_id`, `page_id`) USING BTREE COMMENT 'user_id/page_id索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户授权前端界面表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sys_user_resources
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_resources`;
CREATE TABLE `sys_user_resources`  (
  `id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
  `user_id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户标识',
  `resource_id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口权限标识',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_resource_idx`(`user_id`, `resource_id`) USING BTREE COMMENT 'user_id/resource_id索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户授权接口权限表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sys_user_roles
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_roles`;
CREATE TABLE `sys_user_roles`  (
  `id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识',
  `user_id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户标识',
  `role_id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色标识',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_role_idx`(`user_id`, `role_id`) USING BTREE COMMENT 'user_id/role_id索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户授权角色表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_user_roles
-- ----------------------------
INSERT INTO `sys_user_roles` VALUES ('656637382153474055', '656636605318041621', '656637382153474051');
INSERT INTO `sys_user_roles` VALUES ('656637382153474054', '656636605318041622', '656637382153474050');

-- ----------------------------
-- Table structure for sys_users
-- ----------------------------
DROP TABLE IF EXISTS `sys_users`;
CREATE TABLE `sys_users`  (
  `id` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '帐号标识',
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
  `create_user` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_users
-- ----------------------------
INSERT INTO `sys_users` VALUES ('656636605318041621', 'test', '9b22bcdea4a09a59382323a479982c10', 'chenweiqiao@tisson.cn', '18912345678', '测试', 'S0A', NULL, 0, NULL, '2018-12-29 00:00:00', -1, '2058-12-29 00:00:00', 'admin', '2018-12-29 00:00:00', 'admin', '2018-12-29 00:00:00');
INSERT INTO `sys_users` VALUES ('656636605318041622', 'admin', '9b22bcdea4a09a59382323a479982c10', 'chenweiqiao@tisson.cn', '18912345678', '管理员', 'S0A', NULL, 0, NULL, '2018-12-29 09:02:44', -1, '2058-12-29 09:03:00', 'admin', '2018-12-29 09:03:28', 'admin', '2018-12-29 09:03:37');

SET FOREIGN_KEY_CHECKS = 1;
