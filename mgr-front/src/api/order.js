import request from '../utils/request'

export const orderApi = {
  // 分页查询订单
  findOrderInfoPage(page, limit) {
    return request({
      url: `/mgr/order/info/findOrderInfoPage/${page}/${limit}`,
      method: 'get'
    })
  },

  // 根据ID获取订单
  getOrderInfoById(id) {
    return request({
      url: `/mgr/order/info/getOrderInfoById/${id}`,
      method: 'get'
    })
  },

  // 根据订单号获取订单
  getOrderInfoByOrderNo(orderNo) {
    return request({
      url: '/mgr/order/info/getOrderInfoByOrderNo',
      method: 'get',
      params: { orderNo }
    })
  },

  // 根据乘客ID分页查询订单
  findOrderInfoPageByCustomerId(page, limit, customerId) {
    return request({
      url: `/mgr/order/info/findOrderInfoPageByCustomerId/${page}/${limit}`,
      method: 'get',
      params: { customerId }
    })
  },

  // 根据司机ID分页查询订单
  findOrderInfoPageByDriverId(page, limit, driverId) {
    return request({
      url: `/mgr/order/info/findOrderInfoPageByDriverId/${page}/${limit}`,
      method: 'get',
      params: { driverId }
    })
  },

  // 根据订单状态分页查询订单
  findOrderInfoPageByStatus(page, limit, status) {
    return request({
      url: `/mgr/order/info/findOrderInfoPageByStatus/${page}/${limit}`,
      method: 'get',
      params: { status }
    })
  }
}
