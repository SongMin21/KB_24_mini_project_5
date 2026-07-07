<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../api/axios'

const router = useRouter()

const items = ref([])
const activeTab = ref('all')
const selectedDate = ref('')
const selectedCategory = ref('LEARNED')
const currentPage = ref(1)
const pageSize = 6
const isLoading = ref(false)
const errorMessage = ref('')

const tabs = [
  { key: 'all', label: '전체' },
  { key: 'date', label: '날짜별' },
  { key: 'category', label: '카테고리별' },
  { key: 'like', label: '공감순' },
]

const categories = [
  { value: 'LEARNED', label: '배운 점' },
  { value: 'LACKED', label: '부족한 점' },
  { value: 'GOOD', label: '좋았던 점' },
]

const categoryColorMap = {
  LEARNED: 'var(--color-learned)',
  LACKED: 'var(--color-lacked)',
  GOOD: 'var(--color-good)',
}

const categoryLabelMap = {
  LEARNED: '배운 점',
  LACKED: '부족한 점',
  GOOD: '좋았던 점',
}

const totalPages = computed(() => {
  return Math.max(1, Math.ceil(items.value.length / pageSize))
})

const pagedItems = computed(() => {
  const start = pageSize * (currentPage.value - 1)
  const end = pageSize * currentPage.value

  return items.value.slice(start, end)
})

const pageNumbers = computed(() => {
  return Array.from({ length: totalPages.value }, (_, index) => index + 1)
})

const getErrorMessage = (e) => {
  const data = e.response?.data

  if (typeof data === 'string') {
    return data
  }

  return '회고 목록을 불러오지 못했어요.'
}

const resetPage = () => {
  currentPage.value = 1
}

const fetchAllItems = async () => {
  isLoading.value = true
  errorMessage.value = ''

  try {
    const { data } = await api.get('/thinking')
    items.value = Array.isArray(data) ? data : []
    resetPage()
  } catch (e) {
    errorMessage.value = getErrorMessage(e)
    items.value = []
  } finally {
    isLoading.value = false
  }
}

const fetchDateItems = async () => {
  if (!selectedDate.value) {
    items.value = []
    resetPage()
    return
  }

  isLoading.value = true
  errorMessage.value = ''

  try {
    const { data } = await api.get(`/thinking/date/${selectedDate.value}`)
    items.value = Array.isArray(data) ? data : []
    resetPage()
  } catch (e) {
    errorMessage.value = getErrorMessage(e)
    items.value = []
  } finally {
    isLoading.value = false
  }
}

const fetchCategoryItems = async () => {
  isLoading.value = true
  errorMessage.value = ''

  try {
    const { data } = await api.get('/thinking/category', {
      params: { category: selectedCategory.value },
    })
    items.value = Array.isArray(data) ? data : []
    resetPage()
  } catch (e) {
    errorMessage.value = getErrorMessage(e)
    items.value = []
  } finally {
    isLoading.value = false
  }
}

const fetchLikeItems = async () => {
  isLoading.value = true
  errorMessage.value = ''

  try {
    const { data } = await api.get('/thinking/like')
    const list = Array.isArray(data) ? data : []
    items.value = [...list].sort((a, b) => (b.likeCount || 0) - (a.likeCount || 0))
    resetPage()
  } catch (e) {
    errorMessage.value = getErrorMessage(e)
    items.value = []
  } finally {
    isLoading.value = false
  }
}

const changeTab = async (tab) => {
  activeTab.value = tab
  errorMessage.value = ''

  if (tab === 'all') {
    await fetchAllItems()
    return
  }

  if (tab === 'date') {
    items.value = []
    resetPage()
    return
  }

  if (tab === 'category') {
    await fetchCategoryItems()
    return
  }

  if (tab === 'like') {
    await fetchLikeItems()
  }
}

const changeCategory = async (category) => {
  selectedCategory.value = category
  await fetchCategoryItems()
}

