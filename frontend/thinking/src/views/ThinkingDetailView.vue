<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api/axios'

const props = defineProps({
  id: {
    type: String,
    required: true,
  },
})

const router = useRouter()

const thinking = ref(null)
const comments = ref([])
const loading = ref(true)

const heartBounce = ref(false)

const commentContent = ref('')
const commentPassword = ref('')

const editingCommentId = ref(null)
const editingContent = ref('')
const editingPassword = ref('')

const showPasswordModal = ref(false)
const passwordModalMode = ref('') // 'comment-update' | 'comment-delete'
const passwordInput = ref('')
const passwordError = ref('')
const targetCommentId = ref(null)

const categoryMeta = computed(() => {
  if (!thinking.value) return null
  const map = {
    LEARNED: { label: '배운 것', color: 'var(--color-learned)' },
    LACKED: { label: '부족한 것', color: 'var(--color-lacked)' },
    GOOD: { label: '좋았던 것', color: 'var(--color-good)' },
  }
  return map[thinking.value.category]
})

const formatDate = (ms) => {
  const d = new Date(ms)
  const yyyy = d.getFullYear()
  const MM = String(d.getMonth() + 1).padStart(2, '0')
  const dd = String(d.getDate()).padStart(2, '0')
  const HH = String(d.getHours()).padStart(2, '0')
  const mm = String(d.getMinutes()).padStart(2, '0')
  return `${yyyy}.${MM}.${dd} ${HH}:${mm}`
}

const fetchThinking = async () => {
  const { data } = await api.get(`/thinking/${props.id}`)
  thinking.value = data
}

const fetchComments = async () => {
  const { data } = await api.get(`/comment/${props.id}`)
  comments.value = data
}

onMounted(async () => {
  loading.value = true
  try {
    await Promise.all([fetchThinking(), fetchComments()])
  } catch (e) {
    console.error(e.response?.data ?? '오류가 발생했습니다.')
  } finally {
    loading.value = false
  }
})

const goToList = () => {
  router.push({ name: 'thinking-list' })
}

const goToUpdate = () => {
  router.push({ name: 'thinking-update', params: { id: props.id } })
}

// TODO: DELETE /api/thinking/{id} 백엔드 추가 후 연동
const handleDeleteClick = () => {
  alert('삭제 기능은 곧 지원 예정입니다.')
}

const handleLike = async () => {
  try {
    const { data } = await api.post(`/thinking/${props.id}/like`)
    thinking.value.likeCount = data.likeCount
    heartBounce.value = true
    setTimeout(() => {
      heartBounce.value = false
    }, 300)
  } catch (e) {
    console.error(e.response?.data ?? '오류가 발생했습니다.')
  }
}

const submitComment = async () => {
  if (!commentContent.value.trim() || !commentPassword.value) return
  try {
    const { data } = await api.post(`/comment/${props.id}`, {
      thinkingId: thinking.value.id,
      content: commentContent.value,
      password: commentPassword.value,
    })
    comments.value.push(data)
    commentContent.value = ''
    commentPassword.value = ''
  } catch (e) {
    const msg = e.response?.data ?? '오류가 발생했습니다.'
    console.error(msg)
    alert(msg)
  }
}

const openPasswordModal = (commentId, mode) => {
  targetCommentId.value = commentId
  passwordModalMode.value = mode
  passwordInput.value = ''
  passwordError.value = ''
  showPasswordModal.value = true
}

const closePasswordModal = () => {
  showPasswordModal.value = false
  passwordModalMode.value = ''
  targetCommentId.value = null
  passwordInput.value = ''
  passwordError.value = ''
}

const cancelEdit = () => {
  editingCommentId.value = null
  editingContent.value = ''
  editingPassword.value = ''
}

const confirmPassword = async () => {
  if (!passwordInput.value) return

  if (passwordModalMode.value === 'comment-delete') {
    try {
      await api.delete(`/comment/${targetCommentId.value}`, {
        data: { password: passwordInput.value },
      })
      comments.value = comments.value.filter((c) => c.id !== targetCommentId.value)
      closePasswordModal()
    } catch (e) {
      if (e.response?.status === 401) {
        passwordError.value = '비밀번호가 일치하지 않습니다.'
        return
      }
      const msg = e.response?.data ?? '오류가 발생했습니다.'
      console.error(msg)
      alert(msg)
    }
  } else if (passwordModalMode.value === 'comment-update') {
    const comment = comments.value.find((c) => c.id === targetCommentId.value)
    editingCommentId.value = targetCommentId.value
    editingContent.value = comment.content
    editingPassword.value = passwordInput.value
    closePasswordModal()
  }
}

