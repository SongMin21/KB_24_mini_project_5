# 프론트엔드 팀 작업 가이드 (4인)

이 저장소의 프론트엔드는 `frontend/thinking/`에서 작업한다. 전체 배경/API 목록은 [PLAN.md](./PLAN.md), 페이지별 UI/스타일 스펙(레이아웃, 색상, 인터랙션)은 [design.md](./design.md)를 참고한다. **이 문서 + PLAN.md + design.md 세 개만 보면 각자 담당 페이지를 바로 시작할 수 있도록** 실제 API 요청/응답 형태와 현재 백엔드의 한계까지 정리해뒀다.

## 페이지 담당 배정

페이지 4개 = 팀원 4명. 아래 표에 이름을 채운다. 각 담당의 상세 가이드는 이 문서 아래 "담당별 상세 가이드" 섹션에 있다.

| 담당 | 페이지 | 파일 | 라우트 | 담당자 |
|---|---|---|---|---|
| A | 목록 | `src/views/ThinkingListView.vue` | `/thinking/list` | |
| B | 상세 (+ 댓글) | `src/views/ThinkingDetailView.vue` | `/thinking/:id` | |
| C | 작성 | `src/views/ThinkingCreateView.vue` | `/thinking/create` | |
| D | 수정 | `src/views/ThinkingUpdateView.vue` | `/thinking/update/:id` | |

`Header`/`Footer` 등 공통 컴포넌트(`src/components/`)는 모든 페이지에서 쓰이므로, 먼저 필요해진 사람이 만들고 다른 사람이 재사용한다. 겹치는 작업이 생기면 먼저 PR 올린 쪽 기준으로 맞춘다.

---

## 반드시 지킬 코드 컨벤션

- **Composition API + `<script setup>`만 사용.** Options API 금지.
- 반응형 상태는 `ref`/`reactive`, 파생 값은 `computed`, 사이드이펙트는 `onMounted` 등 라이프사이클 훅.
- **함수는 화살표 함수 기본** (`const fn = () => {}`). `function` 키워드는 특별한 이유 없으면 지양.
- **HTTP 통신은 반드시 `src/api/axios.js`의 공통 인스턴스로.** `fetch` 금지.
- **비동기는 `async/await`.** `.then().catch()` 체인 금지.
- API 호출은 `try/catch`로 에러 처리:

```js
import { ref, onMounted } from 'vue'
import api from '@/api/axios'

const items = ref([])
const loading = ref(false)

const fetchItems = async () => {
  loading.value = true
  try {
    const { data } = await api.get('/thinking')
    items.value = data
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

onMounted(fetchItems)
```

- 페이지 컴포넌트: `PascalCase` + `View` 접미사, **페이지당 1파일**. 한 파일에 여러 페이지 몰아넣지 않는다.
- 라우트 `path`는 kebab-case, `name`은 의미가 드러나는 문자열 (이미 `router/index.js`에 등록되어 있음, 필요 시 상의 후 변경).

---

## 공통 — 시작 전에 반드시 읽을 백엔드 제약사항

### 1. 에러 응답은 JSON이 아니라 plain text

`ApiExceptionAdvice`가 모든 예외를 텍스트 문자열로 내려준다. axios 기준 `e.response.status`로 분기하고 `e.response.data`는 문자열로 다룬다.

| status | 의미 |
|---|---|
| 401 | 비밀번호 불일치 (`PasswordMismatchException`) |
| 404 | 존재하지 않는 리소스 / 잘못된 인자 (`IllegalArgumentException`, `ResourceNotFoundException`) |
| 500 | 그 외 (라우팅 실패 등도 여기로 뭉뚱그려짐) |

```js
try {
  await api.post(`/thinking/${id}/like`)
} catch (e) {
  const msg = e.response?.data ?? '오류가 발생했습니다.'
  if (e.response?.status === 401) {
    // 비밀번호 불일치 처리
  }
  console.error(msg)
}
```

### 2. `PUT /api/thinking`은 다른 API와 다르게 plain text를 응답한다

다른 엔드포인트는 다 JSON(`ThinkingDTO`/`CommentDTO`)을 내려주는데, 글 수정만 예외적으로 `"success"` / `"fail"` 문자열을 응답 바디로 그대로 내려준다(성공 200, 실패 401). `response.data === 'success'`로 성공 여부를 판단해야 한다 — D 담당(수정 페이지) 필독.

### 3. 댓글 작성 URL의 `{id}`는 무시된다

