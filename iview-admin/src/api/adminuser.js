import axios from '@/libs/api.request'

export const query = (data) => {
  return axios.request({
    url: '/admin/user/query',
    data,
    method: 'post'
  })
}

export const add = (data) => {
  return axios.request({
    url: '/admin/user/add',
    data,
    method: 'post'
  })
}

export const update = (data) => {
  return axios.request({
    url: '/admin/user/update',
    data,
    method: 'post'
  })
}
