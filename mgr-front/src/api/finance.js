import request from '../utils/request'

export const financeApi = {
  // 获取财务统计数据
  getStatistics() {
    return request({
      url: '/mgr/finance/statistics',
      method: 'get'
    })
  },

  // 获取订单报表列表（分页）
  getOrderReportPage(page, limit, form) {
    return request({
      url: `/mgr/finance/order/report/${page}/${limit}`,
      method: 'post',
      data: form
    })
  },

  // 获取提现记录列表（分页）
  getWithdrawReportPage(page, limit, form) {
    return request({
      url: `/mgr/finance/withdraw/report/${page}/${limit}`,
      method: 'post',
      data: form
    })
  }
}