# KB 스타일 디자인 통일 — 디자인 명세

## 배경

현재 `frontend/thinking` 프런트엔드는 `frontend/design.md`에 정의된 팔레트(`--color-primary: #4A90D9` 블루 계열)를 기준으로 만들어졌지만, 실제 뷰 파일(`ThinkingUpdateView.vue`, `ThinkingListView.vue` 등) 7개 파일에 걸쳐 하드코딩된 hex 색상이 86곳 있어 CSS 변수와 실제 화면 색상이 어긋나 있다. 특히 `ThinkingUpdateView.vue`는 다른 페이지가 쓰는 `border-radius`(8px/999px/12px) 컨벤션 대신 혼자 `4px`를 쓰고, 자체 블루(`#2196f3`)를 하드코딩하고 있어 페이지마다 톤이 다르게 보인다.

이번 작업은 국민은행(KB) 브랜드 컬러를 기준으로 팔레트를 재정의하고, 모든 페이지가 CSS 변수만 참조하도록 정리하여 전체적으로 통일된 느낌을 만드는 것이 목표다.

## 요구사항 요약 (사용자 확정 사항)

- KB 스타일 적용 수준: **적극적** — 주요 버튼/CTA/선택 상태를 KB Yellow로 통일
- 카테고리 3종(배운 것/부족한 것/좋았던 것) 색상: **기존 유지** (녹색/오렌지/핑크) — KB 컬러는 브랜드 아이덴티티 역할만 담당
- 헤더: **흰 배경 + 노란색 "글쓰기" CTA 버튼** (전체 노란 바 아님)

## 컬러 토큰 재정의 (`frontend/thinking/src/assets/main.css`)

| 토큰 | 기존 값 | 변경 값 | 비고 |
|---|---|---|---|
| `--color-primary` | `#4A90D9` | `#FFBC00` (KB Yellow Positive) | 주요 버튼, 선택된 탭/토글 배경 |
| `--color-primary-hover` | (없음, 개별 하드코딩 `#3d7ab8`) | `#E6A800` | Primary 버튼 hover (직접 계산한 딥옐로우) |
| `--color-on-primary` | (없음, 기존엔 흰 텍스트) | `#2B2313` | 노란 배경 위에 올라가는 텍스트. 흰 글씨는 대비가 부족해 다크 웜블랙 사용 |
| `--color-text-primary` | `#212121` | `#545045` (KB Dark Gray) | 제목/본문 텍스트 |
| `--color-text-secondary` | `#757575` | `#60584C` (KB Gray) | 보조 텍스트 (날짜, 설명 등) |
| `--color-border` | `#E0E0E0` | `#E6E1D6` | 웜톤 라이트 그레이로 변경, 카드/인풋 테두리 |
| `--color-background` | `#F5F5F5` | `#F7F5F0` | 웜 오프화이트 배경 |
| `--color-card` | `#FFFFFF` | 변경 없음 | |
| `--color-danger` | (없음, 개별 하드코딩 `#d32f2f`, `#c62828` 등) | `#D32F2F` | 에러/경고 텍스트. 브랜드 색상이 아닌 범용 의미색이라 유지하되 변수로 승격 |
| `--color-learned` | (없음, 하드코딩 `#4CAF50` 계열) | `#4CAF50` | 배운 것 — 기존 색 유지, 변수로 승격 |
| `--color-lacked` | (없음, 하드코딩 `#FF9800` 계열) | `#FF9800` | 부족한 것 — 기존 색 유지, 변수로 승격 |
| `--color-good` | (없음, 하드코딩 `#E91E63` 계열) | `#E91E63` | 좋았던 것 — 기존 색 유지, 변수로 승격 |
| `--radius-base` | `12px` | 변경 없음 | 카드에 계속 사용 |

새로 추가하는 반경/버튼 관련 변수:

| 토큰 | 값 | 용도 |
|---|---|---|
| `--radius-input` | `8px` | 인풋/텍스트에어리어/일반 버튼 (List/Create/Detail 페이지 기존 컨벤션) |
| `--radius-pill` | `999px` | 알약형 버튼 (탭, 카테고리 토글, 헤더 CTA) |

## 컴포넌트별 적용 규칙

### Header (`components/Header.vue`)
- 배경: 흰색 유지 (`--color-card`), 하단 얇은 `--color-border` 구분선 유지
- 로고 텍스트: `--color-text-primary`
- "글쓰기" 버튼: 배경 `--color-primary`, 텍스트 `--color-on-primary`, hover 시 `--color-primary-hover`, radius `--radius-pill`

