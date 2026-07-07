<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import api from '@/api/axios';

const router = useRouter();

const category = ref('');
const title = ref('');
const content = ref('');
const password = ref('');
const loading = ref(false);
const errorMessage = ref('');

// 모든 항목이 비어있지 않은지 검증
const isFormValid = computed(() => {
  return (
    category.value &&
    title.value.trim() &&
    content.value.trim() &&
    password.value.trim()
  );
});

const selectCategory = (val) => {
  category.value = val;
};

const goToList = () => {
  router.push({ name: 'thinking-list' });
};

const handleSubmit = async () => {
  if (!isFormValid.value) return;

  loading.value = true;
  errorMessage.value = '';

  try {
    const { data } = await api.post('/thinking', {
      category: category.value,
      title: title.value,
      content: content.value,
      password: password.value,
    });

    router.push({
      name: 'thinking-detail',
      params: { id: data.id },
    });
  } catch (e) {
    const msg = e.response?.data ?? '오류가 발생했습니다.';
    // 백엔드 에러 응답은 plain text 문자열
    errorMessage.value = msg;
    console.error(msg);
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="thinking-create-container">
    <!-- 뒤로가기 링크 -->
    <div class="back-link-wrapper">
      <button @click="goToList" class="back-link">← 목록으로</button>
    </div>

    <!-- 페이지 타이틀 -->
    <h2 class="page-title">회고 작성하기</h2>

    <!-- 작성 폼 카드 -->
    <form @submit.prevent="handleSubmit" class="form-card">
      <!-- 에러 메시지 표시 -->
      <div v-if="errorMessage" class="error-banner">
        {{ errorMessage }}
      </div>

      <!-- 카테고리 선택 -->
      <div class="form-group">
        <label class="form-label"
          >카테고리 선택 <span class="required">*</span></label
        >
        <div class="category-buttons">
          <button
            type="button"
            class="category-btn learned"
            :class="{ active: category === 'LEARNED' }"
            @click="selectCategory('LEARNED')"
          >
            <span class="dot">🟢</span> 배운 것
          </button>
          <button
            type="button"
            class="category-btn lacked"
            :class="{ active: category === 'LACKED' }"
            @click="selectCategory('LACKED')"
          >
            <span class="dot">🟠</span> 부족한 것
          </button>
          <button
            type="button"
            class="category-btn good"
            :class="{ active: category === 'GOOD' }"
            @click="selectCategory('GOOD')"
          >
            <span class="dot">🩷</span> 좋았던 것
          </button>
        </div>
      </div>

      <!-- 제목 입력 -->
      <div class="form-group">
        <label for="title" class="form-label"
          >제목 <span class="required">*</span></label
        >
        <input
          id="title"
          v-model="title"
          type="text"
          placeholder="제목을 입력하세요"
          class="form-input"
          required
        />
      </div>

      <!-- 내용 입력 -->
      <div class="form-group">
        <label for="content" class="form-label"
          >내용 <span class="required">*</span></label
        >
        <textarea
          id="content"
          v-model="content"
          placeholder="회고 내용을 작성하세요"
          class="form-textarea"
          required
        ></textarea>
      </div>

      <!-- 비밀번호 입력 -->
      <div class="form-group half-width">
        <label for="password" class="form-label"
          >비밀번호 (수정/삭제 시 필요) <span class="required">*</span></label
        >
        <input
          id="password"
          v-model="password"
          type="password"
          placeholder="수정/삭제 시 필요합니다"
          class="form-input"
          required
        />
      </div>

      <!-- 하단 버튼 영역 -->
      <div class="form-actions">
        <button type="button" @click="goToList" class="btn-cancel">취소</button>
        <button
          type="submit"
          class="btn-submit"
          :disabled="!isFormValid || loading"
        >
          {{ loading ? '작성 중...' : '작성하기' }}
        </button>
      </div>
    </form>
  </div>
</template>

<style scoped>
.thinking-create-container {
  padding: 20px 0 60px;
}

.back-link-wrapper {
  margin-bottom: 20px;
}

.back-link {
  background: none;
  border: none;
  color: var(--color-text-secondary);
  font-size: 16px;
  font-weight: 500;
  padding: 0;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  cursor: pointer;
  transition: color 0.2s ease;
}

.back-link:hover {
  color: var(--color-primary);
}

.page-title {
  font-size: 22px;
  font-weight: bold;
  color: var(--color-text-primary);
  margin-bottom: 24px;
}

.form-card {
  background: var(--color-card);
  border-radius: var(--radius-base);
  padding: 32px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
}

.error-banner {
  background-color: #ffebee;
  color: #c62828;
  padding: 12px 16px;
  border-radius: 8px;
  margin-bottom: 24px;
  font-size: 14px;
  border-left: 4px solid #e53935;
}

.form-group {
  margin-bottom: 24px;
}

.form-group.half-width {
  width: 50%;
}

@media (max-width: 768px) {
  .form-group.half-width {
    width: 100%;
  }
}

.form-label {
  display: block;
  font-size: 14px;
  font-weight: bold;
  color: var(--color-text-primary);
  margin-bottom: 8px;
}

.required {
  color: #e53935;
  margin-left: 2px;
}

.category-buttons {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.category-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
  background: #ffffff;
  border: 1px solid var(--color-border);
  color: var(--color-text-secondary);
  transition: all 0.2s ease;
}

.category-btn .dot {
  font-size: 12px;
}

/* Category hover effects */
.category-btn.learned:hover {
  border-color: var(--color-learned);
  color: var(--color-learned);
  background: rgba(76, 175, 80, 0.05);
}

.category-btn.lacked:hover {
  border-color: var(--color-lacked);
  color: var(--color-lacked);
  background: rgba(255, 152, 0, 0.05);
}

.category-btn.good:hover {
  border-color: var(--color-good);
  color: var(--color-good);
  background: rgba(233, 30, 99, 0.05);
}

/* Active Category Styles */
.category-btn.learned.active {
  background: var(--color-learned);
  border-color: var(--color-learned);
  color: #ffffff;
}

.category-btn.lacked.active {
  background: var(--color-lacked);
  border-color: var(--color-lacked);
  color: #ffffff;
}

.category-btn.good.active {
  background: var(--color-good);
  border-color: var(--color-good);
  color: #ffffff;
}

/* Input Styles */
.form-input {
  width: 100%;
  padding: 12px 16px;
  border-radius: 8px;
  border: 1px solid var(--color-border);
  font-size: 15px;
  color: var(--color-text-primary);
  background-color: #ffffff;
  outline: none;
  transition: border-color 0.2s ease;
}

.form-input:focus {
  border-color: var(--color-primary);
}

.form-textarea {
  width: 100%;
  min-height: 200px;
  padding: 16px;
  border-radius: 8px;
  border: 1px solid var(--color-border);
  font-size: 15px;
  color: var(--color-text-primary);
  background-color: #ffffff;
  outline: none;
  resize: vertical;
  line-height: 1.6;
  font-family: inherit;
  transition: border-color 0.2s ease;
}

.form-textarea:focus {
  border-color: var(--color-primary);
}

/* Actions Section */
.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 32px;
  border-top: 1px solid var(--color-border);
  padding-top: 24px;
}

.btn-cancel {
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 500;
  background: #ffffff;
  border: 1px solid var(--color-border);
  color: var(--color-text-secondary);
  transition: all 0.2s ease;
}

.btn-cancel:hover {
  background: var(--color-background);
  border-color: #cccccc;
  color: var(--color-text-primary);
}

.btn-submit {
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 500;
  background: var(--color-primary);
  border: 1px solid var(--color-primary);
  color: #ffffff;
  transition: all 0.2s ease;
}

.btn-submit:hover:not(:disabled) {
  background: #357ae8;
  border-color: #357ae8;
  box-shadow: 0 2px 8px rgba(74, 144, 217, 0.3);
}

.btn-submit:disabled {
  background: #b0bec5;
  border-color: #b0bec5;
  cursor: not-allowed;
  color: #ffffff;
}
</style>
// TODO: design.md 기반으로 팀원이 작업 예정
// POST /api/thinking
</script>

<template>
  <div>
    <h1>작성 Page</h1>
    <!-- TODO -->
  </div>
</template>
