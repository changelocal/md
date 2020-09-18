import axios from '@/libs/api.request'

export const query = (data) => {
  return axios.request({
    url: '/web/searchRecord/query',
    data,
    method: 'post'
  })
}

export const update = (data) => {
  return axios.request({
    url: '/web/searchRecord/update',
    data,
    method: 'post'
  })
}
