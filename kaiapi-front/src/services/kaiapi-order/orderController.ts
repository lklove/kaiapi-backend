// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** cancelOrderSn POST /order/cancelOrderSn */
export async function cancelOrderSnUsingPOST(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.cancelOrderSnUsingPOSTParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponse>('/order/cancelOrderSn', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** generateOrder POST /order/generateOrder */
export async function generateOrderUsingPOST(
  body: API.GenerateOrderRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseOrderVo_>('/order/generateOrder', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** generateToken GET /order/generateToken */
export async function generateTokenUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.generateTokenUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponse>('/order/generateToken', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** getOrderInfoByPage GET /order/getOrderInfo */
export async function getOrderInfoByPageUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getOrderInfoByPageUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageOrderDetailVo_>('/order/getOrderInfo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** getOrderSuccessCount GET /order/getOrderSuccess */
export async function getOrderSuccessCountUsingGET(options?: { [key: string]: any }) {
  return request<API.BaseResponseLong_>('/order/getOrderSuccess', {
    method: 'GET',
    ...(options || {}),
  });
}
