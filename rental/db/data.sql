-- ============================================
-- Rental 租房平台 - 地区初始数据
-- 覆盖: 北京/上海/广州/深圳/杭州/成都
-- ============================================

USE rental_db;

-- 清空旧数据
TRUNCATE TABLE district_info;

-- ==================== 省份 ====================
INSERT INTO district_info (id, parent_id, name, level, sort_order) VALUES
(110000, 0, '北京市',   'province', 1),
(310000, 0, '上海市',   'province', 2),
(440000, 0, '广东省',   'province', 3),
(330000, 0, '浙江省',   'province', 4),
(510000, 0, '四川省',   'province', 5);

-- ==================== 城市 ====================
INSERT INTO district_info (id, parent_id, name, level, sort_order) VALUES
-- 北京
(110100, 110000, '北京市', 'city', 1),
-- 上海
(310100, 310000, '上海市', 'city', 1),
-- 广东
(440100, 440000, '广州市', 'city', 1),
(440300, 440000, '深圳市', 'city', 2),
-- 浙江
(330100, 330000, '杭州市', 'city', 1),
-- 四川
(510100, 510000, '成都市', 'city', 1);

-- ==================== 区县 ====================

-- 北京市 朝阳/海淀/丰台/东城/西城/通州/大兴/昌平
INSERT INTO district_info (id, parent_id, name, level, sort_order) VALUES
(110101, 110100, '东城区',   'district', 1),
(110102, 110100, '西城区',   'district', 2),
(110103, 110100, '朝阳区',   'district', 3),
(110104, 110100, '海淀区',   'district', 4),
(110105, 110100, '丰台区',   'district', 5),
(110106, 110100, '通州区',   'district', 6),
(110107, 110100, '大兴区',   'district', 7),
(110108, 110100, '昌平区',   'district', 8);

-- 上海市 浦东/黄浦/徐汇/静安/长宁/普陀/虹口/杨浦/闵行/松江
INSERT INTO district_info (id, parent_id, name, level, sort_order) VALUES
(310101, 310100, '浦东新区', 'district', 1),
(310102, 310100, '黄浦区',   'district', 2),
(310103, 310100, '徐汇区',   'district', 3),
(310104, 310100, '静安区',   'district', 4),
(310105, 310100, '长宁区',   'district', 5),
(310106, 310100, '普陀区',   'district', 6),
(310107, 310100, '虹口区',   'district', 7),
(310108, 310100, '杨浦区',   'district', 8),
(310109, 310100, '闵行区',   'district', 9),
(310110, 310100, '松江区',   'district', 10);

-- 广州市 天河/越秀/海珠/白云/番禺/黄埔/荔湾/花都
INSERT INTO district_info (id, parent_id, name, level, sort_order) VALUES
(440101, 440100, '天河区',   'district', 1),
(440102, 440100, '越秀区',   'district', 2),
(440103, 440100, '海珠区',   'district', 3),
(440104, 440100, '白云区',   'district', 4),
(440105, 440100, '番禺区',   'district', 5),
(440106, 440100, '黄埔区',   'district', 6),
(440107, 440100, '荔湾区',   'district', 7),
(440108, 440100, '花都区',   'district', 8);

-- 深圳市 南山/福田/罗湖/宝安/龙岗/龙华/光明/盐田
INSERT INTO district_info (id, parent_id, name, level, sort_order) VALUES
(440301, 440300, '南山区',   'district', 1),
(440302, 440300, '福田区',   'district', 2),
(440303, 440300, '罗湖区',   'district', 3),
(440304, 440300, '宝安区',   'district', 4),
(440305, 440300, '龙岗区',   'district', 5),
(440306, 440300, '龙华区',   'district', 6),
(440307, 440300, '光明区',   'district', 7),
(440308, 440300, '盐田区',   'district', 8);

-- 杭州市 西湖/滨江/余杭/拱墅/上城/萧山/钱塘
INSERT INTO district_info (id, parent_id, name, level, sort_order) VALUES
(330101, 330100, '西湖区',   'district', 1),
(330102, 330100, '滨江区',   'district', 2),
(330103, 330100, '余杭区',   'district', 3),
(330104, 330100, '拱墅区',   'district', 4),
(330105, 330100, '上城区',   'district', 5),
(330106, 330100, '萧山区',   'district', 6),
(330107, 330100, '钱塘区',   'district', 7);

-- 成都市 武侯/锦江/高新/青羊/成华/金牛/天府新区/双流
INSERT INTO district_info (id, parent_id, name, level, sort_order) VALUES
(510101, 510100, '武侯区',     'district', 1),
(510102, 510100, '锦江区',     'district', 2),
(510103, 510100, '高新区',     'district', 3),
(510104, 510100, '青羊区',     'district', 4),
(510105, 510100, '成华区',     'district', 5),
(510106, 510100, '金牛区',     'district', 6),
(510107, 510100, '天府新区',   'district', 7),
(510108, 510100, '双流区',     'district', 8);

-- ==================== 验证 ====================
SELECT level, COUNT(*) AS cnt FROM district_info GROUP BY level;
-- 预期: province=5, city=6, district=48
