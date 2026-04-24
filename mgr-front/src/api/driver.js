import request from '../utils/request'

export const driverApi = {
  // 分页查询司机
  findDriverInfoPage(page, limit) {
    return request({
      url: `/mgr/driver/info/findDriverInfoPage/${page}/${limit}`,
      method: 'get'
    })
  },

  // 根据名称分页查询司机
  findDriverInfoPageByName(page, limit, name) {
    return request({
      url: `/mgr/driver/info/findDriverInfoPageByName/${page}/${limit}`,
      method: 'get',
      params: { name }
    })
  },

  // 根据认证状态分页查询司机
  findDriverInfoPageByAuthStatus(page, limit, authStatus) {
    return request({
      url: `/mgr/driver/info/findDriverInfoPageByAuthStatus/${page}/${limit}`,
      method: 'get',
      params: { authStatus }
    })
  },

  // 更新司机状态
  updateDriverStatus(id, status) {
    return request({
      url: `/mgr/driver/info/updateDriverStatus/${id}/${status}`,
      method: 'put'
    })
  }
}
