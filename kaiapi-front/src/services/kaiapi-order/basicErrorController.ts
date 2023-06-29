// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** error GET /order/error */
export async function errorUsingGET(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/order/error', {
    method: 'GET',
    ...(options || {}),
  });
}

/** error PUT /order/error */
export async function errorUsingPUT(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/order/error', {
    method: 'PUT',
    ...(options || {}),
  });
}

/** error POST /order/error */
export async function errorUsingPOST(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/order/error', {
    method: 'POST',
    ...(options || {}),
  });
}

/** error DELETE /order/error */
export async function errorUsingDELETE(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/order/error', {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** error PATCH /order/error */
export async function errorUsingPATCH(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/order/error', {
    method: 'PATCH',
    ...(options || {}),
  });
}
