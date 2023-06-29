// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** errorHtml GET /error */
export async function errorHtmlUsingGET(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/error', {
    method: 'GET',
    ...(options || {}),
  });
}

/** errorHtml PUT /error */
export async function errorHtmlUsingPUT(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/error', {
    method: 'PUT',
    ...(options || {}),
  });
}

/** errorHtml POST /error */
export async function errorHtmlUsingPOST(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/error', {
    method: 'POST',
    ...(options || {}),
  });
}

/** errorHtml DELETE /error */
export async function errorHtmlUsingDELETE(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/error', {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** errorHtml PATCH /error */
export async function errorHtmlUsingPATCH(options?: { [key: string]: any }) {
  return request<API.ModelAndView>('/error', {
    method: 'PATCH',
    ...(options || {}),
  });
}
