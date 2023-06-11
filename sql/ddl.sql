/*
SQLyog Ultimate v10.00 Beta1
MySQL - 8.0.26 : Database - kaiapi
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`kaiapi` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `kaiapi`;



DROP TABLE IF EXISTS `alipay_info`;

CREATE TABLE `alipay_info` (
                               `orderSn` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单id',
                               `subject` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '交易名称',
                               `totalAmount` decimal(10,2) NOT NULL COMMENT '交易金额',
                               `buyerPayAmount` decimal(10,2) NOT NULL COMMENT '买家付款金额',
                               `buyerId` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '买家在支付宝的唯一id',
                               `tradeNo` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付宝交易凭证号',
                               `tradeStatus` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '交易状态',
                               `gmtPayment` datetime NOT NULL COMMENT '买家付款时间',
                               PRIMARY KEY (`orderSn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;




/*Table structure for table `api_order` */

DROP TABLE IF EXISTS `api_order`;

CREATE TABLE `api_order` (
                             `id` bigint NOT NULL COMMENT '主键',
                             `interfaceId` bigint NOT NULL COMMENT '接口id',
                             `userId` bigint NOT NULL COMMENT '用户id',
                             `orderSn` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号',
                             `orderNum` bigint NOT NULL COMMENT '购买数量',
                             `charging` decimal(10,2) NOT NULL COMMENT '单价',
                             `totalAmount` decimal(10,2) NOT NULL COMMENT '交易金额',
                             `status` int NOT NULL COMMENT '交易状态【0->待付款；1->已完成；2->无效订单】',
                             `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;



/*Table structure for table `interface_info` */

DROP TABLE IF EXISTS `interface_info`;

CREATE TABLE `interface_info` (
                                  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                  `name` varchar(256) NOT NULL COMMENT '名称',
                                  `description` varchar(256) DEFAULT NULL COMMENT '描述',
                                  `url` varchar(512) NOT NULL COMMENT '接口地址',
                                  `requestHeader` text COMMENT '请求头',
                                  `responseHeader` text COMMENT '响应头',
                                  `isFree` tinyint DEFAULT '0' COMMENT '免费接口 0- 不免费 1- 免费',
                                  `status` int NOT NULL DEFAULT '0' COMMENT '接口状态 （0 - 关闭，1 - 开启）',
                                  `method` varchar(256) NOT NULL COMMENT '请求类型',
                                  `userId` bigint NOT NULL COMMENT '创建',
                                  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
                                  `requestParams` text COMMENT '请求头',
                                  `charging` decimal(10,2) DEFAULT NULL COMMENT '收费标准（元/条）',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb3 COMMENT='接口信息';



/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
                        `id` bigint NOT NULL COMMENT 'id',
                        `userName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户昵称',
                        `userAccount` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账号',
                        `userAvatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户头像',
                        `gender` tinyint DEFAULT NULL COMMENT '性别',
                        `userRole` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user / admin',
                        `userPassword` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '密码',
                        `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号',
                        `accessKey` varchar(512) DEFAULT NULL COMMENT 'ak',
                        `secretKey` varchar(512) DEFAULT NULL COMMENT '密钥',
                        `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
                        PRIMARY KEY (`id`) USING BTREE,
                        UNIQUE KEY `uni_userAccount` (`userAccount`) USING BTREE,
                        UNIQUE KEY `uni_mobile` (`mobile`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户';



DROP TABLE IF EXISTS `user_interface_info`;

CREATE TABLE `user_interface_info` (
                                       `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                       `userId` bigint NOT NULL COMMENT '用户id',
                                       `interfaceInfoId` bigint NOT NULL COMMENT '接口id',
                                       `totalNum` int NOT NULL DEFAULT '0' COMMENT '总的调用次数）',
                                       `leftNum` int NOT NULL DEFAULT '0' COMMENT '剩余调用次数）',
                                       `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3 COMMENT='用户接口信息关系表';
