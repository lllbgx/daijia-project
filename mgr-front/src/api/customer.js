import request from '../utils/request'

export const customerApi = {
  // 分页查询用户
  findCustomerInfoPage(page, limit) {
    return request({
      url: `/mgr/customer/info/findCustomerInfoPage/${page}/${limit}`,
      method: 'get'
    })
  },

  // 根据名称分页查询用户
  findCustomerInfoPageByName(page, limit, nickname) {
    return request({
      url: `/mgr/customer/info/findCustomerInfoPageByName/${page}/${limit}`,
      method: 'get',
      params: { nickname }
    })
  },

  // 更新用户状态
  updateCustomerStatus(id, status) {
    return request({
      url: `/mgr/customer/info/updateCustomerStatus/${id}/${status}`,
      method: 'put'
    })
  }
}
