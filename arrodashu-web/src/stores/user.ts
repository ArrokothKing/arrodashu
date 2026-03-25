import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { UserInfo, LoginParams } from '@/api/auth'
import { login as loginApi, getUserInfo as getUserInfoApi, logout as logoutApi } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref<UserInfo | null>(null)

  // 登录
  async function login(params: LoginParams) {
    const res = await loginApi(params)
    token.value = res.data.token
    userInfo.value = res.data.user
    localStorage.setItem('token', res.data.token)
    return res.data
  }

  // 获取用户信息
  async function getUserInfo() {
    try {
      const res = await getUserInfoApi()
      userInfo.value = res.data
      return res.data
    } catch (error) {
      token.value = ''
      userInfo.value = null
      localStorage.removeItem('token')
      throw error
    }
  }

  // 登出
  async function logout() {
    try {
      await logoutApi()
    } finally {
      token.value = ''
      userInfo.value = null
      localStorage.removeItem('token')
    }
  }

  // 判断是否已登录
  function isLoggedIn() {
    return !!token.value
  }

  return {
    token,
    userInfo,
    login,
    getUserInfo,
    logout,
    isLoggedIn,
  }
})
