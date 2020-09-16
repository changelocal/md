import axios from '@/libs/api.request'

export const loadSelect = () => {
  return axios.request({
    url: '/web/common/loadService',
    method: 'get'
  })
}