`POST /api/comment/{id}`의 `{id}` path variable은 컨트롤러에서 `@PathVariable`로 받지 않는다. 실제로 어떤 글의 댓글인지는 **요청 body의 `thinkingId` 필드**로 결정된다. URL의 id 값 자체는 아무 의미 없지만, 그래도 관례상 `/api/comment/${thinkingId}`로 호출하고 body에도 `thinkingId`를 넣는다(아래 표 참고).

### 4. 아직 백엔드에 없는 기능 (구현 시 우회 필요, 백엔드팀과 조율 필요)

design.md에는 있지만 **현재 컨트롤러에 엔드포인트가 없는** 기능들이다. 서비스/매퍼 레이어에는 이미 로직이 구현돼 있는 경우도 있으니(예: 카테고리 필터, 글 삭제) 백엔드팀에 컨트롤러 메서드 추가만 요청하면 될 가능성이 높다 — **프론트에서 백엔드 컨트롤러를 직접 추가하지 말고, 먼저 백엔드 담당자에게 요청한다.**

- **글 카테고리별 필터** (`ThinkingServiceImpl.getByCategory()`는 있지만 `ThinkingController`에 매핑 없음): A 담당(목록 페이지)은 당장은 `GET /api/thinking` 전체 목록을 받아 **프론트에서 `category` 필드로 클라이언트 필터링**하는 걸로 임시 구현하고, 백엔드에 `GET /api/thinking/category/{category}` 같은 엔드포인트 추가를 요청한다.
- **글 삭제** (`ThinkingServiceImpl.deleteThinking()`은 있지만 `ThinkingController`에 매핑 없음): B 담당(상세 페이지)의 "삭제" 버튼은 **호출할 API가 아직 없다.** 버튼 UI만 만들어두고 실제 연동은 백엔드에 `DELETE /api/thinking/{id}` 추가를 요청한 뒤 진행한다.
- **비밀번호 사전 검증 전용 API 없음**: design.md는 "수정/삭제 버튼 클릭 → 비밀번호 모달 → 일치 확인 후 이동"을 요구하지만, 비밀번호만 미리 확인하는 API가 따로 없다. 즉:
  - 글 수정: 비밀번호 모달 없이 바로 수정 페이지로 이동시키고, 실제 `PUT /api/thinking` 제출 시 비밀번호가 틀리면 그때 401 에러를 폼에 표시하는 방식으로 단순화한다.
  - 댓글 수정/삭제는 API 자체가 이미 매 요청마다 비밀번호를 받게 되어 있으니(`PUT`/`DELETE /api/comment/{id}` body에 `password` 포함), 모달에서 비밀번호를 받아서 그 값을 바로 수정/삭제 요청에 실어 보내면 된다(별도 사전 검증 없이 한 번에 처리).
- **목록 페이지네이션 파라미터 없음**: `GET /api/thinking`은 페이지/limit 파라미터를 받지 않고 전체 목록을 한 번에 반환한다. A 담당은 우선 **클라이언트 사이드 페이지네이션**(받아온 배열을 slice)으로 구현하고, 데이터가 많아지면 백엔드에 페이징 파라미터 추가를 요청한다.

### 5. 카테고리 값

`category`는 문자열이며 다음 3개 값만 사용한다 (실제 DB 데이터로 확인됨): `"LEARNED"`(배운 것), `"LACKED"`(부족한 것), `"GOOD"`(좋았던 것). design.md의 색상 매핑과 동일하게 쓰면 된다.

---

## API 레퍼런스 (요청/응답 필드 포함)

### Thinking

| 목적 | Method & Path | 요청 body | 응답 |
|---|---|---|---|
| 전체 목록 | `GET /api/thinking` | - | `ThinkingDTO[]` |
| 날짜별 조회 | `GET /api/thinking/date/{yyyy-MM-dd}` | - | `ThinkingDTO[]` |
| 공감순 조회 | `GET /api/thinking/like` | - | `ThinkingDTO[]` |
| 단건 조회 | `GET /api/thinking/{id}` | - | `ThinkingDTO` |
| 작성 | `POST /api/thinking` | `{ category, title, content, password }` | `ThinkingDTO` |
| 공감 +1 | `POST /api/thinking/{id}/like` | - | `ThinkingDTO` (변경된 likeCount 포함) |
| 수정 | `PUT /api/thinking` | `{ id, category, title, content, password }` | **plain text** `"success"` (200) / `"fail"` (401) — 위 2번 항목 참고 |
| 삭제 | ❌ 없음 | — | 4번 항목 참고 |
| 카테고리별 조회 | ❌ 없음 | — | 4번 항목 참고 |

