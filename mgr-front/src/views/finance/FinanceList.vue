<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">财务管理</h2>
    </div>

    <!-- 统计概览卡片 -->
    <div class="statistics-cards">
      <div class="stat-card">
        <div class="stat-label">今日营收</div>
        <div class="stat-value income">¥{{ statistics.todayIncome || '0.00' }}</div>
      </div>
      <div class="stat-card">
        <div class="stat-label">本周营收</div>
        <div class="stat-value income">¥{{ statistics.weekIncome || '0.00' }}</div>
      </div>
      <div class="stat-card">
        <div class="stat-label">本月营收</div>
        <div class="stat-value income">¥{{ statistics.monthIncome || '0.00' }}</div>
      </div>
      <div class="stat-card">
        <div class="stat-label">今日订单</div>
        <div class="stat-value">{{ statistics.todayOrderCount || 0 }}</div>
      </div>
      <div class="stat-card">
        <div class="stat-label">本周订单</div>
        <div class="stat-value">{{ statistics.weekOrderCount || 0 }}</div>
      </div>
      <div class="stat-card">
        <div class="stat-label">本月订单</div>
        <div class="stat-value">{{ statistics.monthOrderCount || 0 }}</div>
      </div>
      <div class="stat-card">
        <div class="stat-label">今日提现</div>
        <div class="stat-value withdraw">¥{{ statistics.todayWithdraw || '0.00' }}</div>
      </div>
      <div class="stat-card">
        <div class="stat-label">本周提现</div>
        <div class="stat-value withdraw">¥{{ statistics.weekWithdraw || '0.00' }}</div>
      </div>
      <div class="stat-card">
        <div class="stat-label">本月提现</div>
        <div class="stat-value withdraw">¥{{ statistics.monthWithdraw || '0.00' }}</div>
      </div>
    </div>

    <!-- 标签页切换 -->
    <el-tabs v-model="activeTab" class="finance-tabs" @tab-change="handleTabChange">
      <!-- 订单报表 -->
      <el-tab-pane label="订单报表" name="order">
        <div class="search-form">
          <el-form :inline="true" :model="orderSearchForm">
            <el-form-item label="开始日期">
              <el-date-picker
                v-model="orderSearchForm.startDate"
                type="date"
                placeholder="选择开始日期"
                value-format="YYYY-MM-DD"
                clearable
              />
            </el-form-item>
            <el-form-item label="结束日期">
              <el-date-picker
                v-model="orderSearchForm.endDate"
                type="date"
                placeholder="选择结束日期"
                value-format="YYYY-MM-DD"
                clearable
              />
            </el-form-item>
            <el-form-item label="订单号">
              <el-input v-model="orderSearchForm.orderNo" placeholder="请输入订单号" clearable />
            </el-form-item>
            <el-form-item label="订单状态">
              <el-select v-model="orderSearchForm.status" placeholder="请选择状态" clearable>
                <el-option label="待支付" :value="7" />
                <el-option label="已支付" :value="8" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleOrderSearch">
                <el-icon><Search /></el-icon>
                搜索
              </el-button>
              <el-button @click="handleOrderReset">
                <el-icon><Refresh /></el-icon>
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </div>

        <el-table :data="orderTableData" v-loading="orderLoading" border style="width: 100%" scroll-x="true">
          <el-table-column type="index" label="序号" min-width="60" fixed />
          <el-table-column prop="orderNo" label="订单号" min-width="150" fixed />
          <el-table-column prop="customerId" label="乘客ID" min-width="80" />
          <el-table-column prop="driverId" label="司机ID" min-width="80" />
          <el-table-column prop="startLocation" label="上车地点" min-width="150" show-overflow-tooltip />
          <el-table-column prop="endLocation" label="下车地点" min-width="150" show-overflow-tooltip />
          <el-table-column prop="payAmount" label="订单金额" min-width="90">
            <template #default="{ row }">
              ¥{{ row.payAmount || 0 }}
            </template>
          </el-table-column>
          <el-table-column prop="distanceFee" label="里程费" min-width="80">
            <template #default="{ row }">
              ¥{{ row.distanceFee || 0 }}
            </template>
          </el-table-column>
          <el-table-column prop="waitFee" label="等时费" min-width="80">
            <template #default="{ row }">
              ¥{{ row.waitFee || 0 }}
            </template>
          </el-table-column>
          <el-table-column prop="tollFee" label="路桥费" min-width="80">
            <template #default="{ row }">
              ¥{{ row.tollFee || 0 }}
            </template>
          </el-table-column>
          <el-table-column prop="parkingFee" label="停车费" min-width="80">
            <template #default="{ row }">
              ¥{{ row.parkingFee || 0 }}
            </template>
          </el-table-column>
          <el-table-column prop="otherFee" label="其他费用" min-width="80">
            <template #default="{ row }">
              ¥{{ row.otherFee || 0 }}
            </template>
          </el-table-column>
          <el-table-column prop="couponAmount" label="优惠券" min-width="80">
            <template #default="{ row }">
              -¥{{ row.couponAmount || 0 }}
            </template>
          </el-table-column>
          <el-table-column prop="platformIncome" label="平台收入" min-width="90">
            <template #default="{ row }">
              <span style="color: #67c23a">¥{{ row.platformIncome || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="driverIncome" label="司机收入" min-width="90">
            <template #default="{ row }">
              <span style="color: #409eff">¥{{ row.driverIncome || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="statusName" label="状态" min-width="80">
            <template #default="{ row }">
              <el-tag :type="row.status === 8 ? 'success' : row.status === 7 ? 'warning' : 'info'">
                {{ row.statusName || '未知' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" min-width="160" />
        </el-table>

        <div class="pagination">
          <el-pagination
            v-model:current-page="orderCurrentPage"
            v-model:page-size="orderPageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="orderTotal"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleOrderSizeChange"
            @current-change="handleOrderCurrentChange"
          />
        </div>
      </el-tab-pane>

      <!-- 提现记录 -->
      <el-tab-pane label="提现记录" name="withdraw">
        <div class="search-form">
          <el-form :inline="true" :model="withdrawSearchForm">
            <el-form-item label="开始日期">
              <el-date-picker
                v-model="withdrawSearchForm.startDate"
                type="date"
                placeholder="选择开始日期"
                value-format="YYYY-MM-DD"
                clearable
              />
            </el-form-item>
            <el-form-item label="结束日期">
              <el-date-picker
                v-model="withdrawSearchForm.endDate"
                type="date"
                placeholder="选择结束日期"
                value-format="YYYY-MM-DD"
                clearable
              />
            </el-form-item>
            <el-form-item label="司机ID">
              <el-input v-model="withdrawSearchForm.driverId" placeholder="请输入司机ID" clearable />
            </el-form-item>
            <el-form-item label="交易号">
              <el-input v-model="withdrawSearchForm.tradeNo" placeholder="请输入交易号" clearable />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleWithdrawSearch">
                <el-icon><Search /></el-icon>
                搜索
              </el-button>
              <el-button @click="handleWithdrawReset">
                <el-icon><Refresh /></el-icon>
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </div>

        <el-table :data="withdrawTableData" v-loading="withdrawLoading" border style="width: 100%">
          <el-table-column type="index" label="序号" min-width="60" />
          <el-table-column prop="tradeNo" label="交易号" min-width="180" />
          <el-table-column prop="driverId" label="司机ID" min-width="80" />
          <el-table-column prop="driverName" label="司机姓名" min-width="100" />
          <el-table-column prop="driverPhone" label="司机手机" min-width="120" />
          <el-table-column prop="tradeTypeName" label="交易类型" min-width="80" />
          <el-table-column prop="amount" label="提现金额" min-width="100">
            <template #default="{ row }">
              <span class="amount-red">-¥{{ row.amount || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="availableAmount" label="账户余额" min-width="100">
            <template #default="{ row }">
              ¥{{ row.availableAmount || 0 }}
            </template>
          </el-table-column>
          <el-table-column prop="content" label="交易说明" min-width="150" show-overflow-tooltip />
          <el-table-column prop="createTime" label="交易时间" min-width="160" />
        </el-table>

        <div class="pagination">
          <el-pagination
            v-model:current-page="withdrawCurrentPage"
            v-model:page-size="withdrawPageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="withdrawTotal"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleWithdrawSizeChange"
            @current-change="handleWithdrawCurrentChange"
          />
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'
import { financeApi } from '../../api/finance'

// 统计数据
const statistics = ref({
  todayIncome: '0.00',
  weekIncome: '0.00',
  monthIncome: '0.00',
  todayOrderCount: 0,
  weekOrderCount: 0,
  monthOrderCount: 0,
  todayWithdraw: '0.00',
  weekWithdraw: '0.00',
  monthWithdraw: '0.00'
})

// 标签页
const activeTab = ref('order')

// 订单报表相关
const orderLoading = ref(false)
const orderTableData = ref([])
const orderCurrentPage = ref(1)
const orderPageSize = ref(10)
const orderTotal = ref(0)

const orderSearchForm = reactive({
  startDate: '',
  endDate: '',
  orderNo: '',
  status: null
})

// 提现记录相关
const withdrawLoading = ref(false)
const withdrawTableData = ref([])
const withdrawCurrentPage = ref(1)
const withdrawPageSize = ref(10)
const withdrawTotal = ref(0)

const withdrawSearchForm = reactive({
  startDate: '',
  endDate: '',
  driverId: '',
  tradeNo: ''
})

// 获取统计数据
const fetchStatistics = async () => {
  try {
    const data = await financeApi.getStatistics()
    if (data) {
      statistics.value = data
    }
  } catch (error) {
    console.error('获取统计数据失败', error)
  }
}

// 订单报表搜索
const handleOrderSearch = () => {
  orderCurrentPage.value = 1
  fetchOrderReport()
}

const handleOrderReset = () => {
  orderSearchForm.startDate = ''
  orderSearchForm.endDate = ''
  orderSearchForm.orderNo = ''
  orderSearchForm.status = null
  orderCurrentPage.value = 1
  fetchOrderReport()
}

// 获取订单报表数据
const fetchOrderReport = async () => {
  orderLoading.value = true
  try {
    const form = {
      startDate: orderSearchForm.startDate,
      endDate: orderSearchForm.endDate,
      orderNo: orderSearchForm.orderNo,
      status: orderSearchForm.status
    }
    const res = await financeApi.getOrderReportPage(
      orderCurrentPage.value,
      orderPageSize.value,
      form
    )
    if (res) {
      orderTableData.value = res.records || []
      orderTotal.value = res.total || 0
    } else {
      orderTableData.value = []
      orderTotal.value = 0
    }
  } catch (error) {
    ElMessage.error('获取订单报表失败')
    orderTableData.value = []
  } finally {
    orderLoading.value = false
  }
}

// 提现记录搜索
const handleWithdrawSearch = () => {
  withdrawCurrentPage.value = 1
  fetchWithdrawReport()
}

const handleWithdrawReset = () => {
  withdrawSearchForm.startDate = ''
  withdrawSearchForm.endDate = ''
  withdrawSearchForm.driverId = ''
  withdrawSearchForm.tradeNo = ''
  withdrawCurrentPage.value = 1
  fetchWithdrawReport()
}

// 获取提现记录数据
const fetchWithdrawReport = async () => {
  withdrawLoading.value = true
  try {
    const form = {
      startDate: withdrawSearchForm.startDate,
      endDate: withdrawSearchForm.endDate,
      driverId: withdrawSearchForm.driverId ? parseInt(withdrawSearchForm.driverId) : null,
      tradeNo: withdrawSearchForm.tradeNo
    }
    const res = await financeApi.getWithdrawReportPage(
      withdrawCurrentPage.value,
      withdrawPageSize.value,
      form
    )
    if (res) {
      withdrawTableData.value = res.records || []
      withdrawTotal.value = res.total || 0
    } else {
      withdrawTableData.value = []
      withdrawTotal.value = 0
    }
  } catch (error) {
    ElMessage.error('获取提现记录失败')
    withdrawTableData.value = []
  } finally {
    withdrawLoading.value = false
  }
}

// 分页变化
const handleOrderSizeChange = (val) => {
  orderPageSize.value = val
  fetchOrderReport()
}

const handleOrderCurrentChange = (val) => {
  orderCurrentPage.value = val
  fetchOrderReport()
}

const handleWithdrawSizeChange = (val) => {
  withdrawPageSize.value = val
  fetchWithdrawReport()
}

const handleWithdrawCurrentChange = (val) => {
  withdrawCurrentPage.value = val
  fetchWithdrawReport()
}

// 标签页切换
const handleTabChange = (tabName) => {
  if (tabName === 'withdraw') {
    fetchWithdrawReport()
  }
}

onMounted(() => {
  fetchStatistics()
  fetchOrderReport()
})
</script>

<style scoped>
.page-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-title {
  font-size: 20px;
  font-weight: 500;
  color: #303133;
  margin: 0;
}

/* 统计卡片 */
.statistics-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  padding: 20px;
  color: #fff;
}

.stat-card:nth-child(4),
.stat-card:nth-child(5),
.stat-card:nth-child(6) {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
}

.stat-card:nth-child(7),
.stat-card:nth-child(8),
.stat-card:nth-child(9) {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
}

.stat-value.income {
  color: #ffd700;
}

.stat-value.withdraw {
  color: #ff6b6b;
}

/* 标签页 */
.finance-tabs {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
}

/* 搜索表单 */
.search-form {
  margin-bottom: 20px;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
}

/* 分页 */
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/* 金额样式 */
.amount-red {
  color: #f56c6c;
  font-weight: bold;
}
</style>