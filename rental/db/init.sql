-- ============================================
-- Rental 租房平台 - 数据库初始化脚本
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS rental_db
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;
USE rental_db;

-- ==================== 地区信息表 ====================
DROP TABLE IF EXISTS district_info;
CREATE TABLE district_info (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '地区ID',
    parent_id   BIGINT       NOT NULL DEFAULT 0 COMMENT '父级ID(0=省份)',
    name        VARCHAR(64)  NOT NULL COMMENT '地区名称',
    level       VARCHAR(16)  NOT NULL COMMENT '级别: province/city/district',
    sort_order  INT          NOT NULL DEFAULT 0 COMMENT '排序',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除(0=未删除,1=已删除)',
    PRIMARY KEY (id),
    INDEX idx_parent_id (parent_id),
    INDEX idx_level (level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='地区信息表';

-- ==================== 属性名称表 ====================
DROP TABLE IF EXISTS attr_key;
CREATE TABLE attr_key (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '属性名称ID',
    name        VARCHAR(64)  NOT NULL COMMENT '属性名称',
    sort_order  INT          NOT NULL DEFAULT 0 COMMENT '排序',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='属性名称表';

-- ==================== 属性值表 ====================
DROP TABLE IF EXISTS attr_value;
CREATE TABLE attr_value (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '属性值ID',
    key_id      BIGINT       NOT NULL COMMENT '属性名称ID',
    name        VARCHAR(64)  NOT NULL COMMENT '属性值名称',
    sort_order  INT          NOT NULL DEFAULT 0 COMMENT '排序',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    INDEX idx_key_id (key_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='属性值表';

-- ==================== 标签信息表 ====================
DROP TABLE IF EXISTS label_info;
CREATE TABLE label_info (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '标签ID',
    type        VARCHAR(32)  NOT NULL DEFAULT 'room' COMMENT '标签类型: room/apartment',
    name        VARCHAR(64)  NOT NULL COMMENT '标签名称',
    sort_order  INT          NOT NULL DEFAULT 0 COMMENT '排序',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    INDEX idx_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签信息表';

-- ==================== 配套信息表 ====================
DROP TABLE IF EXISTS facility_info;
CREATE TABLE facility_info (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '配套ID',
    name        VARCHAR(64)  NOT NULL COMMENT '配套名称',
    icon        VARCHAR(255) DEFAULT NULL COMMENT '图标URL',
    sort_order  INT          NOT NULL DEFAULT 0 COMMENT '排序',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='配套信息表';

-- ==================== 支付方式表 ====================
DROP TABLE IF EXISTS payment_type;
CREATE TABLE payment_type (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '支付方式ID',
    name        VARCHAR(64)  NOT NULL COMMENT '支付方式名称',
    sort_order  INT          NOT NULL DEFAULT 0 COMMENT '排序',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付方式表';

-- ==================== 租期表 ====================
DROP TABLE IF EXISTS lease_term;
CREATE TABLE lease_term (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '租期ID',
    month_count INT          NOT NULL COMMENT '月数',
    unit        VARCHAR(16)  NOT NULL DEFAULT 'month' COMMENT '单位',
    sort_order  INT          NOT NULL DEFAULT 0 COMMENT '排序',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租期表';

-- ==================== 公寓信息表 ====================
DROP TABLE IF EXISTS apartment_info;
CREATE TABLE apartment_info (
    id            BIGINT        NOT NULL AUTO_INCREMENT COMMENT '公寓ID',
    name          VARCHAR(128)  NOT NULL COMMENT '公寓名称',
    intro         TEXT          COMMENT '公寓介绍',
    district_id   BIGINT        NOT NULL COMMENT '区县ID',
    detail_address VARCHAR(255) COMMENT '详细地址',
    latitude      DECIMAL(10,7) COMMENT '纬度',
    longitude     DECIMAL(10,7) COMMENT '经度',
    cover_url     VARCHAR(512)  COMMENT '封面图片URL',
    images        JSON          COMMENT '公寓图片列表(JSON数组)',
    facility_ids  JSON          COMMENT '配套ID列表(JSON数组)',
    phone         VARCHAR(32)   COMMENT '联系电话',
    is_release    TINYINT       NOT NULL DEFAULT 0 COMMENT '发布状态(0=未发布,1=已发布)',
    sort_order    INT           NOT NULL DEFAULT 0 COMMENT '排序',
    create_time   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted       TINYINT       NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    INDEX idx_district_id (district_id),
    INDEX idx_is_release (is_release)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公寓信息表';

-- ==================== 房间信息表 ====================
DROP TABLE IF EXISTS room_info;
CREATE TABLE room_info (
    id            BIGINT        NOT NULL AUTO_INCREMENT COMMENT '房间ID',
    apartment_id  BIGINT        NOT NULL COMMENT '所属公寓ID',
    name          VARCHAR(128)  NOT NULL COMMENT '房间名称',
    intro         TEXT          COMMENT '房间介绍',
    price         DECIMAL(10,2) NOT NULL COMMENT '租金(元/月)',
    area          DECIMAL(8,2)  COMMENT '面积(㎡)',
    floor         VARCHAR(32)   COMMENT '楼层',
    room_type     VARCHAR(32)   COMMENT '户型(如: 1室1厅)',
    cover_url     VARCHAR(512)  COMMENT '封面图片URL',
    images        JSON          COMMENT '房间图片列表(JSON数组)',
    attr_value_ids JSON         COMMENT '属性值ID列表(JSON数组)',
    is_release    TINYINT       NOT NULL DEFAULT 0 COMMENT '发布状态(0=未发布,1=已发布)',
    sort_order    INT           NOT NULL DEFAULT 0 COMMENT '排序',
    create_time   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted       TINYINT       NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    INDEX idx_apartment_id (apartment_id),
    INDEX idx_is_release (is_release)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房间信息表';

-- ==================== 房间-支付方式关联表 ====================
DROP TABLE IF EXISTS room_payment;
CREATE TABLE room_payment (
    id               BIGINT   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    room_id          BIGINT   NOT NULL COMMENT '房间ID',
    payment_type_id  BIGINT   NOT NULL COMMENT '支付方式ID',
    create_time      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted          TINYINT  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    INDEX idx_room_id (room_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房间支付方式关联表';

-- ==================== 房间-租期关联表 ====================
DROP TABLE IF EXISTS room_lease_term;
CREATE TABLE room_lease_term (
    id             BIGINT   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    room_id        BIGINT   NOT NULL COMMENT '房间ID',
    lease_term_id  BIGINT   NOT NULL COMMENT '租期ID',
    create_time    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted        TINYINT  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    INDEX idx_room_id (room_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房间租期关联表';

-- ==================== 房间-标签关联表 ====================
DROP TABLE IF EXISTS room_label;
CREATE TABLE room_label (
    id          BIGINT   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    room_id     BIGINT   NOT NULL COMMENT '房间ID',
    label_id    BIGINT   NOT NULL COMMENT '标签ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     TINYINT  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    INDEX idx_room_id (room_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房间标签关联表';

-- ==================== 房间-配套关联表 ====================
DROP TABLE IF EXISTS room_facility;
CREATE TABLE room_facility (
    id          BIGINT   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    room_id     BIGINT   NOT NULL COMMENT '房间ID',
    facility_id BIGINT   NOT NULL COMMENT '配套ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     TINYINT  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    INDEX idx_room_id (room_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房间配套关联表';

-- ==================== 管理员用户表 ====================
DROP TABLE IF EXISTS system_user;
CREATE TABLE system_user (
    id            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
    username      VARCHAR(64)  NOT NULL COMMENT '用户名',
    password      VARCHAR(128) NOT NULL COMMENT '密码(加密)',
    name          VARCHAR(64)  COMMENT '姓名',
    avatar_url    VARCHAR(512) COMMENT '头像URL',
    phone         VARCHAR(20)  COMMENT '手机号',
    type          VARCHAR(16)  NOT NULL DEFAULT 'admin' COMMENT '类型: super=超级管理员, admin=普通管理员',
    status        TINYINT      NOT NULL DEFAULT 1 COMMENT '状态(0=禁用,1=启用)',
    last_login_time DATETIME   COMMENT '最后登录时间',
    create_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted       TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    UNIQUE INDEX uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员用户表';

-- ==================== 移动端用户表 ====================
DROP TABLE IF EXISTS user_info;
CREATE TABLE user_info (
    id              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    phone           VARCHAR(20)  NOT NULL COMMENT '手机号',
    nickname        VARCHAR(64)  COMMENT '昵称',
    avatar_url      VARCHAR(512) COMMENT '头像URL',
    real_name       VARCHAR(32)  COMMENT '真实姓名',
    id_card         VARCHAR(32)  COMMENT '身份证号',
    status          TINYINT      NOT NULL DEFAULT 1 COMMENT '状态(0=禁用,1=正常)',
    last_login_time DATETIME     COMMENT '最后登录时间',
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    UNIQUE INDEX uk_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='移动端用户表';

-- ==================== 看房预约表 ====================
DROP TABLE IF EXISTS view_appointment;
CREATE TABLE view_appointment (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '预约ID',
    user_id          BIGINT       NOT NULL COMMENT '用户ID',
    room_id          BIGINT       NOT NULL COMMENT '房间ID',
    apartment_id     BIGINT       NOT NULL COMMENT '公寓ID',
    appointment_time DATETIME     NOT NULL COMMENT '预约看房时间',
    name             VARCHAR(32)  NOT NULL COMMENT '联系人姓名',
    phone            VARCHAR(20)  NOT NULL COMMENT '联系人电话',
    remark           VARCHAR(512) COMMENT '备注',
    status           VARCHAR(16)  NOT NULL DEFAULT 'pending' COMMENT '状态: pending=待确认, confirmed=已确认, cancelled=已取消, completed=已完成',
    cancel_reason    VARCHAR(512) COMMENT '取消原因',
    create_time      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted          TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    INDEX idx_user_id (user_id),
    INDEX idx_room_id (room_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='看房预约表';

-- ==================== 租约表 ====================
DROP TABLE IF EXISTS lease_agreement;
CREATE TABLE lease_agreement (
    id              BIGINT        NOT NULL AUTO_INCREMENT COMMENT '租约ID',
    user_id         BIGINT        NOT NULL COMMENT '用户ID',
    room_id         BIGINT        NOT NULL COMMENT '房间ID',
    apartment_id    BIGINT        NOT NULL COMMENT '公寓ID',
    lease_term_id   BIGINT        NOT NULL COMMENT '租期ID',
    payment_type_id BIGINT        NOT NULL COMMENT '支付方式ID',
    start_date      DATE          NOT NULL COMMENT '租赁开始日期',
    end_date        DATE          NOT NULL COMMENT '租赁结束日期',
    rent_amount     DECIMAL(10,2) NOT NULL COMMENT '月租金',
    deposit_amount  DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '押金',
    status          VARCHAR(16)   NOT NULL DEFAULT 'signed' COMMENT '状态: signed=已签订, active=执行中, expired=已到期, terminated=已解约',
    sign_time       DATETIME      COMMENT '签订时间',
    create_time     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT       NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (id),
    INDEX idx_user_id (user_id),
    INDEX idx_room_id (room_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租约表';

-- ==================== 短信验证码表 ====================
DROP TABLE IF EXISTS sms_code;
CREATE TABLE sms_code (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'ID',
    phone       VARCHAR(20)  NOT NULL COMMENT '手机号',
    code        VARCHAR(10)  NOT NULL COMMENT '验证码',
    type        VARCHAR(16)  NOT NULL DEFAULT 'login' COMMENT '类型: login/register',
    used        TINYINT      NOT NULL DEFAULT 0 COMMENT '是否已使用(0=未使用,1=已使用)',
    expire_time DATETIME     NOT NULL COMMENT '过期时间',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_phone_type (phone, type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='短信验证码表';

-- ==================== 初始化数据 ====================

-- 默认超级管理员: admin / admin123
INSERT INTO system_user (username, password, name, type, status) VALUES
('admin', 'admin123', '超级管理员', 'super', 1);
-- 注: 明文密码会在首次启动时由 InitDataRunner 自动加密为BCrypt

-- 默认支付方式
INSERT INTO payment_type (name, sort_order) VALUES
('押一付一', 1),
('押一付三', 2),
('押二付一', 3),
('半年付', 4),
('年付', 5);

-- 默认租期
INSERT INTO lease_term (month_count, unit, sort_order) VALUES
(1, 'month', 1),
(3, 'month', 2),
(6, 'month', 3),
(12, 'month', 4),
(24, 'month', 5);

-- 默认配套
INSERT INTO facility_info (name, sort_order) VALUES
('WiFi', 1),
('空调', 2),
('洗衣机', 3),
('冰箱', 4),
('热水器', 5),
('电梯', 6),
('门禁', 7),
('停车位', 8),
('健身房', 9),
('游泳池', 10);

-- 默认房间标签
INSERT INTO label_info (type, name, sort_order) VALUES
('room', '近地铁', 1),
('room', '朝南', 2),
('room', '精装修', 3),
('room', '首次出租', 4),
('room', '独立卫生间', 5),
('room', '带阳台', 6);
