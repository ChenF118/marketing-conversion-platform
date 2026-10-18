import request from "@/utils/request";

// 上传图片（二维码/头像），返回 { code, message, data: { url } }
export const uploadImageApi = function (formData) {
  return request.post("/upload/image", formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });
};
