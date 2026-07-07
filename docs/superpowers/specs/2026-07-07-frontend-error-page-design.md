# 프론트엔드 에러 페이지 — 설계 문서

## 배경

`PLAN.md`/`TEAM_GUIDE.md`에 정리된 대로 백엔드(`ApiExceptionAdvice`)는 이미 401(비밀번호 불일치)/404(리소스 없음·잘못된 인자)/500(그 외) 상태 코드를 plain text 메시지로 내려주고 있다. 지금까지 프론트는 이 에러들을 전부 인라인(모달·폼 내 텍스트)으로만 처리해왔고, "화면 자체를 그릴 수 없는" 상황(잘못된 URL, 존재하지 않는 리소스, 예상 못한 서버 에러)에 대한 전용 화면이 없다. 이번 작업은 이 공백을 메우는 `ErrorView.vue`를 추가한다.

백엔드 수정은 없다 — 필요한 상태 코드/커스텀 예외(`PasswordMismatchException`, `ResourceNotFoundException`, `ApiExceptionAdvice`)는 이미 구현되어 있고 TEAM_GUIDE의 "backend/** 수정 금지" 원칙을 그대로 따른다.

## 범위

- **포함**: 라우터 404(잘못된 URL) catch-all, API 404/500(초기 로딩 실패 등 페이지 단위 에러) → 전체 화면 에러 페이지
- **제외**: 401(비밀번호 불일치)은 기존 설계(design.md)대로 모달/폼 인라인 메시지 유지. 전역 axios 인터셉터로 자동 리다이렉트하지 않음(댓글 수정/삭제 등 인라인 처리가 필요한 요청까지 화면을 이탈시키는 부작용 방지).

## 아키텍처

단일 컴포넌트 `src/views/ErrorView.vue`가 `status` prop(숫자)과 `message` prop(문자열, 선택)을 받아 이모지/제목/설명을 분기 렌더링한다. 404 전용/500 전용 뷰로 나누지 않는다(레이아웃이 사실상 동일해 중복만 늘어남).

### 라우트 (`src/router/index.js`에 추가)

기존 4개 라우트(`thinking-list`/`thinking-detail`/`thinking-create`/`thinking-update`)는 그대로 두고 2개 추가:

```js
{
  path: '/error',
  name: 'error',
  component: () => import('../views/ErrorView.vue'),
  props: (route) => ({
    status: Number(route.query.status) || 500,
    message: route.query.message,
  }),
},
{
  // 반드시 라우트 배열의 마지막에 위치 (catch-all)
  path: '/:pathMatch(.*)*',
  name: 'not-found',
  component: () => import('../views/ErrorView.vue'),
  props: { status: 404 },
},
```

### 상태 코드 → 콘텐츠 매핑 (컴포넌트 내부)

| status | 이모지 | 제목 | 기본 설명 (message prop 없을 때) |
|---|---|---|---|
| 404 | 🔍 | 페이지를 찾을 수 없어요 | 요청하신 페이지가 존재하지 않거나 삭제되었을 수 있습니다. |
| 500 | ⚠️ | 일시적인 오류가 발생했어요 | 잠시 후 다시 시도해주세요. |
| 그 외(fallback) | ❗ | 문제가 발생했어요 | 알 수 없는 오류가 발생했습니다. |

`message` prop이 있으면(예: 백엔드가 내려준 plain text 메시지) 기본 설명 대신 그 값을 그대로 표시한다.

### 다른 페이지에서 에러 페이지로 이동하는 방법

페이지의 "초기 데이터 로딩"(예: 상세 페이지의 `GET /api/thinking/{id}`)이 404/500으로 실패했을 때, 해당 페이지의 `onMounted` catch 블록에서:

```js
router.push({
  name: 'error',
  query: { status: e.response?.status ?? 500, message: e.response?.data },
})
```

이번 작업 범위에서는 `ErrorView.vue`와 라우터 설정만 만든다. 이미 존재하는 다른 담당자의 페이지(`ThinkingListView`/`ThinkingDetailView`/`ThinkingCreateView`/`ThinkingUpdateView`)를 이 방식으로 수정할지는 별도로 각 담당자와 조율한다(TEAM_GUIDE: "이미 만들어진 다른 사람 담당 페이지 파일을 임의로 수정 금지").

## 시각 디자인

design.md 톤 유지 — 카드형, 중앙 정렬, max-width 960px, Header/Footer 공통 컴포넌트 재사용.

```
┌─────────────────────────────────────────┐
│  Header                        [글쓰기] │
├─────────────────────────────────────────┤
│                                         │
│              🔍 (상태별 이모지)            │
│               404 (상태 코드, 장식용)      │
│         페이지를 찾을 수 없어요 (제목)      │
│   요청하신 페이지가 존재하지 않거나          │
│   삭제되었을 수 있습니다. (설명)           │
│         [ 목록으로 돌아가기 ]              │
│                                         │
│  Footer                                 │
└─────────────────────────────────────────┘
```

- 배경 `#F5F5F5`, 카드 `#FFFFFF` + `border-radius: 12px` + 부드러운 그림자
- 제목: Text Primary `#212121`, 볼드
- 설명: Text Secondary `#757575`
- 상태 코드 숫자: 크고 연한 회색, 장식 요소
- "목록으로 돌아가기" 버튼: Primary `#4A90D9` 배경 + 흰색 텍스트, 클릭 시 `router.push({ name: 'thinking-list' })`

## 데이터 흐름

1. 사용자가 잘못된 URL 접근 → vue-router가 `not-found` 라우트 매칭 → `ErrorView`에 `status=404` 고정 전달, `message` 없음 → 기본 설명 문구 표시.
2. 페이지가 API 호출 실패(404/500) → 해당 페이지가 `router.push({ name: 'error', query: {...} })` 호출 → `ErrorView`가 query를 props로 변환해서 받음 → `message`가 있으면 백엔드 메시지 표시, 없으면 기본 문구.
3. "목록으로 돌아가기" 클릭 → `thinking-list`로 이동, 에러 상태는 그냥 버려짐(별도 정리 로직 불필요 — 라우트 이동 시 컴포넌트 언마운트로 자연 소멸).

## 테스트

- 존재하지 않는 URL(`/foo/bar`) 접근 시 404 콘텐츠가 뜨는지 수동 확인
- `/error?status=500&message=서버%20오류` 같은 쿼리로 직접 접근해 500 콘텐츠/커스텀 메시지가 뜨는지 확인
- `/error`(쿼리 없음) 접근 시 fallback(500) 콘텐츠가 뜨는지 확인
- "목록으로 돌아가기" 버튼이 `/thinking/list`로 이동하는지 확인
- 이번 프로젝트에 프론트 유닛 테스트 러너가 없으므로(TEAM_GUIDE에 언급 없음), `npm run dev`로 브라우저 수동 확인으로 검증한다.
