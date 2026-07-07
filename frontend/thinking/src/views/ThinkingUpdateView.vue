<template>
  <div class="thinking-update-container">
    <h1>글 수정하기</h1>

    <div v-if="loading" class="loading-state">
      데이터를 불러오는 중입니다...
    </div>

    <form v-else @submit.prevent="handleSubmit" class="update-form">
      <div class="form-group">
        <label class="form-label">카테고리 *</label>
        <div class="category-buttons">
          <button
            type="button"
            :class="['category-btn', 'learned', { active: category === 'LEARNED' }]"
            @click="setCategory('LEARNED')"
          >
            배운 것 (LEARNED)
          </button>
          <button
            type="button"
            :class="['category-btn', 'lacked', { active: category === 'LACKED' }]"
            @click="setCategory('LACKED')"
          >
            부족한 것 (LACKED)
          </button>
          <button
            type="button"
            :class="['category-btn', 'good', { active: category === 'GOOD' }]"
            @click="setCategory('GOOD')"
          >
            좋았던 것 (GOOD)
          </button>
        </div>
      </div>

      <div class="form-group">
        <label for="title" class="form-label">제목</label>
        <input
          id="title"
          v-model="title"
          type="text"
          placeholder="제목을 입력하세요"
          required
          class="form-input"
        />
      </div>

      <div class="form-group">
        <label for="content" class="form-label">내용</label>
        <textarea
          id="content"
          v-model="content"
          placeholder="내용을 입력하세요"
          required
          rows="10"
          class="form-textarea"
        ></textarea>
      </div>

      <div class="form-group">
        <label for="password" class="form-label">비밀번호 확인 *</label>
        <input
          id="password"
          v-model="password"
          type="password"
          placeholder="글 작성 시 설정한 비밀번호를 입력하세요"
          required
          class="form-input"
        />
        <p v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </p>
      </div>

      <div class="button-group">
        <button 
          type="button" 
          @click="handleCancel" 
          class="btn btn-cancel"
        >
          취소
        </button>
        <button 
          type="submit" 
          :disabled="isSubmitDisabled" 
          class="btn btn-submit"
        >
          수정 완료
        </button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '@/api/axios'

// 라우터 및 경로 파라미터 (id) 취득
const route = useRoute()
const router = useRouter()
const thinkingId = route.params.id

// 상태 정의
const category = ref('')
const title = ref('')
const content = ref('')
const password = ref('') // 비밀번호는 초기 로딩 시 비워둠

const loading = ref(false)
const errorMessage = ref('')

// 제출 버튼 비활성화 조건 (카테고리 미선택 시 또는 비밀번호가 없을 때 막음)
const isSubmitDisabled = computed(() => {
  return !category.value || !title.value.trim() || !content.value.trim() || !password.value
})

// 카테고리 변경 핸들러
const setCategory = (selectedCategory) => {
  category.value = selectedCategory
}

// 1. 초기 로딩: 기존 데이터 가져오기 (비밀번호는 응답에 없으므로 프리필하지 않음)
const fetchThinkingDetail = async () => {
  loading.value = true
  errorMessage.value = ''
  try {
    const { data } = await api.get(`/thinking/${thinkingId}`)
    category.value = data.category
    title.value = data.title
    content.value = data.content
  } catch (e) {
    console.error(e)
    const msg = e.response?.data ?? '데이터를 불러오는 중 오류가 발생했습니다.'
    alert(msg)
    // 404 등 오류 발생 시 목록으로 리다이렉트
    router.push({ name: 'thinking-list' })
  } finally {
    loading.value = false
  }
}

// 2. 수정 제출 버튼 클릭 핸들러
const handleSubmit = async () => {
  errorMessage.value = ''
  
  try {
    // PUT /api/thinking 요청 body 조립
    const requestBody = {
      id: thinkingId,
      category: category.value,
      title: title.value,
      content: content.value,
      password: password.value
    }

    const response = await api.put('/thinking', requestBody)

    // 백엔드 제약사항 2번: 응답 바디가 plain text인 "success" 인지 확인
    if (response.data === 'success') {
      // 성공 시 상세 페이지로 이동
      router.push({ name: 'thinking-detail', params: { id: thinkingId } })
    } else {
      // 응답 바디가 "fail"인 경우 처리
      errorMessage.value = '비밀번호가 일치하지 않거나 수정에 실패했습니다.'
    }
  } catch (e) {
    console.error(e)
    // 백엔드 제약사항 1번: 에러 응답이 plain text 문자열로 옴
    const status = e.response?.status
    const textMessage = e.response?.data

    if (status === 401) {
      errorMessage.value = '비밀번호가 일치하지 않습니다.'
    } else if (status === 404) {
      errorMessage.value = textMessage || '존재하지 않는 리소스이거나 잘못된 요청입니다.'
    } else {
      errorMessage.value = textMessage || '서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.'
    }
  }
}

// 3. 취소 버튼 클릭 핸들러 (상세 페이지로 복귀)
const handleCancel = () => {
  router.push({ name: 'thinking-detail', params: { id: thinkingId } })
}

// 컴포넌트 마운트 시 기존 데이터 조회 실행
onMounted(fetchThinkingDetail)
</script>

<style scoped>
/* 아래 스타일은 가이드라인의 구조 이해를 돕기 위한 예시 코드입니다. 
  실제 레이아웃과 색상은 design.md 스펙에 맞춰 수정하여 사용하세요.
*/
.thinking-update-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  margin-bottom: 8px;
  font-weight: bold;
}

.category-buttons {
  display: flex;
  gap: 10px;
}

.category-btn {
  padding: 10px 16px;
  border: 1px solid #ccc;
  background-color: #fff;
  cursor: pointer;
  border-radius: 4px;
}

/* design.md 카테고리별 색상 스펙 예시 매핑 */
.category-btn.active.learned { background-color: #e3f2fd; border-color: #2196f3; color: #1565c0; }
.category-btn.active.lacked { background-color: #ffebee; border-color: #f44336; color: #c62828; }
.category-btn.active.good { background-color: #e8f5e9; border-color: #4caf50; color: #2e7d32; }

.form-input, .form-textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

.button-group {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-cancel {
  background-color: #e0e0e0;
}

.btn-submit {
  background-color: #2196f3;
  color: white;
}

.btn-submit:disabled {
  background-color: #bdbdbd;
  cursor: not-allowed;
}

.error-message {
  color: #d32f2f;
  font-size: 14px;
  margin-top: 6px;
}

.loading-state {
  text-align: center;
  padding: 4px 0;
  color: #666;
}
</style>