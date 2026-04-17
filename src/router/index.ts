import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import { useUserStore } from '../stores/user'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/article/:id',
      name: 'article',
      component: () => import('../views/ArticleDetail.vue')
    },
    {
      path: '/tag/:name',
      name: 'tag',
      component: () => import('../views/TagList.vue')
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/About.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/Login.vue')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/Register.vue')
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('../views/Profile.vue')
    },
    {
      path: '/write',
      name: 'write',
      component: () => import('../views/CreateArticle.vue')
    },
    {
      path: '/write/:id',
      name: 'write-edit',
      component: () => import('../views/CreateArticle.vue')
    },
    {
      path: '/user/:id',
      name: 'user',
      component: () => import('../views/UserProfile.vue')
    },
    {
      path: '/search',
      name: 'search',
      component: () => import('../views/Search.vue')
    },
    {
      path: '/admin',
      name: 'admin',
      component: () => import('../views/admin/Admin.vue'),
      children: [
        {
          path: 'articles',
          name: 'admin-articles',
          component: () => import('../views/admin/ArticleManage.vue')
        },
        {
          path: 'articles/edit/:id?',
          name: 'admin-article-edit',
          component: () => import('../views/admin/ArticleEdit.vue')
        },
        {
          path: 'tags',
          name: 'admin-tags',
          component: () => import('../views/admin/TagManage.vue')
        },
        {
          path: 'comments',
          name: 'admin-comments',
          component: () => import('../views/admin/CommentManage.vue')
        },
        {
          path: 'users',
          name: 'admin-users',
          component: () => import('../views/admin/UserManage.vue')
        },
        {
          path: 'settings',
          name: 'admin-settings',
          component: () => import('../views/admin/Setting.vue')
        }
      ]
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: () => import('../views/NotFound.vue')
    }
  ]
})

// 路由守卫：非管理员不能访问 /admin/** 页面
router.beforeEach(async (to) => {
  if (!to.path.startsWith('/admin')) return true

  const userStore = useUserStore()

  // 如果还没加载过用户信息，先拉取一次
  if (userStore.token && !userStore.user) {
    await userStore.getProfile()
  }

  if (!userStore.user?.isAdmin) {
    return '/'
  }
  return true
})

export default router