`ThinkingDTO` 응답 필드: `{ id, category, title, content, likeCount, createdAt, updatedAt }` (password는 응답에 없음, createdAt/updatedAt은 epoch ms 숫자로 내려옴).

### Comment

| 목적 | Method & Path | 요청 body | 응답 |
|---|---|---|---|
| 댓글 목록 | `GET /api/comment/{thinkingId}` | - | `CommentDTO[]` |
| 댓글 작성 | `POST /api/comment/{thinkingId}` | `{ thinkingId, content, password }` (**주의**: URL의 id는 무시됨, body의 thinkingId가 진짜 기준 — 위 3번 항목) | `CommentDTO` |
| 댓글 수정 | `PUT /api/comment/{id}` | `{ content, password }` | `CommentDTO` |
| 댓글 삭제 | `DELETE /api/comment/{id}` | `{ password }` | `CommentDTO` (삭제된 댓글 정보) |

`CommentDTO` 응답 필드: `{ id, thinkingId, content, createdAt, updatedAt }`.

---

## 담당별 상세 가이드

### A — 목록 페이지 (`ThinkingListView.vue`, `/thinking/list`)

**design.md 참고 섹션**: "페이지 1: 목록 페이지"

**필요한 상태**:
- `items` (현재 화면에 표시할 글 배열), `activeTab` (`all` | `date` | `category` | `like`), `selectedDate`, `selectedCategory`, `currentPage`(클라이언트 페이지네이션용)

**탭별 데이터 소스**:
| 탭 | 호출 | 비고 |
|---|---|---|
| 전체 | `GET /api/thinking` | 기본 탭 |
| 날짜별 | `GET /api/thinking/date/{date}` | `date`는 `<input type="date">` 값(`yyyy-MM-dd`)을 그대로 사용 |
| 카테고리별 | (없음, 4-①번 항목 참고) | `GET /api/thinking` 결과를 `items.filter(i => i.category === selectedCategory)`로 클라이언트 필터링 |
| 공감순 | `GET /api/thinking/like` | 이미 정렬되어 내려오는지 실제로 확인하고, 안 되어 있으면 `[...list].sort((a,b) => b.likeCount - a.likeCount)`로 보정 |

**페이지네이션**: 서버가 지원하지 않으므로(4-④번 항목), 받아온 전체 배열을 `computed`로 `slice(pageSize*(page-1), pageSize*page)` 해서 카드 목록에 뿌리고, 하단 페이지 번호 버튼은 `Math.ceil(items.length / pageSize)`로 개수를 계산한다.

**카드 클릭**: `router.push({ name: 'thinking-detail', params: { id: item.id } })`

**빈 상태**: design.md "빈 상태" 섹션대로 `items.length === 0`일 때 안내 문구 + `글쓰기` 버튼(→ `thinking-create` 라우트) 표시.

**"글쓰기" 버튼**(공통 Header에도 있음): `router.push({ name: 'thinking-create' })`.

### B — 상세 페이지 (`ThinkingDetailView.vue`, `/thinking/:id`)

**design.md 참고 섹션**: "페이지 2: 글 상세 페이지"

**필요한 상태**: `thinking`(글 데이터), `comments`(댓글 배열), `showPasswordModal`, `passwordModalMode`(`update`|`delete`|`comment-update`|`comment-delete`), `passwordInput`, `passwordError`, `editingCommentId`(인라인 수정 모드용)

**초기 로딩** (`onMounted`): `route.params.id`로 `GET /api/thinking/{id}`, `GET /api/comment/{id}` 두 개를 병렬로 호출(`Promise.all` + 각각 `await`, 또는 두 개의 async 함수를 그냥 순차 호출해도 무방).

**공감 버튼**: `POST /api/thinking/{id}/like` 호출 → 응답으로 온 `likeCount`로 화면 갱신(전체 재조회 불필요). design.md의 하트 스케일 애니메이션은 CSS transition + 클릭 시 클래스 토글로 구현(스타일 단계에서 처리, 지금은 클릭 핸들러만 연결해두면 됨).

**수정 버튼**: design.md는 비밀번호 모달을 요구하지만 사전 검증 API가 없으므로(4-③번 항목), **바로 `router.push({ name: 'thinking-update', params: { id } })`로 이동**한다. 비밀번호는 수정 페이지에서 실제 제출 시 확인된다. (팀 내 합의로 모달을 유지하고 싶다면, 모달에서 입력받은 비밀번호를 수정 페이지로 쿼리스트링 등으로 넘기지 말고 — 어차피 검증이 안 되므로 — 모달 없이 바로 이동하는 쪽을 권장.)

