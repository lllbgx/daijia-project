Page({
  data: {
    orderId: '',
    driverId: '',
    customerId: '',
    stars: [1, 2, 3, 4, 5],
    rate: 5,
    remark: ''
  },

  onLoad: function(options) {
    console.log("===== 页面传过来的参数 =====", options); // 加这一行
    this.setData({
      orderId: options.orderId || '',
      driverId: options.driverId || '',
      customerId: options.customerId || ''
    });
  },

  selectStar: function(e) {
    this.setData({
      rate: e.currentTarget.dataset.rate
    });
  },

  onInput: function(e) {
    this.setData({
      remark: e.detail.value
    });
  },

  onCancel: function() {
    wx.navigateBack();
  },

  onSubmit: function() {
    var that = this;
    wx.showLoading({
      title: '提交中...',
      mask: true
    });

    wx.request({
      url: 'http://localhost:8600/customer-api/order/comment/submit',
      method: 'POST',
      header: {
        'Content-Type': 'application/json',
        'token': wx.getStorageSync('token') || ''
      },
      data: {
        orderId: that.data.orderId,
        driverId: that.data.driverId,
        customerId: that.data.customerId,
        rate: that.data.rate,
        remark: that.data.remark
      },
      success: function(res) {
        wx.hideLoading();
        if (res.data.code === 200) {
          wx.showToast({
            title: '评价成功',
            icon: 'success',
            duration: 1500
          });
          setTimeout(function() {
            wx.navigateBack();
          }, 1500);
        } else {
          wx.showToast({
            title: res.data.message || '评价失败',
            icon: 'none'
          });
        }
      },
      fail: function(err) {
        wx.hideLoading();
        wx.showToast({
          title: '评价失败',
          icon: 'error'
        });
      }
    });
  }
});