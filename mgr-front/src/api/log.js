import request from '../utils/request'

export const logApi = {
  // 分页查询用户登录日志
  findCustomerLoginLogPage(page, limit, conditions) {
    return request({
      url: `/mgr/log/findCustomerLoginLogPage/${page}/${limit}`,
      method: 'post',
      data: conditions
    })
  },

  // 分页查询司机登录日志
  findDriverLoginLogPage(page, limit, conditions) {
    return request({
      url: `/mgr/log/findDriverLoginLogPage/${page}/${limit}`,
      method: 'post',
      data: conditions
    })
  }
}
