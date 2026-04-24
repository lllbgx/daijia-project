<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">订单管理</h2>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="订单号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入订单号" clearable />
        </el-form-item>
        <el-form-item label="乘客ID">
          <el-input v-model="searchForm.customerId" placeholder="请输入乘客ID" clearable />
        </el-form-item>
        <el-form-item label="司机ID">
          <el-input v-model="searchForm.driverId" placeholder="请输入司机ID" clearable />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待接单" :value="1" />
            <el-option label="已接单" :value="2" />
            <el-option label="司机已到达" :value="3" />
            <el-option label="服务中" :value="5" />
            <el-option label="待支付" :value="7" />
            <el-option label="已支付" :value="8" />
            <el-option label="已取消" :value="-1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData" v-loading="loading" border style="width: 100%">
      <el-table-column type="index" label="序号" min-width="60" />
      <el-table-column prop="id" label="订单ID" min-width="70" />
      <el-table-column prop="orderNo" label="订单号" min-width="160" />
      <el-table-column prop="customerId" label="乘客ID" min-width="90" />
      <el-table-column prop="driverId" label="司机ID" min-width="90" />
      <el-table-column prop="startLocation" label="上车地点" min-width="180" show-overflow-tooltip />
      <el-table-column prop="endLocation" label="下车地点" min-width="180" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" min-width="90">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="realAmount" label="实付金额" min-width="90">
        <template #default="{ row }">
          ￥{{ row.realAmount || 0 }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" min-width="160" />
      <el-table-column label="操作" min-width="80" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="handleDetail(row)">
            详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 订单详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="订单详情"
      width="800px"
    >
      <el-descriptions :column="2" border v-if="orderDetail">
        <el-descriptions-item label="订单ID">{{ orderDetail.id }}</el-descriptions-item>
        <el-descriptions-item label="订单号">{{ orderDetail.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="乘客ID">{{ orderDetail.customerId }}</el-descriptions-item>
        <el-descriptions-item label="司机ID">{{ orderDetail.driverId || '未接单' }}</el-descriptions-item>
        <el-descriptions-item label="上车地点" :span="2">{{ orderDetail.startLocation }}</el-descriptions-item>
        <el-descriptions-item label="下车地点" :span="2">{{ orderDetail.endLocation }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag :type="getStatusType(orderDetail.status)">
            {{ getStatusText(orderDetail.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="实付金额">￥{{ orderDetail.realAmount || 0 }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ orderDetail.createTime }}</el-descriptions-item>
        <el-descriptions-item label="接单时间">{{ orderDetail.acceptTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="开始服务时间">{{ orderDetail.startServiceTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="结束服务时间">{{ orderDetail.endServiceTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="支付时间">{{ orderDetail.payTime || '-' }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'
import { orderApi } from '../../api/order'

const loading = ref(false)
const tableData = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const detailVisible = ref(false)
const orderDetail = ref(null)

const searchForm = reactive({
  orderNo: '',
  customerId: '',
  driverId: '',
  status: null
})

const getStatusType = (status) => {
  const typeMap = {
    "-1": 'info',
    1: 'warning',
    2: 'primary',
    3: 'primary',
    5: 'primary',
    7: 'warning',
    8: 'success'
  }
  return typeMap[status] || 'info'
}

const getStatusText = (status) => {
  const textMap = {
    "-1": '已取消',
    1: '待接单',
    2: '已接单',
    3: '司机已到达',
    5: '服务中',
    7: '待支付',
    8: '已支付'
  }
  return textMap[status] || '未知'
}

const fetchData = async () => {
  loading.value = true
  try {
    let result
    if (searchForm.orderNo) {
      result = { records: [await orderApi.getOrderInfoByOrderNo(searchForm.orderNo)], total: 1 }
    } else if (searchForm.customerId) {
      result = await orderApi.findOrderInfoPageByCustomerId(currentPage.value, pageSize.value, searchForm.customerId)
    } else if (searchForm.driverId) {
      result = await orderApi.findOrderInfoPageByDriverId(currentPage.value, pageSize.value, searchForm.driverId)
    } else if (searchForm.status !== null) {
      result = await orderApi.findOrderInfoPageByStatus(currentPage.value, pageSize.value, searchForm.status)
    } else {
      result = await orderApi.findOrderInfoPage(currentPage.value, pageSize.value)
    }
    tableData.value = result.records || []
    total.value = result.total || 0
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  fetchData()
}

const handleReset = () => {
  searchForm.orderNo = ''
  searchForm.customerId = ''
  searchForm.driverId = ''
  searchForm.status = null
  currentPage.value = 1
  fetchData()
}

const handleDetail = async (row) => {
  try {
    orderDetail.value = await orderApi.getOrderInfoById(row.id)
    detailVisible.value = true
  } catch (error) {
    ElMessage.error('获取订单详情失败')
  }
}

const handleSizeChange = (val) => {
  pageSize.value = val
  fetchData()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchData()
}

onMounted(() => {
  fetchData()
})
</script>
