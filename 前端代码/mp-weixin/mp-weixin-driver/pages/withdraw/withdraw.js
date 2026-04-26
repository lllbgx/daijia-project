Page({
  data: {
    availableAmount: '0.00',
    amount: ''
  },

  onLoad: function(options) {
    if (options.availableAmount) {
      this.setData({ availableAmount: options.availableAmount });
    }
  },

  onAmountInput: function(e) {
    this.setData({ amount: e.detail.value });
  },

  onWithdraw: function() {
    var that = this;
    var amount = parseFloat(this.data.amount);

    if (!amount || amount <= 0) {
      wx.showToast({ title: '请输入有效金额', icon: 'none' });
      return;
    }

    var available = parseFloat(this.data.availableAmount);
    if (amount > available) {
      wx.showToast({ title: '超过可提现金额', icon: 'none' });
      return;
    }

    wx.showLoading({ title: '提现中...' });

    wx.request({
      url: 'http://localhost:8600/driver-api/driver/account/withdraw',
      method: 'POST',
      header: {
        'Content-Type': 'application/json',
        'token': wx.getStorageSync('token') || ''
      },
      data: {
        amount: amount,
        content: '司机提现',
        tradeNo: 'TX' + Date.now()
      },
      success: function(res) {
        wx.hideLoading();
        if (res.data.code === 200) {
          wx.showToast({ title: '提现成功', icon: 'success' });
          setTimeout(function() {
            wx.navigateBack();
          }, 1500);
        } else {
          wx.showToast({ title: res.data.message || '提现失败', icon: 'none' });
        }
      },
      fail: function(err) {
        wx.hideLoading();
        wx.showToast({ title: '提现失败', icon: 'error' });
      }
    });
  }
});