/*
 Navicat Premium Data Transfer

 Source Server         : localhost-MYSQL
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : 127.0.0.1:3306
 Source Schema         : schema

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 11/12/2020 19:14:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_estonian_ci NOT NULL,
  `password` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('admin', '21232f297a57a5a743894a0e4a801fc3');

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `content_title` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `content_page` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `publish_date` date NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of news
-- ----------------------------
INSERT INTO `news` VALUES (1, '1：Web开发基础及Java EE开发环境搭建', '', '2015-10-24');
INSERT INTO `news` VALUES (2, '2：使用JSP技术开发Web项目', '', '2015-10-15');
INSERT INTO `news` VALUES (3, '3：Servlet组件、使用MVC模式开发Web项目', '', '2015-10-15');
INSERT INTO `news` VALUES (4, '4：MyBatis框架', '', '2015-10-15');
INSERT INTO `news` VALUES (5, '5：Spring MVC框架', '', '2015-10-15');
INSERT INTO `news` VALUES (6, '6：Spring框架', '', '2015-10-15');
INSERT INTO `news` VALUES (7, '7：使用SSM整合开发Web项目', '', '2015-12-01');
INSERT INTO `news` VALUES (8, '8：Spring Boot框架', '', '2015-12-01');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `username` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `realname` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mobile` char(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `age` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('chenjiu', '777', '陈久', '13700000009', 99);
INSERT INTO `user` VALUES ('lisi', '222', '李四', '13700000004', 44);
INSERT INTO `user` VALUES ('qianqi', '555', '钱七', '13700000007', 77);
INSERT INTO `user` VALUES ('qwe', '123', 'qwe', '123', 21);
INSERT INTO `user` VALUES ('sunba', '666', '孙八', '13700000008', 88);
INSERT INTO `user` VALUES ('wangwu', '333', '王五', '13700000005', 55);
INSERT INTO `user` VALUES ('zhangsan', '111', '张三', '13700000003', 33);
INSERT INTO `user` VALUES ('zhaoliu', '444', '赵六', '13700000006', 66);

SET FOREIGN_KEY_CHECKS = 1;
