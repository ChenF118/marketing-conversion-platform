import request from "@/utils/request";

// get转化微信号统计
export const getwxApi = function (pages, limit, filterform) {
  return request.post("/weixin/getwx", {
    currentpage: pages,
    singlepage: limit,
    condition: filterform,
  });
};

//微信号管理
export const cotrWxApi = function (pages, limit, filterform) {
  return request.post("/weixin/ctwx", {
    currentpage: pages,
    singlepage: limit,
    condition: filterform,
  });
};

// 添加微信号
export const appendWxApi = function (form) {
  return request.post("/weixin/append", {
    data: form,
  });
};
// 改微信号
export const updateWxApi = function (form) {
  return request.post("/weixin/udwx", {
    data: form,
  });
};

// 删除微信
export const deletewxApi = function (id) {
  return request.get("/weixin/delwx", {
    params: {
      arr: id,
    },
  });
};

// 改二维码
export const updateQRApi = function (form) {
  return request.post("/weixin/udqr", {
    data: form,
  });
};
// 改在线状态
export const updateOnlineApi = function (id, status) {
  return request.get("/weixin/cjol", {
    params: {
      id: id,
      online: status,
    },
  });
};
// 改权重
export const updatelevelApi = function (id, val) {
  return request.get("/weixin/cjlv", {
    params: {
      id: id,
      level: val,
    },
  });
};
// 删除二维码
export const deleteQRApi = function (id) {
  return request.get("/weixin/delqr", {
    params: {
      id: id,
    },
  });
};

// 导出 CSV
export const exportWeixinCsvApi = function () {
  return request.get("/export/weixin.csv", { responseType: "blob" });
};

// 分组
// 增
export const appendwxgroupApi = function (form) {
  return request.post("/wxgroup/append", {
    data: form,
  });
};

// 删
export const delgroupApi = function (id) {
  return request.get("/wxgroup/del", {
    params: {
      id: id,
    },
  });
};

// 改
export const updatewxgroupApi = function (form) {
  return request.post("/wxgroup/update", {
    forms: form,
  });
};

// 查
export const getwxgroupApi = function (pages, limit, filterform) {
  return request.get("/wxgroup/gtg", {
    params: {
      currentpage: pages,
      singlepage: limit,
      condition: filterform,
    },
  });
};

// 获取完整列表
export const getgrouplistApi = function () {
  return request.get("/wxgroup/ggl");
};
