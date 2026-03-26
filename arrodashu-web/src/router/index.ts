import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/test',
      name: 'test',
      component: () => import('@/views/TestView.vue'),
    },
    {
      path: '/',
      name: 'home',
      component: () => import('@/views/HomeView.vue'),
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue'),
      meta: { guest: true },
    },
    {
      path: '/species',
      name: 'species',
      component: () => import('@/views/species/SpeciesList.vue'),
    },
    {
      path: '/species/:id',
      name: 'species-detail',
      component: () => import('@/views/species/SpeciesDetail.vue'),
    },
    {
      path: '/knowledge',
      name: 'knowledge',
      component: () => import('@/views/knowledge/KnowledgeView.vue'),
    },
    {
      path: '/user',
      name: 'user',
      component: () => import('@/views/user/UserCenter.vue'),
      meta: { requiresAuth: true },
    },
  ],
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  if (to.meta.requiresAuth && !userStore.isLoggedIn()) {
    next('/login')
  } else if (to.meta.guest && userStore.isLoggedIn()) {
    next('/')
  } else {
    next()
  }
})

export default router
