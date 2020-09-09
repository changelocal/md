import axios from '@/libs/api.request'

export const query = (data) => {
  return axios.request({
    url: '/web/wxuser/query',
    data,
    method: 'post'
  })
}
export const update = (data) => {
  return axios.request({
    url: '/web/wxuser/update',
    data,
    method: 'post'
  })
}