const changePage = (page) => {
  currentPage.value = page
}

const goToDetail = (item) => {
  router.push({ name: 'thinking-detail', params: { id: item.id } })
}

const formatDate = (date) => {
  if (!date) {
    return '-'
  }

  const parsedDate = new Date(date)

  if (Number.isNaN(parsedDate.getTime())) {
    return String(date).slice(0, 10)
  }

  const year = parsedDate.getFullYear()
  const month = String(parsedDate.getMonth() + 1).padStart(2, '0')
  const day = String(parsedDate.getDate()).padStart(2, '0')

  return `${year}-${month}-${day}`
}

onMounted(async () => {
  await fetchAllItems()
})
</script>

<template>
  <main class="thinking-list-page">
    <section class="list-header">
      <h1>우리의 회고를 나눠보세요</h1>
    </section>

    <section class="filter-section" aria-label="회고 목록 필터">
      <div class="tab-bar">
        <button
          v-for="tab in tabs"
          :key="tab.key"
          type="button"
          class="tab-button"
          :class="{ active: activeTab === tab.key }"
          @click="changeTab(tab.key)"
        >
          {{ tab.label }}
        </button>
      </div>

      <div v-if="activeTab === 'date'" class="filter-controls">
        <input v-model="selectedDate" class="date-input" type="date" @change="fetchDateItems" />
        <button class="search-button" type="button" @click="fetchDateItems">조회</button>
      </div>

      <div v-if="activeTab === 'category'" class="filter-controls category-controls">
        <button
          v-for="category in categories"
          :key="category.value"
          type="button"
          class="category-filter-button"
          :class="{ active: selectedCategory === category.value }"
          @click="changeCategory(category.value)"
        >
          {{ category.label }}
        </button>
      </div>
    </section>

    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    <p v-if="isLoading" class="loading-message">회고를 불러오는 중입니다.</p>

    <section v-else-if="items.length === 0" class="empty-state">
      <p>아직 작성된 회고가 없어요. 첫 번째 회고를 남겨보세요!</p>
    </section>

    <section v-else class="card-grid" aria-label="회고 게시글 목록">
      <article
        v-for="item in pagedItems"
        :key="item.id"
        class="thinking-card"
        tabindex="0"
        @click="goToDetail(item)"
        @keyup.enter="goToDetail(item)"
      >
        <div class="card-top">
          <span
            class="category-badge"
            :style="{ backgroundColor: categoryColorMap[item.category] || 'var(--color-primary)' }"
          >
            {{ categoryLabelMap[item.category] || item.category }}
          </span>
          <h2 class="card-title">{{ item.title }}</h2>
          <span class="created-date">{{ formatDate(item.createdAt || item.createDate) }}</span>
        </div>

        <div class="card-meta">
          <span>♥ {{ item.likeCount || 0 }}</span>
        </div>
      </article>
    </section>

    <nav v-if="items.length > pageSize" class="pagination" aria-label="회고 목록 페이지">
      <button
        v-for="page in pageNumbers"
        :key="page"
        type="button"
        class="page-button"
        :class="{ active: currentPage === page }"
        @click="changePage(page)"
      >
        {{ page }}
      </button>
    </nav>
  </main>
</template>

<style scoped>
.thinking-list-page {
  width: min(1080px, calc(100% - 32px));
  margin: 0 auto;
  padding: 56px 0 72px;
  color: var(--color-text-primary);
}

.list-header {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 32px;
}

.list-header h1 {
  margin: 0;
  font-size: 32px;
  font-weight: 800;
  line-height: 1.3;
  text-align: center;
}

.filter-section {
  display: flex;
  flex-direction: column;
  gap: 18px;
  margin-bottom: 28px;
}

.tab-bar {
  display: flex;
  justify-content: center;
  gap: 10px;
  flex-wrap: wrap;
}

