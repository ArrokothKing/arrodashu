import request from './request'

export interface Category {
  id: number
  name: string
  latinName: string
  description?: string
}

export interface Genus extends Category {
  familyId: number
}

// 获取所有科
export function getFamilies() {
  return request.get<any, { data: Category[] }>('/category/families')
}

// 获取所有属
export function getGenera() {
  return request.get<any, { data: Genus[] }>('/category/genera')
}

// 根据科获取属
export function getGeneraByFamily(familyId: number) {
  return request.get<any, { data: Genus[] }>(`/category/family/${familyId}/genera`)
}
