-- 种子数据（依赖 migrate.ps1 完成列名迁移后执行）
INSERT INTO wx_access.access_token (id, access)
SELECT 1, '' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM wx_access.access_token WHERE id = 1);

INSERT INTO monitor_atr.cvsmsg_type (typename, typeint, createTime)
SELECT * FROM (
  SELECT '点击' AS t, 1 AS i, NOW() AS c
  UNION ALL SELECT '长按识别', 2, NOW()
  UNION ALL SELECT '微信复制按钮点击', 3, NOW()
  UNION ALL SELECT '微信打开按钮点击', 4, NOW()
  UNION ALL SELECT '咨询按钮点击', 5, NOW()
  UNION ALL SELECT '下载按钮点击', 6, NOW()
  UNION ALL SELECT '购买按钮点击', 7, NOW()
  UNION ALL SELECT '注册按钮点击', 8, NOW()
  UNION ALL SELECT '登录按钮点击', 9, NOW()
  UNION ALL SELECT '拨打电话按钮点击', 10, NOW()
  UNION ALL SELECT '短信按钮点击', 11, NOW()
  UNION ALL SELECT '加入购物车按钮点击', 12, NOW()
  UNION ALL SELECT '地图按钮点击', 13, NOW()
  UNION ALL SELECT 'QQ咨询按钮点击', 14, NOW()
  UNION ALL SELECT '抽奖按钮点击', 15, NOW()
  UNION ALL SELECT '投票按钮点击', 16, NOW()
  UNION ALL SELECT '表单确认提交按钮点击', 17, NOW()
) x
WHERE NOT EXISTS (SELECT 1 FROM monitor_atr.cvsmsg_type);

INSERT INTO monitor_atr.edition_group (groupName, remarks, total, createTime, updateTime)
SELECT '默认分组', '系统默认分组', 0, NOW(), NOW() FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM monitor_atr.edition_group);

UPDATE monitor_atr.edition_group
SET groupName = '默认分组', remarks = '系统默认分组'
WHERE id = 1 AND (groupName IS NULL OR groupName = '');

INSERT INTO monitor_atr.cvsmsg_type (typename, typeint, createTime)
SELECT '点击', 1, NOW() FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM monitor_atr.cvsmsg_type WHERE typeint = 1);
