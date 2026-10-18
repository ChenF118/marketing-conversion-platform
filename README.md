# 高并发营销转化平台（Java 全栈）

面向广告投放场景的**落地页 → 微信号 → 行为转化**一体化平台：一端管理落地页与微信资源，另一端通过 SDK 采集访问、点击、长按、复制等行为，实时形成投放看板与转化明细。

- 后端：Spring Boot 3.5 + MyBatis-Plus + MySQL 8 兼容（本机 5.5 亦可运行）
- 前端：Vue 2 + Element UI + ECharts
- 部署形态：前后端分离，REST API，JWT 无状态鉴权
- 交付状态：**60+ 接口、65 项接口回归全部通过**
- 接口清单：[Fans_java/docs/API_MAPPING.md](Fans_java/docs/API_MAPPING.md)

---

## 1. 业务价值

| 角色 | 解决的问题 |
|---|---|
| 投放运营 | 一批落地页集中管理、分组、启停、批量导入，不用逐条手工维护 |
| 客服/销售 | 每个落地页绑定不同微信号/分组，按在线状态与权重自动展示 |
| 数据分析 | 访问、点击、长按、复制、上报全链路留痕，支持按页面/微信号/日期下钻 |
| 管理者 | 多账号分权：超管看全量，运营只看自己名下的落地页与微信号 |

典型链路：

```
广告投放 → 落地页(H5) → SDK 拉取该页绑定的微信号 → 用户复制/长按 → 行为上报
                                   │
                                   └→ 实时进入后台：访问量、转化数、转化率、明细
```

---

## 2. 技术架构

| 层 | 技术 | 说明 |
|---|---|---|
| Web | Spring Boot 3.5 / Tomcat 10 | 统一响应、全局异常、CORS |
| 鉴权 | JWT（HS256）+ 白名单 + RBAC | 无状态、可水平扩展 |
| ORM | MyBatis-Plus 3.5.9 | 分页插件、逻辑列映射、批量操作 |
| 数据库 | MySQL（4 库 9 表） | 单数据源 + 库名限定，支持跨库 JOIN |
| 缓存/限流 | 本地滑动窗口 | 登录与公开埋点接口防刷，可平滑替换 Redis |
| 文档 | springdoc-openapi | `/swagger-ui.html` 在线调试 |
| 前端 | Vue 2 / Element UI / ECharts | 落地页、微信号、转化看板 |
| 构建 | Maven / npm | 一键脚本启动 |

---

## 3. 面向高并发的设计要点

1. **无状态鉴权**：JWT 不依赖 Session，服务可横向扩容；网关/负载均衡后无需粘性会话。
2. **入口限流**：登录 10 次/分钟、业务接口 600 次/分钟（按 IP 滑动窗口），防止恶意刷量与撞库。
3. **写入幂等**：埋点携带 `eventId`，唯一索引兜底，重复上报只记一次，避免计数虚高。
4. **索引设计**：落地页 `ownerId/uid/enable/url`、监测 `uid/cvsdate/wx/eventId`、微信号 `uid/online` 均建索引。
5. **连接池**：HikariCP 默认 20 连接，可配置；慢查询与连接等待可观测。
6. **分页与按需字段**：列表接口统一分页，统计接口只取必要列，避免大结果集。
7. **时间类型**：`cvsdate DATE`、`cvstime TIME`，等值/范围查询走索引，替代字符串 LIKE。
8. **数据隔离**：所有业务表带 `ownerId`，查询按归属过滤，天然支持多运营并行投放。

> 真实压测数据请以自己的部署环境为准；上述为架构层面的高并发准备，而非虚构的 QPS 数字。

---

## 4. 功能矩阵

### 落地页管理
- 新增/编辑/删除/批量删除、启停、分组、备注
- **批量导入**：一行一个 URL，预览「可导入/重复/格式错误」，确认后写入（单次 ≤500）
- **接入代码生成**：一键生成 `<script>` SDK 片段，复制到 H5 即完成接入
- **CSV 导出**

### 微信号管理
- 微信号 CRUD、二维码上传（文件存储）、在线状态、权重、分组
- 落地页 ↔ 微信分组绑定，按在线状态与权重下发
- CSV 导出

### 行为转化
- 访问（PV）、点击、长按识别、复制微信号、打开微信等 17 类行为
- 事件幂等、日期/时间规范化、落地页最近转化时间更新
- 实时明细、按落地页/微信号/日期统计、转化率、7 日趋势
- CSV 导出

### 账号与权限
- 注册/登录/改密、BCrypt 密码、JWT 48 小时
- 超管（power≥1）全量可见；运营（power=0）仅见自己创建的数据
- 初始账号 `admin / 123456`

### 平台能力
- Swagger 在线接口文档
- 图片上传（≤5MB，png/jpg/gif/webp）
- 统一响应体、全局异常、CORS、请求日志

---

## 5. 快速开始