const saveEditedComment = async () => {
  if (!editingContent.value.trim()) return
  try {
    const { data } = await api.put(`/comment/${editingCommentId.value}`, {
      content: editingContent.value,
      password: editingPassword.value,
    })
    const idx = comments.value.findIndex((c) => c.id === editingCommentId.value)
    comments.value[idx] = data
    cancelEdit()
  } catch (e) {
    if (e.response?.status === 401) {
      alert('비밀번호가 일치하지 않습니다.')
      cancelEdit()
      return
    }
    const msg = e.response?.data ?? '오류가 발생했습니다.'
    console.error(msg)
    alert(msg)
  }
}
</script>

<template>
  <div class="detail-page">
    <button class="back-link" type="button" @click="goToList">← 목록으로</button>

    <div v-if="loading" class="state-text">불러오는 중...</div>

    <template v-else-if="thinking">
      <article class="post-card">
        <span class="badge" :style="{ backgroundColor: categoryMeta.color }">
          {{ categoryMeta.label }}
        </span>
        <h1 class="title">{{ thinking.title }}</h1>
        <p class="content">{{ thinking.content }}</p>
        <p class="date">{{ formatDate(thinking.createdAt) }}</p>

        <div class="actions">
          <button class="like-btn" type="button" :class="{ flash: heartBounce }" @click="handleLike">
            <span class="heart-icon" :class="{ bounce: heartBounce }">❤️</span>
            공감 {{ thinking.likeCount }}
          </button>
          <div class="post-buttons">
            <button class="edit-btn" type="button" @click="goToUpdate">수정</button>
            <button class="delete-btn" type="button" @click="handleDeleteClick">삭제</button>
          </div>
        </div>
      </article>

      <section class="comment-form">
        <h2 class="comments-heading">댓글 작성</h2>
        <textarea
          v-model="commentContent"
          class="comment-textarea"
          placeholder="댓글을 입력하세요"
        ></textarea>
        <div class="comment-form-row">
          <input
            v-model="commentPassword"
            type="password"
            class="comment-password-input"
            placeholder="비밀번호"
          />
          <button type="button" class="submit-btn" @click="submitComment">댓글 작성</button>
        </div>
      </section>

      <section class="comments-section">
        <h2 class="comments-heading">댓글 {{ comments.length }}개</h2>

        <p v-if="comments.length === 0" class="state-text">
          아직 댓글이 없어요. 첫 번째 댓글을 남겨보세요!
        </p>

        <ul v-else class="comment-list">
          <li v-for="comment in comments" :key="comment.id" class="comment-card">
            <template v-if="editingCommentId === comment.id">
              <textarea v-model="editingContent" class="edit-textarea"></textarea>
              <div class="comment-edit-buttons">
                <button type="button" class="cancel-btn" @click="cancelEdit">취소</button>
                <button type="button" class="save-btn" @click="saveEditedComment">저장</button>
              </div>
            </template>
            <template v-else>
              <p class="comment-content">{{ comment.content }}</p>
              <div class="comment-footer">
                <span class="comment-date">{{ formatDate(comment.createdAt) }}</span>
                <div class="comment-buttons">
                  <button
                    type="button"
                    class="edit-btn"
                    @click="openPasswordModal(comment.id, 'comment-update')"
                  >
                    수정
                  </button>
                  <button
                    type="button"
                    class="delete-btn"
                    @click="openPasswordModal(comment.id, 'comment-delete')"
                  >
                    삭제
                  </button>
                </div>
              </div>
            </template>
          </li>
        </ul>
      </section>
    </template>

    <div v-if="showPasswordModal" class="modal-overlay" @click.self="closePasswordModal">
      <div class="modal">
        <p class="modal-text">비밀번호를 입력하세요</p>
        <input
          v-model="passwordInput"
          type="password"
          class="modal-input"
          @keyup.enter="confirmPassword"
        />
        <p v-if="passwordError" class="modal-error">{{ passwordError }}</p>
        <div class="modal-buttons">
          <button type="button" class="cancel-btn" @click="closePasswordModal">취소</button>
          <button type="button" class="confirm-btn" @click="confirmPassword">확인</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.detail-page {
  padding: 24px 0 60px;
}

