import axios from '@/libs/api.request'

export const query = (data) => {
  return axios.request({
    url: '/web/consultation/query',
    data,
    method: 'post'
  })
}
