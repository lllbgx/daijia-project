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

  // 新增优惠券
  addCoupon(data) {
    return request({
      url: `/mgr/coupon/info/addCoupon`,
      method: 'post',
      data
    })
  },

  // 删除优惠券
  deleteCoupon(id) {
    return request({
      url: `/mgr/coupon/info/deleteCoupon/${id}`,
      method: 'delete'
    })
  },

  // 根据多条件分页查询优惠券
  findCouponInfoPageByCondition(page, limit, conditions) {
    return request({
      url: `/mgr/coupon/info/findCouponInfoPageByCondition/${page}/${limit}`,
      method: 'post',
      data: conditions
    })
  }
}
