// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 绑定用户手机号 POST /api/user/bindPhone */
export async function bindPhoneUsingPOST(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.bindPhoneUsingPOSTParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponse>('/api/user/bindPhone', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** deleteUser POST /api/user/delete */
export async function deleteUserUsingPOST(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseboolean>('/api/user/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getUserById GET /api/user/get */
export async function getUserByIdUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserByIdUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseUserVO>('/api/user/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** getLoginUser GET /api/user/get/login */
export async function getLoginUserUsingGET(options?: { [key: string]: any }) {
  return request<API.BaseResponseUserVO>('/api/user/get/login', {
    method: 'GET',
    ...(options || {}),
  });
}

/** getAllUserCount GET /api/user/getAllUserCount */
export async function getAllUserCountUsingGET(options?: { [key: string]: any }) {
  return request<API.BaseResponselong>('/api/user/getAllUserCount', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 生成图形验证码 GET /api/user/getCaptcha */
export async function getCaptchaUsingGET(options?: { [key: string]: any }) {
  return request<any>('/api/user/getCaptcha', {
    method: 'GET',
    ...(options || {}),
  });
}

/** getGithubStart GET /api/user/getGithubStart */
export async function getGithubStartUsingGET(options?: { [key: string]: any }) {
  return request<API.BaseResponsestring>('/api/user/getGithubStart', {
    method: 'GET',
    ...(options || {}),
  });
}

/** listUser GET /api/user/list */
export async function listUserUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listUserUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseListUserVO>('/api/user/list', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listUserByPage GET /api/user/list/page */
export async function listUserByPageUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listUserByPageUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageUserVO>('/api/user/list/page', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** userLogin POST /api/user/login */
export async function userLoginUsingPOST(
  body: API.UserLoginRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLoginUserVo>('/api/user/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 用户通过手机号进行登录 POST /api/user/loginBySms */
export async function loginBySmsUsingPOST(
  body: API.UserLoginBySmsRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLoginUserVo>('/api/user/loginBySms', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** userLogout POST /api/user/logout */
export async function userLogoutUsingPOST(options?: { [key: string]: any }) {
  return request<API.BaseResponseboolean>('/api/user/logout', {
    method: 'POST',
    ...(options || {}),
  });
}

/** userRegister POST /api/user/register */
export async function userRegisterUsingPOST(
  body: API.UserRegisterRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponselong>('/api/user/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 发送短信验证码 GET /api/user/sendSms */
export async function sendSmsUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.sendSmsUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseboolean>('/api/user/sendSms', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** updateUser POST /api/user/update */
export async function updateUserUsingPOST(
  body: API.UserUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseboolean>('/api/user/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
