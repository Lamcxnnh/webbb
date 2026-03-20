/*
 Navicat Premium Dump SQL

 Source Server         : 192.168.64.100_Linux_mysql8
 Source Server Type    : MySQL
 Source Server Version : 80408 (8.4.8)
 Source Host           : 192.168.64.100:3306
 Source Schema         : leasetest

 Target Server Type    : MySQL
 Target Server Version : 80408 (8.4.8)
 File Encoding         : 65001

 Date: 21/03/2026 00:52:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_users
-- ----------------------------
DROP TABLE IF EXISTS `admin_users`;
CREATE TABLE `admin_users`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID（主键）',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码（加密存储）',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `user_type` tinyint NOT NULL COMMENT '用户类型（1-超级管理员，2-普通管理员）',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号码',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像地址',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '备注信息',
  `position_id` int NOT NULL COMMENT '岗位ID（外键）',
  `account_status` tinyint NOT NULL DEFAULT 1 COMMENT '账号状态（0-禁用，1-正常）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE,
  INDEX `idx_position_id`(`position_id` ASC) USING BTREE,
  CONSTRAINT `fk_admin_user_position` FOREIGN KEY (`position_id`) REFERENCES `positions` (`position_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '后台管理系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_users
-- ----------------------------

-- ----------------------------
-- Table structure for apartment_facility_rels
-- ----------------------------
DROP TABLE IF EXISTS `apartment_facility_rels`;
CREATE TABLE `apartment_facility_rels`  (
  `rel_id` int NOT NULL AUTO_INCREMENT COMMENT '关系ID（主键）',
  `apartment_id` int NOT NULL COMMENT '公寓ID（外键）',
  `facility_id` int NOT NULL COMMENT '配套ID（外键）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`rel_id`) USING BTREE,
  UNIQUE INDEX `uk_apartment_facility`(`apartment_id` ASC, `facility_id` ASC) USING BTREE,
  INDEX `idx_facility_id`(`facility_id` ASC) USING BTREE,
  CONSTRAINT `fk_af_rel_apartment` FOREIGN KEY (`apartment_id`) REFERENCES `apartments` (`apartment_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_af_rel_facility` FOREIGN KEY (`facility_id`) REFERENCES `facilities` (`facility_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公寓-配套关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of apartment_facility_rels
-- ----------------------------

-- ----------------------------
-- Table structure for apartment_fee_rels
-- ----------------------------
DROP TABLE IF EXISTS `apartment_fee_rels`;
CREATE TABLE `apartment_fee_rels`  (
  `rel_id` int NOT NULL AUTO_INCREMENT COMMENT '关系ID（主键）',
  `apartment_id` int NOT NULL COMMENT '公寓ID（外键）',
  `fee_value_id` int NOT NULL COMMENT '杂费值ID（外键）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`rel_id`) USING BTREE,
  UNIQUE INDEX `uk_apartment_fee`(`apartment_id` ASC, `fee_value_id` ASC) USING BTREE,
  INDEX `idx_fee_value_id`(`fee_value_id` ASC) USING BTREE,
  CONSTRAINT `fk_afr_rel_apartment` FOREIGN KEY (`apartment_id`) REFERENCES `apartments` (`apartment_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_afr_rel_fee_value` FOREIGN KEY (`fee_value_id`) REFERENCES `misc_fee_values` (`fee_value_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公寓-杂费值关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of apartment_fee_rels
-- ----------------------------

-- ----------------------------
-- Table structure for apartment_tag_rels
-- ----------------------------
DROP TABLE IF EXISTS `apartment_tag_rels`;
CREATE TABLE `apartment_tag_rels`  (
  `rel_id` int NOT NULL AUTO_INCREMENT COMMENT '关系ID（主键）',
  `apartment_id` int NOT NULL COMMENT '公寓ID（外键）',
  `tag_id` int NOT NULL COMMENT '标签ID（外键）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`rel_id`) USING BTREE,
  UNIQUE INDEX `uk_apartment_tag`(`apartment_id` ASC, `tag_id` ASC) USING BTREE,
  INDEX `idx_tag_id`(`tag_id` ASC) USING BTREE,
  CONSTRAINT `fk_at_rel_apartment` FOREIGN KEY (`apartment_id`) REFERENCES `apartments` (`apartment_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_at_rel_tag` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`tag_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公寓-标签关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of apartment_tag_rels
-- ----------------------------

-- ----------------------------
-- Table structure for apartments
-- ----------------------------
DROP TABLE IF EXISTS `apartments`;
CREATE TABLE `apartments`  (
  `apartment_id` int NOT NULL AUTO_INCREMENT COMMENT '公寓ID（主键）',
  `apartment_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公寓名称',
  `apartment_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '公寓介绍',
  `district_id` int NOT NULL COMMENT '所在区县ID（外键）',
  `district_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所在区县名称',
  `city_id` int NOT NULL COMMENT '所在城市ID（外键）',
  `city_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所在城市名称',
  `province_id` int NOT NULL COMMENT '所在省份ID（外键）',
  `province_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所在省份名称',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '详细地址',
  `longitude` decimal(10, 7) NULL DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10, 7) NULL DEFAULT NULL COMMENT '纬度',
  `front_desk_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公寓前台电话',
  `is_published` tinyint NOT NULL DEFAULT 0 COMMENT '是否发布（0-否，1-是）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`apartment_id`) USING BTREE,
  INDEX `idx_district_id`(`district_id` ASC) USING BTREE,
  INDEX `idx_city_id`(`city_id` ASC) USING BTREE,
  INDEX `idx_province_id`(`province_id` ASC) USING BTREE,
  CONSTRAINT `fk_apartments_city` FOREIGN KEY (`city_id`) REFERENCES `cities` (`city_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_apartments_district` FOREIGN KEY (`district_id`) REFERENCES `districts` (`district_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_apartments_province` FOREIGN KEY (`province_id`) REFERENCES `provinces` (`province_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公寓信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of apartments
-- ----------------------------

-- ----------------------------
-- Table structure for appointments
-- ----------------------------
DROP TABLE IF EXISTS `appointments`;
CREATE TABLE `appointments`  (
  `appointment_id` int NOT NULL AUTO_INCREMENT COMMENT '预约ID（主键）',
  `user_id` int NOT NULL COMMENT '用户ID（外键）',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户姓名',
  `user_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户手机号码',
  `apartment_id` int NOT NULL COMMENT '公寓ID（外键）',
  `appointment_time` datetime NOT NULL COMMENT '预约时间',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '备注信息',
  `appointment_status` tinyint NOT NULL DEFAULT 0 COMMENT '预约状态（0-待确认，1-已确认，2-已取消）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`appointment_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_apartment_id`(`apartment_id` ASC) USING BTREE,
  CONSTRAINT `fk_appointment_apartment` FOREIGN KEY (`apartment_id`) REFERENCES `apartments` (`apartment_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_appointment_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '预约看房信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of appointments
-- ----------------------------

-- ----------------------------
-- Table structure for attribute_names
-- ----------------------------
DROP TABLE IF EXISTS `attribute_names`;
CREATE TABLE `attribute_names`  (
  `attr_id` int NOT NULL AUTO_INCREMENT COMMENT '属性ID（主键）',
  `attr_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '属性名称',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`attr_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '属性名称表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of attribute_names
-- ----------------------------

-- ----------------------------
-- Table structure for attribute_values
-- ----------------------------
DROP TABLE IF EXISTS `attribute_values`;
CREATE TABLE `attribute_values`  (
  `attr_value_id` int NOT NULL AUTO_INCREMENT COMMENT '属性值ID（主键）',
  `attr_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '属性值',
  `attr_id` int NOT NULL COMMENT '对应属性名称ID（外键）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`attr_value_id`) USING BTREE,
  INDEX `idx_attr_id`(`attr_id` ASC) USING BTREE,
  CONSTRAINT `fk_attr_value_name` FOREIGN KEY (`attr_id`) REFERENCES `attribute_names` (`attr_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '属性值表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of attribute_values
-- ----------------------------

-- ----------------------------
-- Table structure for browse_histories
-- ----------------------------
DROP TABLE IF EXISTS `browse_histories`;
CREATE TABLE `browse_histories`  (
  `history_id` int NOT NULL AUTO_INCREMENT COMMENT '历史ID（主键）',
  `user_id` int NOT NULL COMMENT '用户ID（外键）',
  `room_id` int NOT NULL COMMENT '房间ID（外键）',
  `browse_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`history_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_room_id`(`room_id` ASC) USING BTREE,
  CONSTRAINT `fk_browse_history_room` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_browse_history_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '浏览历史表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of browse_histories
-- ----------------------------

-- ----------------------------
-- Table structure for cities
-- ----------------------------
DROP TABLE IF EXISTS `cities`;
CREATE TABLE `cities`  (
  `city_id` int NOT NULL AUTO_INCREMENT COMMENT '城市ID（主键）',
  `city_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '城市名称',
  `province_id` int NOT NULL COMMENT '所属省份ID（外键）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`city_id`) USING BTREE,
  INDEX `idx_province_id`(`province_id` ASC) USING BTREE,
  CONSTRAINT `fk_cities_province` FOREIGN KEY (`province_id`) REFERENCES `provinces` (`province_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '城市信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cities
-- ----------------------------

-- ----------------------------
-- Table structure for districts
-- ----------------------------
DROP TABLE IF EXISTS `districts`;
CREATE TABLE `districts`  (
  `district_id` int NOT NULL AUTO_INCREMENT COMMENT '区县ID（主键）',
  `district_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '区县名称',
  `city_id` int NOT NULL COMMENT '所属城市ID（外键）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`district_id`) USING BTREE,
  INDEX `idx_city_id`(`city_id` ASC) USING BTREE,
  CONSTRAINT `fk_districts_city` FOREIGN KEY (`city_id`) REFERENCES `cities` (`city_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '区县信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of districts
-- ----------------------------

-- ----------------------------
-- Table structure for facilities
-- ----------------------------
DROP TABLE IF EXISTS `facilities`;
CREATE TABLE `facilities`  (
  `facility_id` int NOT NULL AUTO_INCREMENT COMMENT '配套ID（主键）',
  `object_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属对象类型（apartment/room）',
  `facility_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配套名称',
  `facility_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '配套图标地址',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`facility_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '配套信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of facilities
-- ----------------------------

-- ----------------------------
-- Table structure for images
-- ----------------------------
DROP TABLE IF EXISTS `images`;
CREATE TABLE `images`  (
  `image_id` int NOT NULL AUTO_INCREMENT COMMENT '图片ID（主键）',
  `image_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片名称',
  `object_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属对象类型（apartment/room）',
  `object_id` int NOT NULL COMMENT '所属对象ID',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片地址',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`image_id`) USING BTREE,
  INDEX `idx_object`(`object_type` ASC, `object_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图片信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of images
-- ----------------------------

-- ----------------------------
-- Table structure for lease_terms
-- ----------------------------
DROP TABLE IF EXISTS `lease_terms`;
CREATE TABLE `lease_terms`  (
  `term_id` int NOT NULL AUTO_INCREMENT COMMENT '租期ID（主键）',
  `term_count` int NOT NULL COMMENT '租期数',
  `term_unit` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租期单位（月/季/年）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`term_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租期信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lease_terms
-- ----------------------------

-- ----------------------------
-- Table structure for leases
-- ----------------------------
DROP TABLE IF EXISTS `leases`;
CREATE TABLE `leases`  (
  `lease_id` int NOT NULL AUTO_INCREMENT COMMENT '租约ID（主键）',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '承租人手机号',
  `tenant_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '承租人姓名',
  `tenant_id_card` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '承租人身份证号',
  `apartment_id` int NOT NULL COMMENT '签约公寓ID（外键）',
  `room_id` int NOT NULL COMMENT '签约房间ID（外键）',
  `start_date` date NOT NULL COMMENT '租约开始日期',
  `end_date` date NOT NULL COMMENT '租约结束日期',
  `term_id` int NOT NULL COMMENT '租期ID（外键）',
  `rent` decimal(10, 2) NOT NULL COMMENT '租金',
  `deposit` decimal(10, 2) NOT NULL COMMENT '押金',
  `payment_id` int NOT NULL COMMENT '支付方式ID（外键）',
  `lease_status` tinyint NOT NULL DEFAULT 0 COMMENT '租约状态（0-待生效，1-生效中，2-已到期，3-已解约）',
  `lease_source` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租约来源',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '备注信息',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`lease_id`) USING BTREE,
  INDEX `idx_apartment_id`(`apartment_id` ASC) USING BTREE,
  INDEX `idx_room_id`(`room_id` ASC) USING BTREE,
  INDEX `idx_term_id`(`term_id` ASC) USING BTREE,
  INDEX `idx_payment_id`(`payment_id` ASC) USING BTREE,
  CONSTRAINT `fk_lease_apartment` FOREIGN KEY (`apartment_id`) REFERENCES `apartments` (`apartment_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_lease_payment` FOREIGN KEY (`payment_id`) REFERENCES `payment_methods` (`payment_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_lease_room` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_lease_term` FOREIGN KEY (`term_id`) REFERENCES `lease_terms` (`term_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租约信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of leases
-- ----------------------------

-- ----------------------------
-- Table structure for misc_fee_names
-- ----------------------------
DROP TABLE IF EXISTS `misc_fee_names`;
CREATE TABLE `misc_fee_names`  (
  `fee_id` int NOT NULL AUTO_INCREMENT COMMENT '杂费ID（主键）',
  `fee_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '杂费名称',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`fee_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '杂费名称表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of misc_fee_names
-- ----------------------------

-- ----------------------------
-- Table structure for misc_fee_values
-- ----------------------------
DROP TABLE IF EXISTS `misc_fee_values`;
CREATE TABLE `misc_fee_values`  (
  `fee_value_id` int NOT NULL AUTO_INCREMENT COMMENT '杂费值ID（主键）',
  `fee_value_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '杂费值名称',
  `fee_unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '杂费值单位',
  `fee_id` int NOT NULL COMMENT '对应杂费名称ID（外键）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`fee_value_id`) USING BTREE,
  INDEX `idx_fee_id`(`fee_id` ASC) USING BTREE,
  CONSTRAINT `fk_misc_fee_value_name` FOREIGN KEY (`fee_id`) REFERENCES `misc_fee_names` (`fee_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '杂费值表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of misc_fee_values
-- ----------------------------

-- ----------------------------
-- Table structure for payment_methods
-- ----------------------------
DROP TABLE IF EXISTS `payment_methods`;
CREATE TABLE `payment_methods`  (
  `payment_id` int NOT NULL AUTO_INCREMENT COMMENT '支付方式ID（主键）',
  `payment_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付方式名称',
  `pay_term_count` int NOT NULL COMMENT '每次支付租期数',
  `extra_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '附加信息',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`payment_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付方式表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payment_methods
-- ----------------------------

-- ----------------------------
-- Table structure for positions
-- ----------------------------
DROP TABLE IF EXISTS `positions`;
CREATE TABLE `positions`  (
  `position_id` int NOT NULL AUTO_INCREMENT COMMENT '岗位ID（主键）',
  `position_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位编码',
  `position_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位名称',
  `position_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '描述信息',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`position_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '岗位信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of positions
-- ----------------------------

-- ----------------------------
-- Table structure for provinces
-- ----------------------------
DROP TABLE IF EXISTS `provinces`;
CREATE TABLE `provinces`  (
  `province_id` int NOT NULL AUTO_INCREMENT COMMENT '省份ID（主键）',
  `province_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '省份名称',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`province_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '省份信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of provinces
-- ----------------------------

-- ----------------------------
-- Table structure for room_attr_rels
-- ----------------------------
DROP TABLE IF EXISTS `room_attr_rels`;
CREATE TABLE `room_attr_rels`  (
  `rel_id` int NOT NULL AUTO_INCREMENT COMMENT '关系ID（主键）',
  `room_id` int NOT NULL COMMENT '房间ID（外键）',
  `attr_value_id` int NOT NULL COMMENT '属性值ID（外键）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`rel_id`) USING BTREE,
  UNIQUE INDEX `uk_room_attr`(`room_id` ASC, `attr_value_id` ASC) USING BTREE,
  INDEX `idx_attr_value_id`(`attr_value_id` ASC) USING BTREE,
  CONSTRAINT `fk_ra_rel_attr_value` FOREIGN KEY (`attr_value_id`) REFERENCES `attribute_values` (`attr_value_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_ra_rel_room` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '房间-属性值关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room_attr_rels
-- ----------------------------

-- ----------------------------
-- Table structure for room_facility_rels
-- ----------------------------
DROP TABLE IF EXISTS `room_facility_rels`;
CREATE TABLE `room_facility_rels`  (
  `rel_id` int NOT NULL AUTO_INCREMENT COMMENT '关系ID（主键）',
  `room_id` int NOT NULL COMMENT '房间ID（外键）',
  `facility_id` int NOT NULL COMMENT '配套ID（外键）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`rel_id`) USING BTREE,
  UNIQUE INDEX `uk_room_facility`(`room_id` ASC, `facility_id` ASC) USING BTREE,
  INDEX `idx_facility_id`(`facility_id` ASC) USING BTREE,
  CONSTRAINT `fk_rf_rel_facility` FOREIGN KEY (`facility_id`) REFERENCES `facilities` (`facility_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_rf_rel_room` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '房间-配套关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room_facility_rels
-- ----------------------------

-- ----------------------------
-- Table structure for room_payment_rels
-- ----------------------------
DROP TABLE IF EXISTS `room_payment_rels`;
CREATE TABLE `room_payment_rels`  (
  `rel_id` int NOT NULL AUTO_INCREMENT COMMENT '关系ID（主键）',
  `room_id` int NOT NULL COMMENT '房间ID（外键）',
  `payment_id` int NOT NULL COMMENT '支付方式ID（外键）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`rel_id`) USING BTREE,
  UNIQUE INDEX `uk_room_payment`(`room_id` ASC, `payment_id` ASC) USING BTREE,
  INDEX `idx_payment_id`(`payment_id` ASC) USING BTREE,
  CONSTRAINT `fk_rp_rel_payment` FOREIGN KEY (`payment_id`) REFERENCES `payment_methods` (`payment_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_rp_rel_room` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '房间-支付方式关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room_payment_rels
-- ----------------------------

-- ----------------------------
-- Table structure for room_tag_rels
-- ----------------------------
DROP TABLE IF EXISTS `room_tag_rels`;
CREATE TABLE `room_tag_rels`  (
  `rel_id` int NOT NULL AUTO_INCREMENT COMMENT '关系ID（主键）',
  `room_id` int NOT NULL COMMENT '房间ID（外键）',
  `tag_id` int NOT NULL COMMENT '标签ID（外键）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`rel_id`) USING BTREE,
  UNIQUE INDEX `uk_room_tag`(`room_id` ASC, `tag_id` ASC) USING BTREE,
  INDEX `idx_tag_id`(`tag_id` ASC) USING BTREE,
  CONSTRAINT `fk_rtag_rel_room` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_rtag_rel_tag` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`tag_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '房间-标签关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room_tag_rels
-- ----------------------------

-- ----------------------------
-- Table structure for room_term_rels
-- ----------------------------
DROP TABLE IF EXISTS `room_term_rels`;
CREATE TABLE `room_term_rels`  (
  `rel_id` int NOT NULL AUTO_INCREMENT COMMENT '关系ID（主键）',
  `room_id` int NOT NULL COMMENT '房间ID（外键）',
  `term_id` int NOT NULL COMMENT '租期ID（外键）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`rel_id`) USING BTREE,
  UNIQUE INDEX `uk_room_term`(`room_id` ASC, `term_id` ASC) USING BTREE,
  INDEX `idx_term_id`(`term_id` ASC) USING BTREE,
  CONSTRAINT `fk_rt_rel_room` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_rt_rel_term` FOREIGN KEY (`term_id`) REFERENCES `lease_terms` (`term_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '房间-租期关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room_term_rels
-- ----------------------------

-- ----------------------------
-- Table structure for rooms
-- ----------------------------
DROP TABLE IF EXISTS `rooms`;
CREATE TABLE `rooms`  (
  `room_id` int NOT NULL AUTO_INCREMENT COMMENT '房间ID（主键）',
  `room_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '房间号',
  `rent` decimal(10, 2) NOT NULL COMMENT '租金',
  `apartment_id` int NOT NULL COMMENT '所属公寓ID（外键）',
  `is_published` tinyint NOT NULL DEFAULT 0 COMMENT '是否发布（0-否，1-是）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`room_id`) USING BTREE,
  INDEX `idx_apartment_id`(`apartment_id` ASC) USING BTREE,
  CONSTRAINT `fk_rooms_apartment` FOREIGN KEY (`apartment_id`) REFERENCES `apartments` (`apartment_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '房间信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rooms
-- ----------------------------

-- ----------------------------
-- Table structure for tags
-- ----------------------------
DROP TABLE IF EXISTS `tags`;
CREATE TABLE `tags`  (
  `tag_id` int NOT NULL AUTO_INCREMENT COMMENT '标签ID（主键）',
  `object_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属对象类型（apartment/room）',
  `tag_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名称',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`tag_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '标签信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tags
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID（主键）',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号码',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码（加密存储）',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像地址',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `account_status` tinyint NOT NULL DEFAULT 1 COMMENT '账号状态（0-禁用，1-正常）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `uk_phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
