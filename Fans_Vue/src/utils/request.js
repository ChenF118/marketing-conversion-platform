import axios from "axios";

const request = axios.create({
  // baseURL: "https://api.antairui.com",
  baseURL: "http://127.0.0.1:8000",
});

// 添加请求拦截器
request.interceptors.request.use(
  (config) => {
    const Token = sessionStorage.getItem("Token");
    if (Token) {
      config.headers.Authorization = Token;
    }

    return config;
  },
  function (error) {
    return Promise.reject(error);
  }
);

request.interceptors.response.use(
  function (response) {
    return response;
  },
  function (error) {
    return Promise.reject(error);
  }
);

export default request;
