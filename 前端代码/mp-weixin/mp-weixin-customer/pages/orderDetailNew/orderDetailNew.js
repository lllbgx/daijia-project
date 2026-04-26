Page({
  data: {
    orderId: '',
    status: 0,
    driverInfo: null,
    startLocation: '',
    endLocation: '',
    createTime: '',
    feeList: [],
    orderComment: null,
    commentRate: 0,
    customerId: ''
  },

  onLoad: function(options) {
    var orderId = options.orderId || '';
    this.setData({ orderId: orderId });
    if (orderId) {
      this.loadOrderDetail(orderId);
    }
  },

  loadOrderDetail: function(orderId) {
    var that = this;
    wx.showLoading({ title: '加载中...' });

    wx.request({
      url: 'http://localhost:8600/customer-api/order/getOrderInfo/' + orderId,
      method: 'GET',
      header: {
        'Content-Type': 'application/json',
        'token': wx.getStorageSync('token') || ''
      },
      success: function(res) {
        wx.hideLoading();
        if (res.data.code === 200) {
          var data = res.data.data;
          var feeList = [
            { label: '里程费', value: data.orderBillVo ? data.orderBillVo.distanceFee : 0 },
            { label: '等时费用', value: data.orderBillVo ? data.orderBillVo.waitFee : 0 },
            { label: '路桥费', value: data.orderBillVo ? data.orderBillVo.tollFee : 0 },
            { label: '停车费', value: data.orderBillVo ? data.orderBillVo.parkingFee : 0 },
            { label: '其他费用', value: data.orderBillVo ? data.orderBillVo.otherFee : 0 },
            { label: '远程费', value: data.orderBillVo ? data.orderBillVo.longDistanceFee : 0 },
            { label: '顾客好处费', value: data.orderBillVo ? data.orderBillVo.favourFee : 0 },
            { label: '系统奖励费', value: data.orderBillVo ? data.orderBillVo.rewardFee : 0 },
            { label: '优惠券金额', value: data.orderBillVo ? -(data.orderBillVo.couponAmount || 0) : 0 },
            { label: '总费用', value: data.orderBillVo ? data.orderBillVo.totalAmount : 0 },
            { label: '应付费用', value: data.orderBillVo ? data.orderBillVo.payAmount : 0 }
          ];

          that.setData({
            status: data.status,
            driverInfo: data.driverInfoVo || null,
            startLocation: data.startLocation || '',
            endLocation: data.endLocation || '',
            createTime: data.createTime || '',
            feeList: feeList,
            customerId: data.customerId
          });

          // 如果状态为9，加载评价详情
          if (data.status === 9) {
            that.loadOrderComment(orderId);
          }
        } else {
          wx.showToast({ title: res.data.message || '获取订单详情失败', icon: 'none' });
        }
      },
      fail: function(err) {
        wx.hideLoading();
        wx.showToast({ title: '获取订单详情失败', icon: 'error' });
      }
    });
  },

  loadOrderComment: function(orderId) {
    var that = this;
    wx.request({
      url: 'http://localhost:8600/customer-api/order/comment/get/' + orderId,
      method: 'GET',
      header: {
        'Content-Type': 'application/json',
        'token': wx.getStorageSync('token') || ''
      },
      success: function(res) {
        if (res.data.code === 200 && res.data.data) {
          that.setData({
            orderComment: res.data.data,
            commentRate: res.data.data.rate || 5
          });
        }
      },
      fail: function(err) {
        console.log('获取评价失败', err);
      }
    });
  },

  goComment: function() {
    var that = this;
    var driverId = that.data.driverInfo ? that.data.driverInfo.driverId : '';
    // var userInfo = wx.getStorageSync('userInfo');
    // var customerId = userInfo.id; 
    console.log("===== 页面传过来的参数data =====", that.data); // 加这一行
    wx.navigateTo({
      url: '/pages/orderComment/orderComment?orderId=' + that.data.orderId + '&driverId=' + driverId + '&customerId=' + that.data.customerId
    });
  },

  goBack: function() {
    wx.navigateBack();
  }
});