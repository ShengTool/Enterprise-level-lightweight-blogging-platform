import { onMounted } from 'vue'

export function useSeoMeta(options: {
  title?: string
  description?: string
  keywords?: string
  author?: string
}) {
  const defaultTitle = 'LiteBlog - 企业级轻量博客平台'
  const defaultDescription = '企业级轻量博客平台，专注于内容创作与分享'

  const updateMeta = () => {
    // Title
    document.title = options.title ? `${options.title} - LiteBlog` : defaultTitle

    // Description
    let metaDesc = document.querySelector('meta[name="description"]')
    if (!metaDesc) {
      metaDesc = document.createElement('meta')
      metaDesc.setAttribute('name', 'description')
      document.head.appendChild(metaDesc)
    }
    metaDesc.setAttribute('content', options.description || defaultDescription)

    // Keywords
    if (options.keywords) {
      let metaKeywords = document.querySelector('meta[name="keywords"]')
      if (!metaKeywords) {
        metaKeywords = document.createElement('meta')
        metaKeywords.setAttribute('name', 'keywords')
        document.head.appendChild(metaKeywords)
      }
      metaKeywords.setAttribute('content', options.keywords)
    }

    // Open Graph
    const ogTitle = document.querySelector('meta[property="og:title"]') || createMeta('property', 'og:title')
    ogTitle.setAttribute('content', options.title || defaultTitle)

    const ogDesc = document.querySelector('meta[property="og:description"]') || createMeta('property', 'og:description')
    ogDesc.setAttribute('content', options.description || defaultDescription)

    // Twitter
    const twitterTitle = document.querySelector('meta[name="twitter:title"]') || createMeta('name', 'twitter:title')
    twitterTitle.setAttribute('content', options.title || defaultTitle)

    const twitterDesc = document.querySelector('meta[name="twitter:description"]') || createMeta('name', 'twitter:description')
    twitterDesc.setAttribute('content', options.description || defaultDescription)
  }

  const createMeta = (attr: string, name: string) => {
    const meta = document.createElement('meta')
    meta.setAttribute(attr, name)
    document.head.appendChild(meta)
    return meta
  }

  onMounted(updateMeta)

  return {
    updateMeta
  }
}
