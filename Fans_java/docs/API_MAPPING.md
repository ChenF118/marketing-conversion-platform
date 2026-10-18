# API 接口清单

- 基址：`http://127.0.0.1:8000`
- 鉴权：除白名单外，均需请求头 `Authorization: <token>`（兼容 `Bearer <token>`）
- 请求体：`application/json`，参数按业务包在 `data` / `forms` / `form` / `condition` 中
- 响应：`{code,message,data}` 或 `{code,msg,data,count}`
- 在线文档：`/swagger-ui.html`

## 白名单（无需 Token）

`/admin/login`、`/admin/rgs`、`/monitor/append`、`/monitor/inctotal`、`/tencent/**`、`/access/**`、`/cvslink/flg`、`/demo/**`、`/sdk/**`、`/uploads/**`、`/swagger-ui/**`、`/v3/api-docs/**`、`/wx/scheme`

## 账号 admin

| 方法 | 路径 | 鉴权 | 入参 | 说明 |
|---|---|---|---|---|
| POST | /admin/rgs | 公开 | `form{username,password,imageUrl,power}` | 注册，默认运营角色 |
| POST | /admin/login | 公开 | `form{username,password}` | 返回 `{code,msg,Token,IDcode}` |
| GET | /admin/profile | 需登录 | `id` | 个人资料（不含密码），仅本人或超管 |
| POST | /admin/reset | 需登录 | `form{id,username,password,imageUrl,power}` | 改密/资料，仅本人或超管 |

## 落地页 landing（16）

| 方法 | 路径 | 入参 | 说明 |
|---|---|---|---|
| POST | /landing/append | `data{landingurl,group,remarks,delivery,track,open}` | 新增，URL 查重 |
| GET | /landing/del | `id` | 删除 |
| GET | /landing/dellot | `arr[]` | 批量删除 |
| POST | /landing/update | `data{id,url,group,remarks,delivery,track,enable}` | 编辑 |
| POST | /landing/gtlanding | `currentpage,singlepage,condition{group,enable,words}` | 分页列表（带 Group） |
| GET | /landing/gtl | — | 下拉用 `[{id,url}]` |
| POST | /landing/tgl | `id,enable` | 启停 |
| POST | /landing/lag | — | 全量列表（裸数组，带 cvslink） |
| POST | /landing/lwg | `currentpage,singlepage,condition{group,words}` | 列表（带 wxgroup） |
| GET | /landing/ubd | `id` | 解绑微信 |
| POST | /landing/cgwx | `data{id,erweima,from,group,same,weixin,mclick,mtouch,pclick,pselect,show1,show2,share}` | 绑定微信 |
| GET | /landing/ubl | `arr[]` | 批量解绑 |
| POST | /landing/cgl | `arr[],form{...}` | 批量绑定 |
| POST | /landing/lar | `condition{group,url,date1,date2,order}` | 排行，`order` 白名单 |
| POST | /landing/gtr | `condition{group,url}` | 转化率（含 rate） |
| POST | /landing/import | `data{dryRun,landingurls,group,remarks,delivery,track,open}` | 批量导入：`dryRun=true` 预览，`false` 写入 |

## 分组 group（6）

| 方法 | 路径 | 入参 | 说明 |
|---|---|---|---|
| POST | /group/append | `forms{group,remarks}` | 新增，模糊查重 |
| GET | /group/del | `id` | 删除 |
| POST | /group/update | `forms{id,group,remarks}` | 编辑 |
| GET | /group/gtgroup | `currentpage,singlepage` | 分页（含 landing_count） |
| GET | /group/gta | — | 全量（无 remarks/total） |
| GET | /group/gts | `words` | 搜索（固定第 1 页） |

## 微信号 weixin（9）

