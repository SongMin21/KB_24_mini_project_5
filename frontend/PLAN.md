# 프론트엔드 구현 계획

익명 회고 게시판(Thinking) 프론트엔드. 상세 UI/스타일은 [design.md](./design.md) 참고.

## 기술 스택

- Vue 3 (Composition API + `<script setup>`)
- Vite
- vue-router 4
- Pinia
- axios

## 프로젝트 위치

`frontend/thinking/` — Vite + Vue3 프로젝트 루트.

```
frontend/thinking/
├── src/
│   ├── views/          # 라우트에 연결되는 페이지 컴포넌트
│   ├── components/      # 재사용 컴포넌트 (Header, Footer, 모달 등)
│   ├── router/          # 라우터 설정
│   ├── stores/          # Pinia 스토어
│   ├── api/             # axios 인스턴스 및 API 모듈
│   └── assets/
├── .env / .env.example  # VITE_API_BASE_URL
└── vite.config.js       # dev server proxy
```

## 백엔드 API 인벤토리

백엔드: `backend/Thinking` (Spring Legacy + Java Config, Gradle, WAR). Tomcat context-path `/`, 로컬 실행 시 기본 포트 `8080`으로 가정(팀원 로컬 Tomcat 설정에 따라 다를 수 있음 — 다르면 `.env`/`vite.config.js` proxy target만 바꾸면 됨, 백엔드는 건드리지 않음).

| 리소스 | Method | Path | 설명 |
|---|---|---|---|
| Thinking | GET | `/api/thinking` | 전체 목록 |
| Thinking | GET | `/api/thinking/date/{date}` | 날짜별 조회 (yyyy-MM-dd) |
| Thinking | GET | `/api/thinking/like` | 공감순 조회 |
| Thinking | GET | `/api/thinking/{id}` | 단건 조회 |
| Thinking | POST | `/api/thinking` | 작성 |
| Thinking | POST | `/api/thinking/{id}/like` | 공감 +1 |
| Thinking | PUT | `/api/thinking` | 수정 (id는 요청 body에 포함) |
| Comment | GET | `/api/comment/{thinkingId}` | 특정 글의 댓글 목록 |
| Comment | POST | `/api/comment/{id}` | 댓글 작성 |
| Comment | PUT | `/api/comment/{id}` | 댓글 수정 |
| Comment | DELETE | `/api/comment/{id}` | 댓글 삭제 |

에러 응답은 JSON이 아니라 **plain text**(예외 메시지)로 내려온다 (`ApiExceptionAdvice`). 401 = 비밀번호 불일치, 404 = 리소스 없음/잘못된 인자, 500 = 그 외.

**백엔드 캐버앗 (연동 전 백엔드 팀과 확인 필요, 프론트에서 임의로 고치지 말 것):**
- `POST /api/comment/{id}`가 `{id}`를 `@PathVariable`로 받지 않음 — 실제로는 요청 body(`CommentCreateDTO`)에 담긴 값으로 어떤 글에 속하는 댓글인지 결정되는 것으로 보임.
- **글 카테고리별 조회 엔드포인트 없음**: `ThinkingServiceImpl.getByCategory()` / `ThinkingMapper.getByCategory()`는 구현되어 있지만 `ThinkingController`에 매핑된 핸들러가 없다. 목록 페이지의 "카테고리별" 필터는 당장 API로 못 붙는다 — 클라이언트 필터링으로 임시 처리 필요(자세한 건 `TEAM_GUIDE.md` A 담당 가이드 참고), 백엔드팀에 엔드포인트 추가 요청 필요.
- **글 삭제 엔드포인트 없음**: `ThinkingServiceImpl.deleteThinking()` / `ThinkingMapper.delete()`도 구현되어 있지만 `ThinkingController`에 매핑이 없다. 상세 페이지의 "삭제" 버튼은 백엔드에 `DELETE /api/thinking/{id}`가 추가되기 전까지 실제 연동이 불가능하다.
- **비밀번호 사전 검증 전용 API 없음**: design.md는 수정/삭제 버튼 클릭 시 비밀번호 모달로 먼저 확인 후 이동/처리하길 요구하지만, 검증만 하는 API가 따로 없다. 글 수정은 모달 없이 바로 수정 페이지로 이동 후 제출 시점에 검증하는 방식으로 단순화 권장(`TEAM_GUIDE.md` B 담당 가이드 참고).
- CORS 설정이 백엔드에 없음 → 반드시 Vite dev server proxy를 통해서만 개발한다 (아래 참고).

(`CommentController` 필드 중복 선언으로 인한 컴파일 에러와, 그로 인해 파생됐던 `GET /api/comment/{id}` 라우팅 미등록 문제는 로컬 빌드/재배포로 확인 및 해결됨 — 실제 코드 버그가 아니라 스테일 빌드 산출물 배포 이슈였음.)

## 라우트 (design.md 기준)

| 이름 | Path | 컴포넌트 |
|---|---|---|
| thinking-list | `/thinking/list` (기본 `/`에서 redirect) | `ThinkingListView.vue` |
| thinking-detail | `/thinking/:id` | `ThinkingDetailView.vue` |
| thinking-create | `/thinking/create` | `ThinkingCreateView.vue` |
| thinking-update | `/thinking/update/:id` | `ThinkingUpdateView.vue` |

## CORS 우회 / dev proxy

`vite.config.js`의 `server.proxy['/api']`가 `http://localhost:8080`으로 전달, path rewrite 없음(백엔드가 이미 `/api/...`로 매핑되어 있으므로). axios `baseURL`은 `VITE_API_BASE_URL`(`.env`, 기본값 `/api`)을 사용— 이 값과 proxy prefix가 항상 일치해야 한다.

## 실행 방법

```
cd frontend/thinking
npm install
npm run dev
```

기본적으로 `http://localhost:5173`에서 뜬다. 백엔드(Tomcat, 8080)가 같이 떠 있어야 `/api/**` 요청이 정상 동작한다.
