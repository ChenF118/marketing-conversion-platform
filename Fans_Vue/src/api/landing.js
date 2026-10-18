import request from "@/utils/request";

// add落地页
export const insertApi = function (newdata) {
  return request.post("/landing/append", {
    data: newdata,
  });
};

// 批量导入（dryRun=true 预览，false 正式导入）
export const importLandingApi = function (form, dryRun) {
  return request.post("/landing/import", {
    data: Object.assign({}, form, { dryRun: dryRun }),
  });
};

// 导出 CSV
export const exportLandingCsvApi = function () {
  return request.get("/export/landing.csv", { responseType: "blob" });
};

// 删
export const delsingleApi = function (val) {
  return request.get("/landing/del", {
    params: {
      id: val,
    },
  });
};

// 批量删
export const dellotApi = function (val) {
  return request.get("/landing/dellot", {
    params: {
      arr: val,
    },
  });
};

// 改
export const updatelandingApi = function (form) {
  return request.post("/landing/update", {
    data: form,
  });
};

//改微信绑定信息
export const changeweixinApi = function (form) {
  return request.post("/landing/cgwx", {
    data: form,
  });
};

//批量改微信绑定信息
export const changelotApi = function (arrs, forms) {
  return request.post("/landing/cgl", {
    arr: arrs,
    form: forms,
  });
};

// get落地页
export const getLandingApi = function (pages, limit, filterform) {
  return request.post("/landing/gtlanding", {
    currentpage: pages,
    singlepage: limit,
    condition: filterform,
  });
};

// get落地页排行页数据
export const LandingRankApi = function (pages, limit, filterform) {
  return request.post("/landing/lar", {
    currentpage: pages,
    singlepage: limit,
    condition: filterform,
  });
};

// 获取cvs转化率
export const getcvsrateApi = function (pages, limit, filterform) {
  return request.post("/landing/gtr", {
    currentpage: pages,
    singlepage: limit,
    condition: filterform,
  });
};

// 单独url列表用于渲染菜单
export const landingallApi = function () {
  return request.post("/landing/gtl");
};

// 切换状态
export const tglandingApi = function (ids, ables) {
  return request.post("/landing/tgl", {
    id: ids,
    enable: ables,
  });
};

// 获取带微信分组附表
export const LandingWithGroupApi = function (pages, limit, filterform) {
  return request.post("/landing/lwg", {
    currentpage: pages,
    singlepage: limit,
    condition: filterform,
  });
};

//解绑微信
export const UnbindingApi = function (ids) {
  return request.get("/landing/ubd", {
    params: {
      id: ids,
    },
  });
};

//批量解绑
export const UnbindingLotApi = function (ids) {
  return request.get("/landing/ubl", {
    params: {
      arr: ids,
    },
  });
};

// 分组
// 增
export const addgroupApi = function (form) {
  return request.post("/group/append", {
    forms: form,
  });
};

// 查
export const getgroupApi = function (pages, limit) {
  return request.get("/group/gtgroup", {
    params: {
      currentpage: pages,
      singlepage: limit,
    },
  });
};

export const groupallApi = function () {
  return request.get("/group/gta");
};

export const groupsearchApi = function (val, pages, limit) {
  return request.get("/group/gts", {
    params: {
      words: val,
      currentpage: pages,
      singlepage: limit,
    },
  });
};

// 改
export const updategroupApi = function (form) {
  return request.post("/group/update", {
    forms: form,
  });
};

// 删
export const groupdelApi = function (id) {
  return request.get("/group/del", {
    params: {
      id: id,
    },
  });
};
