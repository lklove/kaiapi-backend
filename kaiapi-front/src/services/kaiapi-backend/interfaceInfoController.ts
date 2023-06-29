// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addInterfaceInfo POST /api/interfaceInfo/add */
export async function addInterfaceInfoUsingPOST(
  body: API.InterfaceInfoAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponselong>('/api/interfaceInfo/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getAvailableInterface GET /api/interfaceInfo/count */
export async function getAvailableInterfaceUsingGET(options?: { [key: string]: any }) {
  return request<API.BaseResponselong>('/api/interfaceInfo/count', {
    method: 'GET',
    ...(options || {}),
  });
}

/** deleteInterfaceInfo POST /api/interfaceInfo/delete */
export async function deleteInterfaceInfoUsingPOST(
  body: API.BranchDeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseboolean>('/api/interfaceInfo/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** feignGetInterfaceInfoById GET /api/interfaceInfo/feign/get */
export async function feignGetInterfaceInfoByIdUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.feignGetInterfaceInfoByIdUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseInterfaceInfoVo>('/api/interfaceInfo/feign/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** getInterfaceInfoById GET /api/interfaceInfo/get */
export async function getInterfaceInfoByIdUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getInterfaceInfoByIdUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseInterfaceDetailVo>('/api/interfaceInfo/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** invoke POST /api/interfaceInfo/invoke */
export async function invokeUsingPOST(
  body: API.InterfaceInvokeRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponse>('/api/interfaceInfo/invoke', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listInterfaceInfo GET /api/interfaceInfo/list */
export async function listInterfaceInfoUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listInterfaceInfoUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseListInterfaceInfo>('/api/interfaceInfo/list', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listInterfaceInfoAllByPage GET /api/interfaceInfo/list/AllPage */
export async function listInterfaceInfoAllByPageUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listInterfaceInfoAllByPageUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageManageInterfaceVo>('/api/interfaceInfo/list/AllPage', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listInterfaceInfoByPage GET /api/interfaceInfo/list/page */
export async function listInterfaceInfoByPageUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listInterfaceInfoByPageUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageInterfaceInfoVo>('/api/interfaceInfo/list/page', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** offline POST /api/interfaceInfo/offline */
export async function offlineUsingPOST(body: API.IdRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseboolean>('/api/interfaceInfo/offline', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** publish POST /api/interfaceInfo/online */
export async function publishUsingPOST(body: API.IdRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseboolean>('/api/interfaceInfo/online', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateInterfaceInfo POST /api/interfaceInfo/update */
export async function updateInterfaceInfoUsingPOST(
  body: API.InterfaceInfoUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseboolean>('/api/interfaceInfo/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
