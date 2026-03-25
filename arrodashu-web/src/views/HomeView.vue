<template>
  <div class="home">
    <!-- Banner -->
    <div class="banner">
      <h1>探索树木的世界</h1>
      <p>收录数千种树木品种，为您提供专业的园林知识</p>
      <el-input
        v-model="searchQuery"
        placeholder="搜索树木品种..."
        size="large"
        class="search-input"
      >
        <template #append>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
        </template>
      </el-input>
    </div>

    <!-- 统计数据 -->
    <div class="stats">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-number">1000+</div>
            <div class="stat-label">品种数量</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-number">50+</div>
            <div class="stat-label">科属分类</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-number">500+</div>
            <div class="stat-label">知识文章</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-number">10000+</div>
            <div class="stat-label">用户收藏</div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 热门品种 -->
    <div class="section">
      <h2>热门品种</h2>
      <el-row :gutter="20">
        <el-col :span="6" v-for="i in 4" :key="i">
          <el-card class="species-card" :body-style="{ padding: '0px' }">
            <img src="https://via.placeholder.com/300x200?text=Tree" class="species-image" />
            <div class="species-info">
              <h3>银杏</h3>
              <p class="latin">Ginkgo biloba</p>
              <el-tag size="small" type="success">落叶乔木</el-tag>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const searchQuery = ref('')
const router = useRouter()

const handleSearch = () => {
  router.push({ path: '/species', query: { q: searchQuery.value } })
}
</script>

<style scoped>
.home {
  padding: 20px;
}

.banner {
  text-align: center;
  padding: 60px 20px;
  background: linear-gradient(135deg, #67c23a 0%, #95d475 100%);
  border-radius: 8px;
  margin-bottom: 40px;
}

.banner h1 {
  color: #fff;
  font-size: 36px;
  margin-bottom: 16px;
}

.banner p {
  color: rgba(255, 255, 255, 0.9);
  font-size: 18px;
  margin-bottom: 32px;
}

.search-input {
  max-width: 600px;
  margin: 0 auto;
}

.stats {
  margin-bottom: 40px;
}

.stat-card {
  text-align: center;
  padding: 20px;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #67c23a;
  margin-bottom: 8px;
}

.stat-label {
  color: #666;
}

.section {
  margin-bottom: 40px;
}

.section h2 {
  margin-bottom: 20px;
  color: #333;
}

.species-card {
  cursor: pointer;
  transition: transform 0.3s;
}

.species-card:hover {
  transform: translateY(-4px);
}

.species-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.species-info {
  padding: 16px;
}

.species-info h3 {
  margin: 0 0 8px;
  color: #333;
}

.latin {
  color: #999;
  font-style: italic;
  margin-bottom: 12px;
}
</style>
