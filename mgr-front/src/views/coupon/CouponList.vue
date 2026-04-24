<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">优惠券管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增优惠券
      </el-button>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="优惠券名称">
          <el-input v-model="searchForm.name" placeholder="请输入优惠券名称" clearable />
        </el-form-item>
        <el-form-item label="优惠券状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
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
      <el-table-column prop="id" label="ID" min-width="60" />
      <el-table-column prop="name" label="优惠券名称" min-width="150" />
      <el-table-column prop="couponType" label="优惠券类型" min-width="90">
        <template #default="{ row }">
          {{ row.couponType === 1 ? '现金券' : '折扣券' }}
        </template>
      </el-table-column>
      <el-table-column prop="amount" label="优惠金额" min-width="90">
        <template #default="{ row }">
          ￥{{ row.amount }}
        </template>
      </el-table-column>
      <el-table-column prop="conditionAmount" label="使用门槛" min-width="110">
        <template #default="{ row }">
          满 ￥{{ row.conditionAmount }} 可用
        </template>
      </el-table-column>
      <el-table-column prop="totalNum" label="发放数量" min-width="90" />
      <el-table-column prop="remainNum" label="剩余数量" min-width="90" />
      <el-table-column prop="perLimit" label="每人限领" min-width="90" />
      <el-table-column prop="status" label="状态" min-width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="expireTime" label="过期时间" min-width="160" />
      <el-table-column label="操作" min-width="130" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="handleEdit(row)">
            编辑
          </el-button>
          <el-button type="danger" size="small" @click="handleDelete(row)">
            删除
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

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑优惠券' : '新增优惠券'"
      width="600px"
    >
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="优惠券名称" prop="couponName">
          <el-input v-model="formData.couponName" placeholder="请输入优惠券名称" />
        </el-form-item>
        <el-form-item label="优惠券类型" prop="couponType">
          <el-radio-group v-model="formData.couponType">
            <el-radio :value="1">现金券</el-radio>
            <el-radio :value="2">折扣券</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="优惠金额" prop="amount">
          <el-input-number v-model="formData.amount" :min="0" :precision="2" :step="0.01" />
          <span style="margin-left: 10px;">元</span>
        </el-form-item>
        <el-form-item label="使用门槛" prop="conditionAmount" v-if="formData.couponType === 1">
          <el-input-number v-model="formData.conditionAmount" :min="0" :precision="2" :step="0.01" />
          <span style="margin-left: 10px;">元</span>
        </el-form-item>
        <el-form-item label="发放数量" prop="totalNum">
          <el-input-number v-model="formData.totalNum" :min="1" />
          <span style="margin-left: 10px;">张</span>
        </el-form-item>
        <el-form-item label="每人限领" prop="perLimit">
          <el-input-number v-model="formData.perLimit" :min="1" />
          <span style="margin-left: 10px;">张</span>
        </el-form-item>
        <el-form-item label="过期时间" prop="expireTime">
          <el-date-picker
            v-model="formData.expireTime"
            type="datetime"
            placeholder="选择过期时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="formData.status"
            :active-value="1"
            :inactive-value="0"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh } from '@element-plus/icons-vue'
import { couponApi } from '../../api/coupon'

const loading = ref(false)
const tableData = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const searchForm = reactive({
  name: '',
  status: null
})

const formData = reactive({
  id: null,
  couponName: '',
  couponType: 1,
  amount: 0,
  conditionAmount: 0,
  totalNum: 100,
  perLimit: 1,
  expireTime: '',
  status: 1
})

const rules = {
  couponName: [{ required: true, message: '请输入优惠券名称', trigger: 'blur' }],
  couponType: [{ required: true, message: '请选择优惠券类型', trigger: 'change' }],
  amount: [{ required: true, message: '请输入优惠金额', trigger: 'blur' }],
  conditionAmount: [{ required: true, message: '请输入使用门槛', trigger: 'blur' }],
  totalNum: [{ required: true, message: '请输入发放数量', trigger: 'blur' }],
  perLimit: [{ required: true, message: '请输入每人限领数量', trigger: 'blur' }],
  expireTime: [{ required: true, message: '请选择过期时间', trigger: 'change' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    let result
    if (searchForm.name && searchForm.status !== null) {
      result = await couponApi.findCouponInfoPageByName(currentPage.value, pageSize.value, searchForm.name)
    } else if (searchForm.name) {
      result = await couponApi.findCouponInfoPageByName(currentPage.value, pageSize.value, searchForm.name)
    } else if (searchForm.status !== null) {
      result = await couponApi.findCouponInfoPageByStatus(currentPage.value, pageSize.value, searchForm.status)
    } else {
      result = await couponApi.findCouponInfoPage(currentPage.value, pageSize.value)
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
  searchForm.name = ''
  searchForm.status = null
  currentPage.value = 1
  fetchData()
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(formData, {
    id: null,
    couponName: '',
    couponType: 1,
    amount: 0,
    conditionAmount: 0,
    totalNum: 100,
    perLimit: 1,
    expireTime: '',
    status: 1
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该优惠券吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await couponApi.deleteCoupon(row.id)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          ElMessage.info('编辑功能暂未实现，后端 API 待补充')
        } else {
          await couponApi.addCoupon(formData)
          ElMessage.success('新增成功（后端 API 待补充）')
        }
        dialogVisible.value = false
        fetchData()
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }
  })
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