### Footer (`components/Footer.vue`)
- 텍스트: `--color-text-secondary`, 구분선 `--color-border` — 색상 토큰만 교체, 구조 변경 없음

### 목록 페이지 (`ThinkingListView.vue`)
- 필터 탭(전체/날짜별/카테고리별/공감순): 선택됨 = 배경 `--color-primary` + 텍스트 `--color-on-primary`, 미선택 = 흰 배경 + `--color-text-secondary` + `--color-border` 테두리
- 카테고리 필터 버튼(카테고리별 탭 하위): 각 카테고리 컬러(`--color-learned/lacked/good`) 유지
- 게시글 카드: 배경 `--color-card`, 테두리 `--color-border`, hover 시 그림자 강화 — 구조 변경 없음, 색상 토큰만 정리
- 페이지네이션 버튼: 선택 페이지만 `--color-primary` 강조

### 상세 페이지 (`ThinkingDetailView.vue`)
- 공감 버튼: 아이콘/숫자 강조색은 카테고리색이 아닌 별도 포인트가 필요하면 `--color-primary` 톤 사용 (기존 로직 유지, 색상 토큰만 교체)
- 수정/삭제 버튼: 보조 버튼 스타일(연한 그레이 배경 + `--color-text-secondary`)
- 비밀번호 모달 확인 버튼: `--color-primary` + `--color-on-primary`
- 댓글 작성 버튼: `--color-primary` + `--color-on-primary`

### 작성/수정 페이지 (`ThinkingCreateView.vue`, `ThinkingUpdateView.vue`)
- 카테고리 토글 버튼: 미선택 = 흰 배경 + `--color-border` 테두리 + `--color-text-secondary`, 선택 = 각 카테고리 컬러 배경 + 흰 텍스트 (카테고리색은 채도가 있어 흰 텍스트로도 대비 확보됨, 기존 유지)
- 제출 버튼("작성하기"/"수정 완료"): `--color-primary` + `--color-on-primary`, disabled 시 회색조
- 취소 버튼: 연한 그레이 배경 + `--color-text-secondary`
- **`ThinkingUpdateView.vue` 전용 정리**: 현재 `border-radius: 4px`로 되어 있는 카드/버튼/인풋을 각각 `--radius-input`(인풋), `--radius-pill`(버튼)로 교체해 다른 페이지와 라디우스 컨벤션을 맞춘다. 하드코딩된 `#2196f3`, `#bdbdbd`, `#d32f2f` 등도 전부 위 토큰으로 교체.

### 에러 페이지 (`ErrorView.vue`)
- 카드 배경/테두리: `--color-card` / `--color-border`
- "목록으로 돌아가기" 버튼: `--color-primary` + `--color-on-primary`
- 상태 코드/이모지 텍스트 색상: `--color-text-secondary`

## `frontend/design.md` 문서 갱신

"디자인 방향 > 컬러 팔레트" 섹션을 위 새 토큰 표로 교체한다. 레이아웃(와이어프레임 ASCII 다이어그램) 자체는 변경하지 않고, 색상 관련 서술만 갱신한다. 헤더 설명에 "글쓰기 버튼은 KB Yellow로 강조" 문구를 추가한다.

## 범위에서 제외하는 것

- 레이아웃/구조 변경 없음 (와이어프레임 그대로)
- 폰트 변경 없음 (Pretendard/Noto Sans KR 유지)
- 카테고리 3색(녹/오렌지/핑크) 변경 없음
- 백엔드 변경 없음
- `--radius-base`(카드용 12px) 값 자체는 변경하지 않음 — 버튼/인풋 반경만 별도 토큰으로 분리

## 검증 방법

1. `frontend/thinking`에서 Vite dev 서버 실행
2. 5개 페이지(목록/상세/작성/수정/에러) 각각 브라우저로 열어 확인:
   - 노란색 CTA 버튼들의 텍스트 대비가 충분히 읽히는지
   - 페이지 간 버튼 radius/색상이 일관되는지 (특히 수정 페이지가 더 이상 튀지 않는지)
   - 카테고리 색상(녹/오렌지/핑크)이 그대로 보이는지
   - 에러 페이지 진입 확인 (404/500 케이스)
3. 하드코딩된 hex 값이 남아있지 않은지 grep으로 재확인 (`#[0-9a-fA-F]{3,6}` 패턴이 CSS 변수 정의부인 `main.css` `:root` 외에는 없어야 함)
