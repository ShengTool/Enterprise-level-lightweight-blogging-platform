<template>
  <Teleport to="head">
    <title>{{ title }} - LiteBlog</title>
    <meta name="description" :content="description" />
    <meta property="og:title" :content="`${title} - LiteBlog`" />
    <meta property="og:description" :content="description" />
    <meta property="og:type" content="article" />
    <meta name="twitter:card" content="summary" />
    <meta name="twitter:title" :content="`${title} - LiteBlog`" />
    <meta name="twitter:description" :content="description" />
  </Teleport>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  title: string
  description?: string
}>()

const description = computed(() => 
  props.description || '企业级轻量博客平台，专注于内容创作与分享'
)
</script>
