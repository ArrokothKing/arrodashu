import request from './request'

export interface LoginParams {
  username: string
  password: string
}

export interface LoginResult {
  token: string
  user: {
    id: number
    username: string
    nickname: string
    avatar: string
  }
}

export interface UserInfo {
  id: number
  username: string
  email: string
  phone: string
  nickname: string
  avatar: string
  status: number
}

// 登录
export function login(data: LoginParams) {
  return request.post<any, { data: LoginResult }>('/auth/login', data)
}

// 获取用户信息
export function getUserInfo() {
  return request.get<any, { data: UserInfo }>('/auth/info')
}

// 登出
export function logout() {
  return request.post('/auth/logout')
}
