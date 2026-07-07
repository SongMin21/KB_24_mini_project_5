<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const props = defineProps({
  status: {
    type: [Number, String],
    default: 500,
  },
  message: {
    type: String,
    default: '',
  },
})

const router = useRouter()

const STATUS_CONTENT = {
  404: {
    emoji: '🔍',
    title: '페이지를 찾을 수 없어요',
    description: '요청하신 페이지가 존재하지 않거나 삭제되었을 수 있습니다.',
  },
  500: {
    emoji: '⚠️',
    title: '일시적인 오류가 발생했어요',
    description: '잠시 후 다시 시도해주세요.',
  },
}

const FALLBACK_CONTENT = {
  emoji: '❗',
  title: '문제가 발생했어요',
  description: '알 수 없는 오류가 발생했습니다.',
}

const statusCode = computed(() => Number(props.status) || 500)

const content = computed(() => STATUS_CONTENT[statusCode.value] || FALLBACK_CONTENT)

const description = computed(() => props.message || content.value.description)

const goToList = () => {
  router.push({ name: 'thinking-list' })
}
</script>

<template>
  <main class="error-page">
    <section class="error-card">
      <p class="error-emoji">{{ content.emoji }}</p>
      <p class="error-status">{{ statusCode }}</p>
      <h1 class="error-title">{{ content.title }}</h1>
      <p class="error-description">{{ description }}</p>
      <button class="back-button" type="button" @click="goToList">목록으로 돌아가기</button>
    </section>
  </main>
</template>

<style scoped>
.error-page {
  width: min(1080px, calc(100% - 32px));
  margin: 0 auto;
  padding: 96px 0;
  display: flex;
  justify-content: center;
}

.error-card {
  width: min(480px, 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  border: 1px solid #e5eaf0;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 6px 18px rgba(20, 37, 63, 0.06);
  padding: 48px 32px;
  text-align: center;
}

.error-emoji {
  margin: 0;
  font-size: 48px;
  line-height: 1;
}

.error-status {
  margin: 0;
  color: #c7d0dc;
  font-size: 15px;
  font-weight: 800;
  letter-spacing: 0.05em;
}

.error-title {
  margin: 0;
  color: #20242a;
  font-size: 22px;
  font-weight: 800;
}

.error-description {
  margin: 0;
  color: #6b7280;
  font-size: 15px;
  line-height: 1.6;
}

.back-button {
  margin-top: 12px;
  border: none;
  border-radius: 999px;
  padding: 12px 28px;
  background: #4a90d9;
  color: #fff;
  font-size: 14px;
  font-weight: 700;
  transition: background-color 0.15s ease;
}

.back-button:hover {
  background-color: #3d7ab8;
}
</style>
