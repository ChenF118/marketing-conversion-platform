-- 演示数据：一条完整的「落地页 -> 微信分组 -> 微信号 -> 统计链接」链路
-- 幂等，可重复执行；不会影响已有数据（按名称/URL/wxh 判重）
-- 执行后打开：http://127.0.0.1:8000/demo/landing.html

INSERT INTO monitor_atr.edition_group (groupName, remarks, total, createTime, updateTime)
SELECT '演示分组', '系统演示数据', 0, NOW(), NOW() FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM monitor_atr.edition_group WHERE groupName = '演示分组');

INSERT INTO wx_atr.manage_wxgroup (name, remarks, createTime, updateTime)
SELECT '演示微信组', '系统演示数据', NOW(), NOW() FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM wx_atr.manage_wxgroup WHERE name = '演示微信组');

INSERT INTO wx_atr.manage_wxnumber
  (uid, wxh, wxname, erweima, sex, online, control, level, remarks, visits, copyCount, report, createTime, updateTime)
SELECT g.id, 'wx_demo_live', '演示客服-小好',
       'https://api.qrserver.com/v1/create-qr-code/?size=240x240&data=wx_demo_live',
       '1', 1, 1, 5, '系统演示数据', 0, 0, 0, NOW(), NOW()
FROM wx_atr.manage_wxgroup g
WHERE g.name = '演示微信组'
  AND NOT EXISTS (SELECT 1 FROM wx_atr.manage_wxnumber w WHERE w.wxh = 'wx_demo_live');

INSERT INTO monitor_atr.edition_landing
  (uid, gid, url, remarks, delivery, track, enable, erweima, weixin, visible, share, total, cvscount, fans, createTime, updateTime)
SELECT g.id, wg.id, 'http://127.0.0.1:8000/demo/landing.html', '系统演示落地页', 0, 0, 1,
       'https://api.qrserver.com/v1/create-qr-code/?size=240x240&data=wx_demo_live',
       'wx_demo_live', 1, 0, 0, 0, 0, NOW(), NOW()
FROM monitor_atr.edition_group g
JOIN wx_atr.manage_wxgroup wg ON wg.name = '演示微信组'
WHERE g.groupName = '演示分组'
  AND NOT EXISTS (
      SELECT 1 FROM monitor_atr.edition_landing l
      WHERE l.url = 'http://127.0.0.1:8000/demo/landing.html'
  );

INSERT INTO monitor_atr.cvsmsg_link (uid, type, cvstype, cvscount, cvsmode, code, second, createTime, updateTime)
SELECT l.id, '1', 1, 0, 0, 'type_index="1"', 0, NOW(), NOW()
FROM monitor_atr.edition_landing l
WHERE l.url = 'http://127.0.0.1:8000/demo/landing.html'
  AND NOT EXISTS (SELECT 1 FROM monitor_atr.cvsmsg_link c WHERE c.uid = l.id);
