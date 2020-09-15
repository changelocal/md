import axios from '@/libs/api.request'

export const loadSelect = () => {
  return axios.request({
    url: '/admin/user/count',
    method: 'get'
  })
}
