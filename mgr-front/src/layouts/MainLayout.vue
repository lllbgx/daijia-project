<template>
  <el-container class="main-container">
    <el-aside width="200px" class="aside">
      <div class="logo">
        <h2>硅谷代驾管理</h2>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#1f2937"
        text-color="#e5e7eb"
        active-text-color="#3b82f6"
      >
        <el-menu-item index="/customer">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/driver">
          <el-icon><Van /></el-icon>
          <span>司机管理</span>
        </el-menu-item>
        <el-menu-item index="/coupon">
          <el-icon><Ticket /></el-icon>
          <span>优惠券管理</span>
        </el-menu-item>
        <el-menu-item index="/order">
          <el-icon><Document /></el-icon>
          <span>订单管理</span>
        </el-menu-item>
        <el-menu-item index="/log">
          <el-icon><List /></el-icon>
          <span>日志管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-title">{{ currentTitle }}</div>
        <div class="header-right">
          <el-dropdown>
            <span class="el-dropdown-link">
              管理员
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { User, Van, Ticket, Document, ArrowDown, List } from '@element-plus/icons-vue'

const route = useRoute()
const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta?.title || '管理后台')
</script>

<style scoped>
.main-container {
  height: 100vh;
}

.aside {
  background-color: #1f2937;
  color: #e5e7eb;
}

.logo {
  padding: 20px;
  text-align: center;
  border-bottom: 1px solid #374151;
}

.logo h2 {
  color: #fff;
  margin: 0;
  font-size: 18px;
}

.header {
  background-color: #fff;
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.header-title {
  font-size: 18px;
  font-weight: 500;
  color: #1f2937;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.el-dropdown-link {
  cursor: pointer;
  color: #1f2937;
  display: flex;
  align-items: center;
}

.main {
  background-color: #f3f4f6;
  padding: 20px;
}
</style>
