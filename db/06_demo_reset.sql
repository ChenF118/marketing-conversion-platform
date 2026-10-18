-- 重置演示数据（配合 seed-demo.ps1 -Reset 使用）
DELETE FROM monitor_atr.cvsmsg_one
WHERE uid IN (SELECT id FROM monitor_atr.edition_landing WHERE url = 'http://127.0.0.1:8000/demo/landing.html');

DELETE FROM monitor_atr.cvsmsg_link
WHERE uid IN (SELECT id FROM monitor_atr.edition_landing WHERE url = 'http://127.0.0.1:8000/demo/landing.html');

DELETE FROM monitor_atr.edition_landing
WHERE url = 'http://127.0.0.1:8000/demo/landing.html';

DELETE FROM wx_atr.manage_wxnumber WHERE wxh = 'wx_demo_live';
DELETE FROM wx_atr.manage_wxgroup WHERE name = '演示微信组';
DELETE FROM monitor_atr.edition_group WHERE groupName = '演示分组';
