<template>
  <div class="app">
    <el-container>
      <!-- 顶部导航 -->
      <el-header class="header">
        <div class="logo">
          <el-icon :size="32"><House /></el-icon>
          <span class="title">大树 ArroDashu</span>
        </div>
        <el-menu
          mode="horizontal"
          :default-active="$route.path"
          router
          class="nav-menu"
        >
          <el-menu-item index="/">首页</el-menu-item>
          <el-menu-item index="/species">品种大全</el-menu-item>
          <el-menu-item index="/knowledge">知识中心</el-menu-item>
        </el-menu>
        <div class="user-actions">
          <template v-if="userStore.isLoggedIn()">
            <el-dropdown>
              <span class="user-info">
                <el-avatar :size="32" :src="userStore.userInfo?.avatar" />
                <span>{{ userStore.userInfo?.nickname || userStore.userInfo?.username }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="$router.push('/user')">个人中心</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="primary" @click="$router.push('/login')">登录</el-button>
          </template>
        </div>
      </el-header>

      <!-- 主内容区 -->
      <el-main class="main">
        <router-view />
      </el-main>

      <!-- 底部 -->
      <el-footer class="footer">
        <p>© 2026 大树 ArroDashu - 树木品种管理平台</p>
      </el-footer>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { House } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const router = useRouter()

const handleLogout = async () => {
  await userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/')
}
</script>

<style scoped>
.app {
  min-height: 100vh;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #67c23a;
}

.logo .title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

.nav-menu {
  flex: 1;
  margin: 0 40px;
  border-bottom: none;
}

.user-actions {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.main {
  min-height: calc(100vh - 120px);
  background: #f5f7fa;
}

.footer {
  text-align: center;
  color: #999;
  background: #fff;
}
</style>
