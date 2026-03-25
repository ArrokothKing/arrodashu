<template>
  <div class="species-list">
    <h1>品种大全</h1>
    
    <!-- 搜索和筛选 -->
    <el-card class="search-card">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input
            v-model="query.keyword"
            placeholder="搜索品种名称或拉丁名"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #append>
              <el-button :icon="Search" @click="handleSearch">搜索</el-button>
            </template>
          </el-input>
        </el-col>
        <el-col :span="6">
          <el-select v-model="query.familyId" placeholder="选择科" clearable @change="handleFamilyChange">
            <el-option
              v-for="item in families"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select v-model="query.genusId" placeholder="选择属" clearable @change="handleSearch">
            <el-option
              v-for="item in filteredGenera"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="handleAdd">添加品种</el-button>
        </el-col>
      </el-row>
    </el-card>
    
    <!-- 品种列表 -->
    <el-row :gutter="20" class="species-grid">
      <el-col :span="6" v-for="item in speciesList" :key="item.id">
        <el-card class="species-card" :body-style="{ padding: '0px' }" @click="goToDetail(item.id)">
          <div class="species-image">
            <el-image
              :src="getDefaultImage()"
              fit="cover"
              style="width: 100%; height: 200px"
            />
          </div>
          <div class="species-info">
            <h3>{{ item.name }}</h3>
            <p class="latin">{{ item.latinName }}</p>
            <div class="tags">
              <el-tag size="small" type="success">{{ item.familyName }}</el-tag>
              <el-tag size="small" type="info">{{ item.genusName }}</el-tag>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="query.page"
        v-model:page-size="query.size"
        :total="total"
        :page-sizes="[12, 24, 36, 48]"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    
    <!-- 添加品种对话框 -->
    <el-dialog v-model="dialogVisible" title="添加品种" width="700px">
      <SpeciesForm ref="formRef" @submit="handleFormSubmit" @cancel="dialogVisible = false" />
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { Species, SpeciesQuery } from '@/api/species'
import { getSpeciesList, addSpecies } from '@/api/species'
import type { Category, Genus } from '@/api/category'
import { getFamilies, getGenera } from '@/api/category'
import SpeciesForm from './SpeciesForm.vue'

const router = useRouter()
const speciesList = ref<Species[]>([])
const families = ref<Category[]>([])
const genera = ref<Genus[]>([])
const total = ref(0)
const dialogVisible = ref(false)
const formRef = ref()

const query = reactive<SpeciesQuery>({
  page: 1,
  size: 12,
  keyword: '',
  familyId: undefined,
  genusId: undefined,
})

// 根据选择的科筛选属
const filteredGenera = computed(() => {
  if (!query.familyId) return genera.value
  return genera.value.filter(g => g.familyId === query.familyId)
})

// 获取默认图片
const getDefaultImage = () => {
  return 'https://via.placeholder.com/300x200?text=Tree'
}

// 加载品种列表
const loadSpecies = async () => {
  try {
    const res = await getSpeciesList(query)
    speciesList.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    ElMessage.error('加载品种列表失败')
  }
}

// 加载分类数据
const loadCategories = async () => {
  try {
    const [familiesRes, generaRes] = await Promise.all([
      getFamilies(),
      getGenera()
    ])
    families.value = familiesRes.data
    genera.value = generaRes.data
  } catch (error) {
    console.error('加载分类失败', error)
  }
}

// 搜索
const handleSearch = () => {
  query.page = 1
  loadSpecies()
}

// 科改变
const handleFamilyChange = () => {
  query.genusId = undefined
  handleSearch()
}

// 分页大小改变
const handleSizeChange = (val: number) => {
  query.size = val
  loadSpecies()
}

// 页码改变
const handleCurrentChange = (val: number) => {
  query.page = val
  loadSpecies()
}

// 添加品种
const handleAdd = () => {
  dialogVisible.value = true
}

// 表单提交
const handleFormSubmit = async (formData: Species) => {
  try {
    await addSpecies(formData)
    ElMessage.success('添加成功')
    dialogVisible.value = false
    loadSpecies()
  } catch (error) {
    ElMessage.error('添加失败')
  }
}

// 跳转到详情
const goToDetail = (id: number) => {
  router.push(`/species/${id}`)
}

onMounted(() => {
  loadSpecies()
  loadCategories()
})
</script>

<style scoped>
.species-list {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.species-grid {
  margin-bottom: 20px;
}

.species-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: transform 0.3s;
}

.species-card:hover {
  transform: translateY(-4px);
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

.tags {
  display: flex;
  gap: 8px;
}

.pagination {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}
</style>
