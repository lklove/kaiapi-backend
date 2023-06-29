// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** paySuccessNotify POST /alipay/notify */
export async function paySuccessNotifyUsingPOST(options?: { [key: string]: any }) {
  return request<any>('/alipay/notify', {
    method: 'POST',
    ...(options || {}),
  });
}

/** aliPay POST /alipay/pay */
export async function aliPayUsingPOST(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.aliPayUsingPOSTParams,
  options?: { [key: string]: any },
) {
  return request<any>('/alipay/pay', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** queryTradeStatus GET /alipay/queryTradeStatus */
export async function queryTradeStatusUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: { orderSn: string | undefined },
  options?: { [p: string]: any },
) {
  return request<API.BaseResponseAlipayInfo_>('/alipay/queryTradeStatus', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
