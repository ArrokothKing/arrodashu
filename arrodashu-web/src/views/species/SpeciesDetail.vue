<template>
  <div class="species-detail">
    <el-page-header @back="goBack" title="品种详情" />
    
    <el-card v-if="species" class="detail-card">
      <el-row :gutter="40">
        <!-- 左侧图片 -->
        <el-col :span="8">
          <el-image
            :src="getDefaultImage()"
            fit="cover"
            style="width: 100%; height: 300px; border-radius: 8px"
          />
        </el-col>
        
        <!-- 右侧信息 -->
        <el-col :span="16">
          <h1 class="species-name">{{ species.name }}</h1>
          <p class="latin-name">{{ species.latinName }}</p>
          <p v-if="species.alias" class="alias">别名：{{ species.alias }}</p>
          
          <el-descriptions :column="2" border>
            <el-descriptions-item label="科">{{ species.familyName }}</el-descriptions-item>
            <el-descriptions-item label="属">{{ species.genusName }}</el-descriptions-item>
            <el-descriptions-item label="树型">{{ getTreeType(species.treeType) }}</el-descriptions-item>
            <el-descriptions-item label="高度">{{ getHeight(species) }}</el-descriptions-item>
            <el-descriptions-item label="光照需求">{{ species.lightPreference || '-' }}</el-descriptions-item>
            <el-descriptions-item label="水分需求">{{ species.waterPreference || '-' }}</el-descriptions-item>
            <el-descriptions-item label="土壤要求">{{ species.soilPreference || '-' }}</el-descriptions-item>
            <el-descriptions-item label="耐寒区">{{ species.hardinessZone || '-' }}</el-descriptions-item>
            <el-descriptions-item label="生长速度">{{ species.growthRate || '-' }}</el-descriptions-item>
            <el-descriptions-item label="原产地">{{ species.origin || '-' }}</el-descriptions-item>
          </el-descriptions>
          
          <div class="actions">
            <el-button type="primary">收藏</el-button>
            <el-button @click="handleEdit">编辑</el-button>
          </div>
        </el-col>
      </el-row>
      
      <!-- 详细描述 -->
      <el-divider />
      
      <div class="description-section">
        <h3>详细描述</h3>
        <p>{{ species.description || '暂无描述' }}</p>
      </div>
      
      <div class="description-section" v-if="species.landscapeUse">
        <h3>园林用途</h3>
        <p>{{ species.landscapeUse }}</p>
      </div>
    </el-card>
    
    <el-skeleton v-else :rows="10" animated />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { Species } from '@/api/species'
import { getSpeciesDetail } from '@/api/species'

const route = useRoute()
const router = useRouter()
const species = ref<Species>()
const loading = ref(false)

// 获取默认图片
const getDefaultImage = () => {
  return 'https://via.placeholder.com/400x300?text=Tree'
}

// 获取树型文本
const getTreeType = (type?: number) => {
  const types: Record<number, string> = {
    1: '乔木',
    2: '灌木',
    3: '藤本'
  }
  return type ? types[type] || '-' : '-'
}

// 获取高度范围
const getHeight = (item: Species) => {
  if (!item.heightMin && !item.heightMax) return '-'
  const min = item.heightMin ? `${item.heightMin}cm` : ''
  const max = item.heightMax ? `${item.heightMax}cm` : ''
  return min && max ? `${min} - ${max}` : min || max
}

// 返回
const goBack = () => {
  router.back()
}

// 编辑
const handleEdit = () => {
  ElMessage.info('编辑功能开发中')
}

// 加载详情
const loadDetail = async () => {
  const id = Number(route.params.id)
  if (!id) {
    ElMessage.error('参数错误')
    return
  }
  
  loading.value = true
  try {
    const res = await getSpeciesDetail(id)
    species.value = res.data
  } catch (error) {
    ElMessage.error('加载品种详情失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadDetail()
})
</script>

<style scoped>
.species-detail {
  padding: 20px;
}

.detail-card {
  margin-top: 20px;
}

.species-name {
  font-size: 28px;
  margin: 0 0 8px;
  color: #333;
}

.latin-name {
  font-size: 16px;
  color: #666;
  font-style: italic;
  margin-bottom: 8px;
}

.alias {
  color: #999;
  margin-bottom: 20px;
}

.actions {
  margin-top: 20px;
}

.description-section {
  margin-top: 20px;
}

.description-section h3 {
  color: #333;
  margin-bottom: 12px;
}

.description-section p {
  color: #666;
  line-height: 1.8;
}
</style>
