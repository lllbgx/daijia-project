<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">司机管理</h2>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="司机姓名">
          <el-input v-model="searchForm.name" placeholder="请输入司机姓名" clearable />
        </el-form-item>
        <el-form-item label="认证状态">
          <el-select v-model="searchForm.authStatus" placeholder="请选择认证状态" clearable>
            <el-option label="未认证" :value="0" />
            <el-option label="认证中" :value="1" />
            <el-option label="认证成功" :value="2" />
            <el-option label="认证失败" :value="-1" />
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
      <el-table-column prop="id" label="司机ID" min-width="80" />
      <el-table-column prop="name" label="姓名" min-width="100" />
      <el-table-column prop="phone" label="手机号" min-width="120" />
      <el-table-column prop="nickname" label="昵称" min-width="120" />
      <el-table-column prop="avatarUrl" label="头像" min-width="80">
        <template #default="{ row }">
          <el-image :src="row.avatarUrl" style="width: 50px; height: 50px; border-radius: 50%;" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column prop="idcardNo" label="身份证号" min-width="160" />
      <el-table-column prop="gender" label="性别" min-width="60">
        <template #default="{ row }">
          {{ row.gender === 1 ? '男' : row.gender === 2 ? '女' : '未知' }}
        </template>
      </el-table-column>
      <el-table-column prop="authStatus" label="认证状态" min-width="90">
        <template #default="{ row }">
          <el-tag :type="getAuthStatusType(row.authStatus)">
            {{ getAuthStatusText(row.authStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" min-width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="100" fixed="right">
        <template #default="{ row }">
          <el-switch
            v-model="row.status"
            :active-value="1"
            :inactive-value="0"
            @change="handleStatusChange(row)"
          />
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'
import { driverApi } from '../../api/driver'

const loading = ref(false)
const tableData = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchForm = reactive({
  name: '',
  authStatus: null
})

const getAuthStatusType = (status) => {
  const typeMap = {
    0: 'info',
    1: 'warning',
    2: 'success',
    '-1': 'danger'
  }
  return typeMap[status] || 'info'
}

const getAuthStatusText = (status) => {
  const textMap = {
    0: '未认证',
    1: '认证中',
    2: '认证成功',
    '-1': '认证失败'
  }
  return textMap[status] || '未知'
}

const fetchData = async () => {
  loading.value = true
  try {
    // 使用多条件查询 API
    const conditions = {}
    if (searchForm.name) {
      conditions.name = searchForm.name
    }
    if (searchForm.authStatus !== null) {
      conditions.authStatus = searchForm.authStatus
    }

    // 如果有条件，使用多条件查询；否则使用普通分页查询
    const hasCondition = Object.keys(conditions).length > 0
    const result = hasCondition
      ? await driverApi.findDriverInfoPageByCondition(currentPage.value, pageSize.value, conditions)
      : await driverApi.findDriverInfoPage(currentPage.value, pageSize.value)

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
  searchForm.name = ''
  searchForm.authStatus = null
  currentPage.value = 1
  fetchData()
}

const handleStatusChange = async (row) => {
  try {
    await driverApi.updateDriverStatus(row.id, row.status)
    ElMessage.success('状态更新成功')
  } catch (error) {
    ElMessage.error('状态更新失败')
    row.status = row.status === 1 ? 0 : 1
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
