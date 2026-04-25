<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">日志管理</h2>
    </div>

    <div class="log-type-tabs">
      <el-radio-group v-model="logType" @change="handleTypeChange">
        <el-radio-button value="customer">用户登录日志</el-radio-button>
        <el-radio-button value="driver">司机登录日志</el-radio-button>
      </el-radio-group>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item :label="logType === 'customer' ? '用户ID' : '司机ID'">
          <el-input v-model="searchForm.id" :placeholder="logType === 'customer' ? '请输入用户ID' : '请输入司机ID'" clearable />
        </el-form-item>
        <el-form-item label="IP地址">
          <el-input v-model="searchForm.ipaddr" placeholder="请输入IP地址" clearable />
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
      <el-table-column :prop="logType === 'customer' ? 'customerId' : 'driverId'" :label="logType === 'customer' ? '用户ID' : '司机ID'" min-width="100" />
      <el-table-column prop="ipaddr" label="IP地址" min-width="150" />
      <el-table-column prop="msg" label="登录信息" min-width="200" show-overflow-tooltip />
      <el-table-column prop="createTime" label="登陆时间" min-width="180" />
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'
import { logApi } from '../../api/log'

const loading = ref(false)
const tableData = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const logType = ref('customer')

const searchForm = reactive({
  id: '',
  ipaddr: ''
})

const fetchData = async () => {
  loading.value = true
  try {
    const conditions = {}
    if (searchForm.id) {
      if (logType.value === 'customer') {
        conditions.customerId = searchForm.id
      } else {
        conditions.driverId = searchForm.id
      }
    }
    if (searchForm.ipaddr) {
      conditions.ipaddr = searchForm.ipaddr
    }

    const result = logType.value === 'customer'
      ? await logApi.findCustomerLoginLogPage(currentPage.value, pageSize.value, conditions)
      : await logApi.findDriverLoginLogPage(currentPage.value, pageSize.value, conditions)

    tableData.value = result.records || []
    total.value = result.total || 0
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const handleTypeChange = () => {
  currentPage.value = 1
  searchForm.id = ''
  searchForm.ipaddr = ''
  fetchData()
}

const handleSearch = () => {
  currentPage.value = 1
  fetchData()
}

const handleReset = () => {
  searchForm.id = ''
  searchForm.ipaddr = ''
  currentPage.value = 1
  fetchData()
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

<style scoped>
.log-type-tabs {
  margin-bottom: 20px;
}
</style>
