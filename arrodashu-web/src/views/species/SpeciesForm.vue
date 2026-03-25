<template>
  <el-form
    ref="formRef"
    :model="form"
    :rules="rules"
    label-width="100px"
  >
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="中文名" prop="name">
          <el-input v-model="form.name" placeholder="请输入中文名" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="拉丁名" prop="latinName">
          <el-input v-model="form.latinName" placeholder="请输入拉丁名" />
        </el-form-item>
      </el-col>
    </el-row>
    
    <el-form-item label="别名">
      <el-input v-model="form.alias" placeholder="多个别名用逗号分隔" />
    </el-form-item>
    
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="科" prop="familyId">
          <el-select v-model="form.familyId" placeholder="选择科" style="width: 100%" @change="handleFamilyChange">
            <el-option
              v-for="item in families"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="属" prop="genusId">
          <el-select v-model="form.genusId" placeholder="选择属" style="width: 100%">
            <el-option
              v-for="item in filteredGenera"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
    
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="树型">
          <el-select v-model="form.treeType" placeholder="选择树型" style="width: 100%">
            <el-option label="乔木" :value="1" />
            <el-option label="灌木" :value="2" />
            <el-option label="藤本" :value="3" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="生长速度">
          <el-select v-model="form.growthRate" placeholder="选择生长速度" style="width: 100%">
            <el-option label="慢" value="慢" />
            <el-option label="中" value="中" />
            <el-option label="快" value="快" />
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
    
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="最小高度(cm)">
          <el-input-number v-model="form.heightMin" :min="0" style="width: 100%" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="最大高度(cm)">
          <el-input-number v-model="form.heightMax" :min="0" style="width: 100%" />
        </el-form-item>
      </el-col>
    </el-row>
    
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="光照需求">
          <el-select v-model="form.lightPreference" placeholder="选择光照需求" style="width: 100%">
            <el-option label="阳性" value="阳性" />
            <el-option label="半阴" value="半阴" />
            <el-option label="阴性" value="阴性" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="水分需求">
          <el-select v-model="form.waterPreference" placeholder="选择水分需求" style="width: 100%">
            <el-option label="耐旱" value="耐旱" />
            <el-option label="适中" value="适中" />
            <el-option label="喜湿" value="喜湿" />
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
    
    <el-form-item label="土壤要求">
      <el-input v-model="form.soilPreference" placeholder="请输入土壤要求" />
    </el-form-item>
    
    <el-form-item label="原产地">
      <el-input v-model="form.origin" placeholder="请输入原产地" />
    </el-form-item>
    
    <el-form-item label="园林用途">
      <el-input v-model="form.landscapeUse" type="textarea" :rows="2" placeholder="请输入园林用途" />
    </el-form-item>
    
    <el-form-item label="详细描述">
      <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入详细描述" />
    </el-form-item>
    
    <el-form-item>
      <el-button type="primary" @click="handleSubmit">提交</el-button>
      <el-button @click="handleCancel">取消</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import type { Species } from '@/api/species'
import type { Category, Genus } from '@/api/category'
import { getFamilies, getGenera } from '@/api/category'

const emit = defineEmits<{
  submit: [data: Species]
  cancel: []
}>()

const formRef = ref<FormInstance>()
const families = ref<Category[]>([])
const genera = ref<Genus[]>([])

const form = reactive<Species>({
  id: 0,
  name: '',
  latinName: '',
  alias: '',
  familyId: 0,
  genusId: 0,
  treeType: undefined,
  heightMin: undefined,
  heightMax: undefined,
  lightPreference: '',
  waterPreference: '',
  soilPreference: '',
  growthRate: '',
  origin: '',
  landscapeUse: '',
  description: '',
  status: 1
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入中文名', trigger: 'blur' }],
  latinName: [{ required: true, message: '请输入拉丁名', trigger: 'blur' }],
  familyId: [{ required: true, message: '请选择科', trigger: 'change' }],
  genusId: [{ required: true, message: '请选择属', trigger: 'change' }]
}

// 根据选择的科筛选属
const filteredGenera = computed(() => {
  if (!form.familyId) return []
  return genera.value.filter(g => g.familyId === form.familyId)
})

// 科改变
const handleFamilyChange = () => {
  form.genusId = 0
}

// 提交
const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate((valid) => {
    if (valid) {
      emit('submit', { ...form })
    }
  })
}

// 取消
const handleCancel = () => {
  emit('cancel')
}

// 加载分类
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

onMounted(() => {
  loadCategories()
})
</script>
