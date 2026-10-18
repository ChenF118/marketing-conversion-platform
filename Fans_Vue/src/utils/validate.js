export function validatePhone(rule, value, callback) {
  if (/^1[34578]{1}\d{9}$/.test(value) == false) {
    callback(new Error("请输入正确的手机号"));
  } else {
    callback();
  }
}

export function validateUrl(rule, value, callback) {
  if (
    /^(?:http(s)?:\/\/)?[\w.-]+(?:\.[\w-]+)+[\w\-_~:/?#[\]@!$&',;=.]+$/.test(
      value
    ) == false
  ) {
    callback(new Error("请输入正确的url格式"));
  } else {
    callback();
  }
}

export function validateUsername(rule, value, callback) {
  if (/^[a-zA-Z0-9_]{3,20}$/.test(value) == false) {
    callback(new Error("用户名为3-20位字母，数字，下划线"));
  } else {
    callback();
  }
}

export function validatePassword(rule, value, callback) {
  if (/^[a-zA-Z0-9.]{4,16}$/.test(value) == false) {
    callback(new Error("密码为4-16位字母，数字 . 组成"));
  } else {
    callback();
  }
}
