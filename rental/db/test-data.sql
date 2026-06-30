-- ============================================
-- Rental 租房平台 - 测试数据
-- 北京地区: 2个公寓 + 6个房间
-- ============================================

USE rental_db;

-- ==================== 公寓 ====================
INSERT INTO apartment_info (id, name, intro, district_id, detail_address, latitude, longitude, cover_url, images, facility_ids, phone, is_release, sort_order) VALUES
(1, '拾光公寓·朝阳旗舰店',
 '拾光公寓朝阳旗舰店位于朝阳区核心地段，交通便利，周边配套成熟。全楼配备智能门禁、24小时安保、高速WiFi，是都市白领的理想之选。',
 110103,
 '北京市朝阳区建国路88号',
 39.9087, 116.4717,
 'https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=800',
 '["https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=800","https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=800","https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=800"]',
 '[1,2,3,4,5,6,7]',
 '010-88886666',
 1, 1),

(2, '拾光公寓·海淀科技园店',
 '坐落于海淀区中关村科技园旁，紧邻地铁站，周边高校云集。专为IT从业者和高校师生打造的高品质公寓，配备健身房和共享办公空间。',
 110104,
 '北京市海淀区中关村大街1号',
 39.9836, 116.3153,
 'https://images.unsplash.com/photo-1493809842364-78817add7ffb?w=800',
 '["https://images.unsplash.com/photo-1493809842364-78817add7ffb?w=800","https://images.unsplash.com/photo-1536376072261-38c75010e6c9?w=800"]',
 '[1,2,3,4,5,6,7,8,9]',
 '010-88889999',
 1, 2);

-- ==================== 房间 - 朝阳旗舰店 ====================
INSERT INTO room_info (id, apartment_id, name, intro, price, area, floor, room_type, cover_url, images, attr_value_ids, is_release, sort_order) VALUES
(1, 1, '阳光大床房',
 '朝南主卧，阳光充沛，独立卫生间，品牌家具家电齐全，拎包入住。',
 3500.00, 28.5, '18层/共28层', '1室1卫',
 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=600',
 '["https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=600","https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=600"]',
 null, 1, 1),

(2, 1, '精致小户型',
 '紧凑实用小户型，适合单人居住，收纳空间充足。',
 2200.00, 18.0, '12层/共28层', '1室0厅1卫',
 'https://images.unsplash.com/photo-1536376072261-38c75010e6c9?w=600',
 '["https://images.unsplash.com/photo-1536376072261-38c75010e6c9?w=600"]',
 null, 1, 2),

(3, 1, '豪华套房',
 '一室一厅豪华套房，独立客厅+卧室，全景落地窗，城市景观尽收眼底。',
 5800.00, 45.0, '25层/共28层', '1室1厅1卫',
 'https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=600',
 '["https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=600","https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=600"]',
 null, 1, 3);

-- ==================== 房间 - 海淀科技园店 ====================
INSERT INTO room_info (id, apartment_id, name, intro, price, area, floor, room_type, cover_url, images, attr_value_ids, is_release, sort_order) VALUES
(4, 2, '科技园单人房',
 '专为IT人士设计，配备人体工学书桌和高速网络，工作生活两不误。',
 2800.00, 22.0, '8层/共20层', '1室1卫',
 'https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=600',
 '["https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=600"]',
 null, 1, 1),

(5, 2, '舒适双人房',
 '宽敞双人房，适合情侣或好友合租，独立阳台，视野开阔。',
 4200.00, 35.0, '15层/共20层', '1室1厅1卫',
 'https://images.unsplash.com/photo-1493809842364-78817add7ffb?w=600',
 '["https://images.unsplash.com/photo-1493809842364-78817add7ffb?w=600","https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=600"]',
 null, 1, 2),

(6, 2, '校园风经济房',
 '性价比之选，简洁装修干净整洁，靠近多所高校，适合学生和实习生。',
 1800.00, 15.0, '3层/共20层', '1室0厅1卫',
 'https://images.unsplash.com/photo-1536376072261-38c75010e6c9?w=600',
 '["https://images.unsplash.com/photo-1536376072261-38c75010e6c9?w=600"]',
 null, 1, 3);

-- ==================== 房间关联: 支付方式 ====================
-- 房间1: 押一付一, 押一付三
INSERT INTO room_payment (room_id, payment_type_id) VALUES
(1,1),(1,2),
(2,1),(2,2),(2,4),
(3,1),(3,2),(3,4),(3,5),
(4,1),(4,2),
(5,1),(5,2),(5,3),
(6,1),(6,4);

-- ==================== 房间关联: 租期 ====================
INSERT INTO room_lease_term (room_id, lease_term_id) VALUES
(1,1),(1,2),(1,3),(1,4),
(2,1),(2,2),(2,3),
(3,1),(3,2),(3,3),(3,4),(3,5),
(4,1),(4,2),(4,3),
(5,1),(5,2),(5,3),(5,4),
(6,1),(6,2),(6,3);

-- ==================== 房间关联: 标签 ====================
INSERT INTO room_label (room_id, label_id) VALUES
(1,1),(1,2),(1,3),        -- 近地铁,朝南,精装修
(2,3),                      -- 精装修
(3,1),(3,2),(3,3),(3,5),(3,6), -- 近地铁,朝南,精装修,独立卫生间,带阳台
(4,3),(4,6),                -- 精装修,带阳台
(5,1),(5,2),(5,5),(5,6),   -- 近地铁,朝南,独立卫生间,带阳台
(6,3),(6,4);                -- 精装修,首次出租

-- ==================== 房间关联: 配套 ====================
INSERT INTO room_facility (room_id, facility_id) VALUES
(1,1),(1,2),(1,3),(1,5),(1,7),
(2,1),(2,2),(2,5),
(3,1),(3,2),(3,3),(3,4),(3,5),(3,7),(3,8),
(4,1),(4,2),(4,5),(4,7),
(5,1),(5,2),(5,3),(5,4),(5,5),(5,7),(5,8),
(6,1),(2,2),(6,5);

-- ==================== 验证 ====================
SELECT '公寓' AS 类型, COUNT(*) AS 数量 FROM apartment_info WHERE deleted=0
UNION ALL
SELECT '房间', COUNT(*) FROM room_info WHERE deleted=0
UNION ALL
SELECT '房间支付方式', COUNT(*) FROM room_payment WHERE deleted=0
UNION ALL
SELECT '房间租期', COUNT(*) FROM room_lease_term WHERE deleted=0
UNION ALL
SELECT '房间标签', COUNT(*) FROM room_label WHERE deleted=0
UNION ALL
SELECT '房间配套', COUNT(*) FROM room_facility WHERE deleted=0;
