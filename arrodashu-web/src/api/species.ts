import request from './request'

export interface Species {
  id: number
  name: string
  latinName: string
  alias?: string
  familyId: number
  genusId: number
  familyName?: string
  genusName?: string
  treeType?: number
  heightMin?: number
  heightMax?: number
  lightPreference?: string
  waterPreference?: string
  soilPreference?: string
  hardinessZone?: string
  growthRate?: string
  landscapeUse?: string
  description?: string
  origin?: string
  status: number
  createTime?: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}

export interface SpeciesQuery {
  page?: number
  size?: number
  keyword?: string
  familyId?: number
  genusId?: number
}

// 获取品种列表
export function getSpeciesList(params: SpeciesQuery) {
  return request.get<any, { data: PageResult<Species> }>('/species', { params })
}

// 获取品种详情
export function getSpeciesDetail(id: number) {
  return request.get<any, { data: Species }>(`/species/${id}`)
}

// 添加品种
export function addSpecies(data: Species) {
  return request.post('/species', data)
}

// 更新品种
export function updateSpecies(id: number, data: Species) {
  return request.put(`/species/${id}`, data)
}

// 删除品种
export function deleteSpecies(id: number) {
  return request.delete(`/species/${id}`)
}
