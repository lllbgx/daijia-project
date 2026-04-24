import request from '../utils/request'

export const couponApi = {
  // 分页查询优惠券
  findCouponInfoPage(page, limit) {
    return request({
      url: `/mgr/coupon/info/findCouponInfoPage/${page}/${limit}`,
      method: 'get'
    })
  },

  // 根据ID获取优惠券
  getCouponInfoById(id) {
    return request({
      url: `/mgr/coupon/info/getCouponInfoById/${id}`,
      method: 'get'
    })
  },

  // 根据名称分页查询优惠券
  findCouponInfoPageByName(page, limit, name) {
    return request({
      url: `/mgr/coupon/info/findCouponInfoPageByName/${page}/${limit}`,
      method: 'get',
      params: { name }
    })
  },

  // 根据状态分页查询优惠券
  findCouponInfoPageByStatus(page, limit, status) {
    return request({
      url: `/mgr/coupon/info/findCouponInfoPageByStatus/${page}/${limit}`,
      method: 'get',
      params: { status }
    })
  },

  // 更新优惠券状态
  updateCouponStatusById(id, status) {
    return request({
      url: `/mgr/coupon/info/updateCouponStatusById/${id}/${status}`,
      method: 'put'
    })
  },

  // 新增优惠券（前端实现，后端暂无对应接口）
  addCoupon(data) {
    // 这里暂时返回成功，后端 API 待补充
    return Promise.resolve({ success: true })
  },

  // 删除优惠券（前端实现，后端暂无对应接口）
  deleteCoupon(id) {
    // 这里暂时返回成功，后端 API 待补充
    return Promise.resolve({ success: true })
  }
}