```powershell
# 0) 首次使用：复制配置模板并填写数据库密码等（.env 已在 .gitignore 中）
Copy-Item .env.example .env

# 1) 一键启动（MySQL + 后端 + 前端 + 浏览器）
powershell -ExecutionPolicy Bypass -File .\start-all.ps1

# 2) 停止
powershell -ExecutionPolicy Bypass -File .\stop-all.ps1
```

手动方式：

```powershell
# 数据库初始化（幂等）
powershell -ExecutionPolicy Bypass -File .\db\migrate.ps1

# 后端（8000）
cd Fans_java
mvn -DskipTests package
java -Dfile.encoding=UTF-8 -jar target\fans.jar

# 前端（8080）
cd ..\Fans_Vue
npm install
npm run serve
```

- 后台：http://localhost:8080 （`admin / 123456`）
- 接口文档：http://127.0.0.1:8000/swagger-ui.html
- 演示落地页：http://127.0.0.1:8000/demo/landing.html

---

## 6. 落地页接入（两种方式）

**方式一：SDK（推荐）**

在后台「落地页管理 → 接入代码」复制片段，粘贴到 H5 页面：

```html
<script src="http://127.0.0.1:8000/sdk/landing.js"
        data-url="https://你的落地页地址"
        data-api="http://127.0.0.1:8000"></script>
```

SDK 自动完成：拉取绑定微信号 → 右下角渲染卡片 → 复制/长按行为上报（含 eventId 幂等）。

**方式二：自行调用 API**

1. `POST /cvslink/flg`，body `{ "words": "当前页面URL" }`；
2. `POST /monitor/append` 上报行为（`url` 为落地页 ID，`type` 为行为类型，`eventId` 建议必传）。

---

## 7. 数据模型

| 库 | 表 | 用途 |
|---|---|---|
| monitor_atr | edition_landing | 落地页（URL、分组、绑定微信、计数） |
| monitor_atr | edition_group | 落地页分组 |
| monitor_atr | cvsmsg_link | 统计链接（页面 ↔ 转化规则） |
| monitor_atr | cvsmsg_type | 行为类型字典 |
| monitor_atr | cvsmsg_one | 行为明细（事件、日期、来源、城市） |
| monitor_atr | user_admin | 账号（power 角色） |
| wx_atr | manage_wxnumber | 微信号（二维码、在线、权重、计数） |
| wx_atr | manage_wxgroup | 微信号分组 |
| wx_access | access_token | 微信接口凭证 |

所有业务表带 `ownerId`（数据归属）与 `createTime/updateTime`；监测表带唯一 `eventId`。

---

## 8. 安全与权限

| 项 | 措施 |
|---|---|
| 密码 | BCrypt（cost=10），不存明文 |
| 鉴权 | JWT HS256，48h 过期，签名校验 + 账号存在性校验 |
| 白名单 | 登录/注册、埋点上报、腾讯回传、落地页配置、SDK、上传文件、文档 |
| 越权 | 列表按 `ownerId` 过滤；单条操作校验归属，越权返回 403 |
| 刷量 | 登录 10/min、业务 600/min（可配置），超限 429 |
| 上传 | 类型白名单 + 大小限制 + 随机文件名 |
| 密钥 | 数据库、JWT、微信参数全部支持环境变量覆盖 |

环境变量示例：

```powershell
$env:DB_PASSWORD="your-password"
$env:JWT_SECRET="a-long-random-secret"
$env:WECHAT_APPID="wx..."
$env:WECHAT_SECRET="..."
```

---

## 9. 接口与测试

- Swagger：`/swagger-ui.html`（60+ 接口、请求体示例、在线调试）
- 回归：`tools/smoke.ps1`

```
=== passed: 65, failed: 0 ===
```

覆盖：鉴权/白名单、RBAC 越权、落地页与批量导入、微信号、行为上报与幂等、日期类型、CSV 导出、Swagger、演示链路。

单元测试：

```powershell
mvn test
```

- `JwtTest`：签发/验签/篡改拒绝
- `ReqTest`：方括号参数、数组、真值转换
- `LandingImportTest`：批量导入分类（有效/重复/非法）

---

## 10. 运维

| 场景 | 操作 |
|---|---|
| 演示数据 | `db\seed-demo.ps1`（幂等）；重置加 `-Reset` |
| 导出 | 页面按钮，或 `GET /export/landing.csv` / `weixin.csv` / `monitor.csv` |
| 日志 | 标准输出，可接 ELK |
| 配置 | `application.yml` 全部支持环境变量 |
| 端口 | `SERVER_PORT`，默认 8000（前端 8080） |

---

## 11. 后续规划

- 统计结果按天预聚合，进一步降低大时间跨度查询成本
- 限流下沉到 Redis，支持集群级配额
- 行为明细异步落库（消息队列削峰）
- 多租户（团队/公司维度）与操作审计
- 移动端适配与看板订阅推送
