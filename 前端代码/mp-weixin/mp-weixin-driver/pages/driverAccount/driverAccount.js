Page({
  data: {
    accountInfo: {
      totalAmount: '0.00',
      lockAmount: '0.00',
      availableAmount: '0.00',
      totalIncomeAmount: '0.00',
      totalPayAmount: '0.00'
    },
    detailList: [],
    page: 1,
    limit: 10,
    pages: 0,
    total: 0
  },

  onLoad: function() {
    this.getAccountInfo();
    this.getAccountDetail();
  },

  onShow: function() {
    if (typeof wx.hideTabBar === 'function') {
      wx.hideTabBar();
    }
    // 从提现页面返回时刷新数据
    this.getAccountInfo();
    this.getAccountDetail();
  },

  onPullDownRefresh: function() {
    var that = this;
    that.setData({ page: 1 });
    that.getAccountInfo();
    that.getAccountDetail();
    setTimeout(function() {
      wx.stopPullDownRefresh();
    }, 1000);
  },

  onReachBottom: function() {
    var that = this;
    if (that.data.page >= that.data.pages) return;
    that.setData({ page: that.data.page + 1 });
    that.getAccountDetail();
  },

  getAccountInfo: function() {
    var that = this;
    wx.request({
      url: 'http://localhost:8600/driver-api/driver/account/info',
      method: 'GET',
      header: {
        'Content-Type': 'application/json',
        'token': wx.getStorageSync('token') || ''
      },
      success: function(res) {
        if (res.data.code === 200 && res.data.data) {
          var data = res.data.data;
          that.setData({
            accountInfo: {
              totalAmount: data.totalAmount ? data.totalAmount.toFixed(2) : '0.00',
              lockAmount: data.lockAmount ? data.lockAmount.toFixed(2) : '0.00',
              availableAmount: data.availableAmount ? data.availableAmount.toFixed(2) : '0.00',
              totalIncomeAmount: data.totalIncomeAmount ? data.totalIncomeAmount.toFixed(2) : '0.00',
              totalPayAmount: data.totalPayAmount ? data.totalPayAmount.toFixed(2) : '0.00'
            }
          });
        }
      }
    });
  },

  getAccountDetail: function() {
    var that = this;
    wx.request({
      url: 'http://localhost:8600/driver-api/driver/account/detail/' + that.data.page + '/' + that.data.limit,
      method: 'GET',
      header: {
        'Content-Type': 'application/json',
        'token': wx.getStorageSync('token') || ''
      },
      success: function(res) {
        if (res.data.code === 200 && res.data.data) {
          var records = res.data.data.records || [];
          var processedList = records.map(function(item) {
            return {
              tradeTypeName: that.getTradeTypeName(item.tradeType),
              content: item.content || '',
              createTime: item.createTime || '',
              amountStr: item.amount ? item.amount.toFixed(2) : '0.00',
              tradeType: item.tradeType
            };
          });
          var newList = that.data.page === 1 ? processedList : that.data.detailList.concat(processedList);
          that.setData({
            detailList: newList,
            pages: res.data.data.pages,
            total: res.data.data.total
          });
        }
      }
    });
  },

  getTradeTypeName: function(tradeType) {
    var type = String(tradeType);
    switch (type) {
      case '1201':
        return '进账';
      case '1202':
        return '解锁';
      case '1203':
        return '提现';
      case '1204':
        return '系统奖励';
      default:
        return '未知';
    }
  },

  goWithdraw: function() {
    wx.navigateTo({
      url: '/pages/withdraw/withdraw?availableAmount=' + this.data.accountInfo.availableAmount
    });
  }
});