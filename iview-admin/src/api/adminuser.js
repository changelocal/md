import axios from '@/libs/api.request'

export const query = (data) => {
  return axios.request({
    url: '/admin/user/query',
    data,
    method: 'post'
  })
}


