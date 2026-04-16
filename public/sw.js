const CACHE_NAME = 'liteblog-v1'
const urlsToCache = [
  '/',
  '/index.html'
]

// 安装 Service Worker
self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then((cache) => {
        return cache.addAll(urlsToCache)
      })
  )
})

// 激活 Service Worker
self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((cacheNames) => {
      return Promise.all(
        cacheNames.map((cacheName) => {
          if (cacheName !== CACHE_NAME) {
            return caches.delete(cacheName)
          }
        })
      )
    })
  )
})

// 拦截请求
self.addEventListener('fetch', (event) => {
  // 只缓存 GET 请求
  if (event.request.method !== 'GET') return

  event.respondWith(
    caches.match(event.request)
      .then((response) => {
        // 如果有缓存，返回缓存
        if (response) {
          return response
        }

        // 否则请求网络
        return fetch(event.request).then((response) => {
          // 检查是否是有效的响应
          if (!response || response.status !== 200 || response.type !== 'basic') {
            return response
          }

          // 缓存新的响应
          const responseToCache = response.clone()
          caches.open(CACHE_NAME)
            .then((cache) => {
              cache.put(event.request, responseToCache)
            })

          return response
        })
      })
      .catch(() => {
        // 如果网络和缓存都失败，返回离线页面
        return caches.match('/')
      })
  )
})

// 背景同步
self.addEventListener('sync', (event) => {
  if (event.tag === 'sync-articles') {
    event.waitUntil(syncArticles())
  }
})

async function syncArticles() {
  // 实现离线数据同步逻辑
  console.log('Background sync triggered')
}
