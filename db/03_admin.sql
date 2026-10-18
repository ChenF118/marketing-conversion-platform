-- 初始管理员：admin / 123456（bcrypt，cost=10）
INSERT INTO monitor_atr.user_admin (username, password, header, power, createTime, updateTime)
SELECT 'admin', '$2a$10$kNDoxV1mUdXTL2dAh/Y/dufNni94GwYwAhTiqGg7nSygTxUrXiQhW', NULL, 1, NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM monitor_atr.user_admin WHERE username = 'admin');

-- 如需重置密码，取消下面注释并把哈希替换为 tools/BcryptHash.java 生成的值：
-- UPDATE monitor_atr.user_admin SET password = '<hash>', updateTime = NOW() WHERE username = 'admin';