**삭제 버튼**: 4-④번 항목대로 현재 호출할 API가 없다. 버튼은 만들되 클릭 시 "곧 지원 예정" 정도의 처리만 해두거나, 백엔드팀이 `DELETE /api/thinking/{id}`를 추가해줄 때까지 보류. **다른 사람 작업을 막지 않도록 TODO 주석으로 명확히 표시해둘 것.**

**댓글 작성**: 폼 제출 시 `POST /api/comment/{id}` (id는 현재 글 id, 어차피 body의 thinkingId가 기준) with `{ thinkingId: id, content, password }`. 성공 시 댓글 목록에 추가하거나 재조회.

**댓글 수정/삭제**: 각 댓글의 수정/삭제 버튼 클릭 → 비밀번호 모달 표시 → 입력값을 그대로 `PUT /api/comment/{commentId}` (`{ content, password }`) 또는 `DELETE /api/comment/{commentId}` (`{ password }`)로 전송. 401이면 모달에 "비밀번호가 일치하지 않습니다" 표시(모달 유지), 성공하면 모달 닫고 목록 갱신.

### C — 작성 페이지 (`ThinkingCreateView.vue`, `/thinking/create`)

**design.md 참고 섹션**: "페이지 3: 글 작성 페이지"

**필요한 상태**: `category`(초기값 없음, 3개 버튼 중 하나 선택), `title`, `content`, `password`

**제출 전 검증**: design.md대로 카테고리 미선택 시 제출 불가 — 버튼 `disabled` 조건에 `!category` 포함. 나머지 필드도 비어있으면 막아도 되고, 최종 검증은 백엔드가 400계열(`IllegalArgumentException` → 404)로도 걸러준다.

**제출**: `POST /api/thinking` with `{ category, title, content, password }` → 성공 시 응답 `ThinkingDTO`의 `id`로 상세 페이지 이동: `router.push({ name: 'thinking-detail', params: { id: data.id } })`.

**취소 버튼**: `router.push({ name: 'thinking-list' })`.

### D — 수정 페이지 (`ThinkingUpdateView.vue`, `/thinking/update/:id`)

**design.md 참고 섹션**: "페이지 4: 글 수정 페이지" (작성 페이지와 레이아웃 동일, 값 프리필 + 타이틀만 다름)

**필요한 상태**: C 담당과 동일 + `id`(route param)

**초기 로딩** (`onMounted`): `GET /api/thinking/{id}`로 기존 값을 가져와 `category`/`title`/`content`에 프리필. **`password`는 절대 프리필하지 않고 빈 값으로 둔다** (응답에도 어차피 password 필드가 없음 — 새로 입력받아야 함).

**제출**: `PUT /api/thinking` with `{ id, category, title, content, password }`. **응답이 JSON이 아니라 plain text `"success"`/`"fail"`이라는 점 필독** (공통 섹션 2번). 성공(`response.data === 'success'`) 시 상세 페이지로 이동, 실패(401, `"fail"`) 시 "비밀번호가 일치하지 않습니다" 에러를 폼에 표시.

**취소 버튼**: `router.push({ name: 'thinking-detail', params: { id } })`.

---

## 실행

```
cd frontend/thinking
npm install
npm run dev
```

백엔드(Tomcat, `backend/Thinking`, 로컬 8080)를 같이 띄워야 `/api/**` 호출이 프록시를 통해 붙는다. 포트가 다르면 `frontend/thinking/vite.config.js`의 `server.proxy['/api'].target`과 `.env`의 `VITE_API_BASE_URL`만 맞추면 된다 — 백엔드 코드는 절대 수정하지 않는다.

## 하지 말 것

- `backend/**` 어떤 파일도 수정/삭제/이동 금지 (CORS 문제도 vite proxy로 해결, 백엔드 건드리지 않음). 위 "아직 백엔드에 없는 기능"은 백엔드팀에 요청하고, 프론트에서 임시로 컨트롤러를 추가하지 않는다.
- 지금 단계에서 이미 만들어진 다른 사람 담당 페이지 파일을 임의로 수정 — 공통 컴포넌트(`src/components/`)나 `router/index.js` 같은 공유 파일을 바꿔야 하면 먼저 팀에 공유한다.
- `git commit` — 각자 브랜치에서 작업하고 PR로 올린다 (세부 브랜치 전략은 팀 내 별도 합의).
