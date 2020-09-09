import axios from '@/libs/api.request'

export const query = (data) => {
  return axios.request({
    url: '/web/brand/query',
    data,
    method: 'post'
  })
}
export const update = (data) => {
  return axios.request({
    url: '/web/brand/update',
    data,
    method: 'post'
  })
}
export const add = (data) => {
  return axios.request({
    url: '/web/brand/add',
    data,
    method: 'post'
  })
}