.tab-button,
.category-filter-button,
.search-button,
.page-button {
  border: 1px solid var(--color-border);
  border-radius: var(--radius-input);
  background: var(--color-card);
  color: var(--color-text-secondary);
  font-size: 14px;
  font-weight: 700;
  line-height: 1;
  min-height: 42px;
  padding: 0 18px;
  cursor: pointer;
  transition:
    background-color 0.2s ease,
    border-color 0.2s ease,
    color 0.2s ease,
    transform 0.2s ease;
}

.tab-button:hover,
.category-filter-button:hover,
.search-button:hover,
.page-button:hover {
  border-color: var(--color-primary);
  color: var(--color-text-primary);
}

.tab-button.active,
.category-filter-button.active,
.page-button.active {
  border-color: var(--color-primary);
  background: var(--color-primary);
  color: var(--color-on-primary);
}

.filter-controls {
  display: flex;
  justify-content: center;
  gap: 10px;
  flex-wrap: wrap;
}

.date-input {
  min-height: 42px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-input);
  color: var(--color-text-primary);
  font-size: 14px;
  padding: 0 14px;
}

.search-button {
  background: var(--color-text-primary);
  border-color: var(--color-text-primary);
  color: var(--color-on-accent);
}

.search-button:hover {
  background: var(--color-text-primary);
  border-color: var(--color-text-primary);
  color: var(--color-on-accent);
  filter: brightness(0.9);
}

.error-message,
.loading-message {
  margin: 28px 0;
  text-align: center;
  font-size: 15px;
  font-weight: 700;
}

.error-message {
  color: var(--color-danger);
}

.loading-message {
  color: var(--color-text-secondary);
}

.card-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 14px;
}

.thinking-card {
  min-height: 92px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-input);
  background: var(--color-card);
  padding: 20px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.06);
  cursor: pointer;
  transition:
    box-shadow 0.2s ease,
    transform 0.2s ease,
    border-color 0.2s ease;
}

.thinking-card:hover,
.thinking-card:focus {
  border-color: var(--color-primary);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.14);
  outline: none;
  transform: translateY(-3px);
}

.card-top {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 12px;
}

.category-badge {
  display: inline-flex;
  align-items: center;
  min-height: 28px;
  border-radius: var(--radius-pill);
  color: var(--color-on-accent);
  font-size: 12px;
  font-weight: 800;
  padding: 0 10px;
  white-space: nowrap;
}

.created-date {
  margin-left: auto;
  color: var(--color-text-secondary);
  font-size: 13px;
  white-space: nowrap;
}

.card-title {
  flex: 1;
  margin: 0;
  color: var(--color-text-primary);
  font-size: 16px;
  font-weight: 800;
  line-height: 1.45;
  word-break: keep-all;
  overflow-wrap: anywhere;
}

.card-meta {
  display: flex;
  gap: 16px;
  color: var(--color-text-secondary);
  font-size: 14px;
  font-weight: 700;
}

.empty-state {
  min-height: 240px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 18px;
  border: 1px dashed var(--color-border);
  border-radius: var(--radius-input);
  background: var(--color-background);
  text-align: center;
  padding: 36px 18px;
}

.empty-state p {
  margin: 0;
  color: var(--color-text-secondary);
  font-size: 16px;
  font-weight: 700;
}

.pagination {
  display: flex;
  justify-content: center;
  gap: 8px;
  flex-wrap: wrap;
  margin-top: 30px;
}

.page-button {
  min-width: 42px;
  padding: 0 12px;
}

@media (max-width: 860px) {
  .list-header {
    flex-direction: column;
    gap: 18px;
  }
}

@media (max-width: 560px) {
  .thinking-list-page {
    width: min(100% - 24px, 1080px);
    padding-top: 36px;
  }

  .list-header h1 {
    font-size: 26px;
  }

  .tab-button,
  .category-filter-button {
    flex: 1 1 calc(50% - 10px);
  }

}
</style>