.back-link {
  border: none;
  background: none;
  padding: 0;
  margin-bottom: 16px;
  font-size: 14px;
  color: var(--color-text-secondary);
}

.state-text {
  text-align: center;
  color: var(--color-text-secondary);
  padding: 24px 0;
}

.post-card,
.comment-card,
.comment-form,
.modal {
  background: var(--color-card);
  border-radius: var(--radius-base);
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.post-card {
  padding: 28px;
}

.badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 999px;
  color: #fff;
  font-size: 13px;
  font-weight: 600;
}

.title {
  font-size: 20px;
  font-weight: 700;
  margin: 16px 0 12px;
}

.content {
  font-size: 16px;
  line-height: 1.8;
  white-space: pre-wrap;
  margin: 0 0 16px;
}

.date {
  color: var(--color-text-secondary);
  font-size: 14px;
  margin: 0 0 20px;
}

.actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-top: 1px solid var(--color-border);
  padding-top: 16px;
}

.like-btn {
  border: none;
  background: none;
  font-size: 15px;
  font-weight: 600;
  color: var(--color-text-primary);
  display: flex;
  align-items: center;
  gap: 6px;
  transition: color 0.3s ease;
}

.like-btn.flash {
  color: #e53935;
}

.heart-icon {
  display: inline-block;
  transition: transform 0.3s ease;
}

.heart-icon.bounce {
  animation: heartPulse 0.3s ease;
}

@keyframes heartPulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.3);
  }
  100% {
    transform: scale(1);
  }
}

.post-buttons {
  display: flex;
  gap: 8px;
}

.edit-btn,
.delete-btn,
.cancel-btn,
.save-btn,
.confirm-btn,
.submit-btn {
  border: 1px solid var(--color-border);
  background: #fff;
  border-radius: 8px;
  padding: 6px 14px;
  font-size: 14px;
  color: var(--color-text-primary);
}

.submit-btn,
.confirm-btn,
.save-btn {
  border: none;
  background: var(--color-primary);
  color: #fff;
  font-weight: 600;
}

.comments-section {
  margin-top: 24px;
}

.comments-heading {
  font-size: 16px;
  font-weight: 700;
  border-bottom: 1px solid var(--color-border);
  padding-bottom: 12px;
  margin-bottom: 16px;
}

.comment-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.comment-card {
  padding: 16px;
}

.comment-content {
  margin: 0 0 12px;
  font-size: 15px;
  white-space: pre-wrap;
}

.comment-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.comment-date {
  color: var(--color-text-secondary);
  font-size: 13px;
}

.comment-buttons {
  display: flex;
  gap: 8px;
}

.edit-textarea,
.comment-textarea {
  width: 100%;
  min-height: 80px;
  border: 1px solid var(--color-border);
  border-radius: 8px;
  padding: 10px;
  font-family: inherit;
  font-size: 14px;
  resize: vertical;
  box-sizing: border-box;
}

.comment-edit-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 8px;
}

.comment-form {
  margin-top: 32px;
  padding: 20px;
}

.comment-form-row {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.comment-password-input {
  border: 1px solid var(--color-border);
  border-radius: 8px;
  padding: 8px 10px;
  font-size: 14px;
  width: 160px;
}

.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
}

.modal {
  padding: 24px;
  width: 280px;
}

.modal-text {
  margin: 0 0 12px;
  font-size: 15px;
  font-weight: 600;
}

.modal-input {
  width: 100%;
  border: 1px solid var(--color-border);
  border-radius: 8px;
  padding: 8px 10px;
  font-size: 14px;
  box-sizing: border-box;
}

.modal-error {
  color: #e53935;
  font-size: 13px;
  margin: 8px 0 0;
}

.modal-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 16px;
}
</style>
