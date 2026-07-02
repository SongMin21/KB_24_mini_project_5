<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goToList = () => {
  router.push({ name: 'thinking-list' })
}

const goToCreate = () => {
  router.push({ name: 'thinking-create' })
}
</script>

<template>
  <header class="header">
    <div class="header-inner container">
      <button class="logo" type="button" @click="goToList">회고 게시판</button>
      <button class="write-btn" type="button" @click="goToCreate">글쓰기</button>
    </div>
  </header>
</template>

<style scoped>
.header {
  border-bottom: 1px solid var(--color-border);
  background: var(--color-card);
}

.header-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 16px;
  padding-bottom: 16px;
}

.logo {
  border: none;
  background: none;
  padding: 0;
  font-size: 18px;
  font-weight: 700;
  color: var(--color-text-primary);
  transition: opacity 0.15s ease;
}

.logo:hover {
  opacity: 0.7;
}

.write-btn {
  border: none;
  border-radius: 999px;
  padding: 8px 18px;
  background: var(--color-primary);
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  transition: background-color 0.15s ease;
}

.write-btn:hover {
  background-color: #3d7ab8;
}
</style>
