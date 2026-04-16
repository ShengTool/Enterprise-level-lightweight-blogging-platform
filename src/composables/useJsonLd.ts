export function useJsonLd(type: 'Article' | 'Person' | 'WebSite', data: Record<string, any>) {
  const generateJsonLd = () => {
    let schema: Record<string, any>

    switch (type) {
      case 'Article':
        schema = {
          '@context': 'https://schema.org',
          '@type': 'Article',
          headline: data.title,
          description: data.summary,
          author: {
            '@type': 'Person',
            name: data.authorName,
            url: data.authorUrl
          },
          datePublished: data.publishedAt,
          dateModified: data.updatedAt,
          image: data.image,
          url: data.url,
          publisher: {
            '@type': 'Organization',
            name: 'LiteBlog',
            logo: {
              '@type': 'ImageObject',
              url: '/favicon.svg'
            }
          }
        }
        break

      case 'Person':
        schema = {
          '@context': 'https://schema.org',
          '@type': 'Person',
          name: data.name,
          url: data.url,
          image: data.avatar,
          description: data.bio,
          sameAs: data.socialLinks || []
        }
        break

      case 'WebSite':
        schema = {
          '@context': 'https://schema.org',
          '@type': 'WebSite',
          name: 'LiteBlog',
          url: window.location.origin,
          description: '企业级轻量博客平台，专注于内容创作与分享',
          publisher: {
            '@type': 'Organization',
            name: 'LiteBlog'
          },
          potentialAction: {
            '@type': 'SearchAction',
            target: {
              '@type': 'EntryPoint',
              urlTemplate: `${window.location.origin}/search?q={search_term_string}`
            },
            'query-input': 'required name=search_term_string'
          }
        }
        break

      default:
        schema = {}
    }

    return JSON.stringify(schema)
  }

  const injectJsonLd = () => {
    // 移除旧的 JSON-LD
    const oldScript = document.querySelector('script[data-type="jsonld"]')
    if (oldScript) {
      oldScript.remove()
    }

    // 添加新的 JSON-LD
    const script = document.createElement('script')
    script.type = 'application/ld+json'
    script.setAttribute('data-type', 'jsonld')
    script.textContent = generateJsonLd()
    document.head.appendChild(script)
  }

  return {
    generateJsonLd,
    injectJsonLd
  }
}