| 方法 | 路径 | 入参 | 说明 |
|---|---|---|---|
| POST | /weixin/getwx | `currentpage,singlepage,condition{}` | 微信号转化统计 |
| POST | /weixin/ctwx | `condition{group1,group2,words,group3}` | 在线管理列表（带 Wxgroup） |
| POST | /weixin/udwx | `data{...}` | 编辑 |
| GET | /weixin/delwx | `arr[]` | 批量删除 |
| POST | /weixin/append | `data{wxh,wxname,erweima,sex,wxgroup,online,auto,level,remarks}` | 新增 |
| POST | /weixin/udqr | `data{id,sendimg}` | 更新二维码（URL） |
| GET | /weixin/delqr | `id` | 清空二维码 |
| GET | /weixin/cjol | `id,online` | 切换在线 |
| GET | /weixin/cjlv | `id,level` | 调整权重 |

## 微信分组 wxgroup（5）

| 方法 | 路径 | 入参 | 说明 |
|---|---|---|---|
| POST | /wxgroup/append | `data{group,remark}` | 新增 |
| GET | /wxgroup/del | `id` | 删除 |
| POST | /wxgroup/update | `forms{id,group,remark}` | 编辑 |
| GET | /wxgroup/gtg | `currentpage,singlepage,condition` | 分页（含 weixin_count） |
| GET | /wxgroup/ggl | — | 全量（无 remarks） |

## 统计链接 cvslink（7）

| 方法 | 路径 | 入参 | 说明 |
|---|---|---|---|
| POST | /cvslink/append | `data{uid,type,cvstype,cvscount,cvsmode,code}` | 新增 |
| GET | /cvslink/del | `id` 或 `arr[]` | 删除 |
| POST | /cvslink/update | `data{id,cvstype,cvscount,cvsmode,type,code}` | 编辑 |
| POST | /cvslink/min | `data{id,second}` | 转化时长 |
| POST | /cvslink/gbl | `currentpage,singlepage,condition{group,words}` | 列表（带 landing） |
| POST | /cvslink/gtr | `condition{group,url,words,date1,date2,cvstype}` | 转化率（带 Landing） |
| POST | /cvslink/flg | `words`（当前页 URL） | **公开**：落地页配置查询 |

## 行为类型 cvstype（5）

| 方法 | 路径 | 入参 | 说明 |
|---|---|---|---|
| GET | /cvstype/gtl | — | 全量 |
| GET | /cvstype/upn | `id,typename` | 改名 |
| GET | /cvstype/del | `id` | 删除 |
| POST | /cvstype/append | `data{type,int}` | 新增 |
| GET | /cvstype/tpl | — | `[{typename,typeint}]` |

## 监测 monitor（4）

| 方法 | 路径 | 入参 | 说明 |
|---|---|---|---|
| POST | /monitor/append | `url,eventId,type,cvsdate,cvstime,wx,targetelement,city,...` | **公开**：行为上报（幂等） |
| POST | /monitor/inctotal | `url,wx` | **公开**：访问计数 |
| POST | /monitor/gtmonitor | `currentpage,singlepage,condition{url,date1,date2,state,words,group}` | 明细 |
| GET | /monitor/mthome | — | 近 7 日趋势 |

## 腾讯回传 tencent（2）

| 方法 | 路径 | 说明 |
|---|---|---|
| POST | /tencent/tfeed | GET 转发腾讯转化接口，原样返回 |
| POST | /tencent/tfeeds | POST 表单回传，返回 `{code,reason,body}` |

## 平台能力

| 方法 | 路径 | 鉴权 | 说明 |
|---|---|---|---|
| POST | /upload/image | 需登录 | 图片上传（≤5MB），返回 `{url}` |
| GET | /export/landing.csv | 需登录 | 落地页导出（按归属） |
| GET | /export/weixin.csv | 需登录 | 微信号导出 |
| GET | /export/monitor.csv | 需登录 | 行为明细导出（最多 5 万行） |
| GET | /swagger-ui.html | 公开 | 在线接口文档 |
| GET | /demo/landing.html | 公开 | 演示落地页（支持 `?url=`） |
| GET | /sdk/landing.js | 公开 | 落地页接入 SDK |
| GET | /wx/scheme | 公开 | 小程序 scheme |
