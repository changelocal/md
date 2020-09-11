import axios from '@/libs/api.request'

export const query = (data) => {
  return axios.request({
    url: '/web/order/query',
    data,
    method: 'post'
  })
}
export const update = (data) => {
  return axios.request({
    url: '/web/order/update',
    data,
    method: 'post'
  })
}
export const push = (data) => {
  return axios.request({
    url: '/web/order/push',
    data,
    method: 'post'
  })
}
