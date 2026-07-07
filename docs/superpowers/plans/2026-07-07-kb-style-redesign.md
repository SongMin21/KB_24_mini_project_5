# KB 스타일 디자인 통일 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** `frontend/thinking` 프런트엔드의 색상/반경 팔레트를 국민은행(KB) 브랜드 컬러 기준으로 재정의하고, 7개 뷰/컴포넌트 파일에 흩어진 86곳의 하드코딩 hex 색상과 `ThinkingUpdateView.vue`의 어긋난 `border-radius`를 전부 CSS 변수로 통일한다.

**Architecture:** `frontend/thinking/src/assets/main.css`의 `:root`에 새 색상/반경 토큰을 정의하고, 각 `.vue` 파일의 `<style scoped>` 블록에서 하드코딩된 hex/keyword 색상 값을 해당 토큰으로 치환한다. 구조(템플릿/레이아웃)와 카테고리 3색(녹/오렌지/핑크)은 변경하지 않는다.

**Tech Stack:** Vue 3 (`<script setup>`), Vite, scoped CSS, CSS custom properties. 프로젝트에 자동화 테스트 프레임워크가 없으므로 "테스트"는 (1) `grep`으로 하드코딩 hex 잔존 여부 확인, (2) Vite dev 서버 기반 수동 브라우저 확인으로 대체한다.

## Global Constraints

- 레이아웃/구조(템플릿, 와이어프레임) 변경 금지 — 색상/반경 토큰만 교체
- 카테고리 3색(`--color-learned: #4caf50`, `--color-lacked: #ff9800`, `--color-good: #e91e63`) 값 자체는 변경하지 않음, 변수로만 승격
- 폰트 변경 없음 (Pretendard/Noto Sans KR 유지)
- `--radius-base`(12px, 카드용) 값은 변경하지 않음 — 버튼/인풋 반경만 별도 토큰(`--radius-input`, `--radius-pill`)으로 분리
- 노란색(`--color-primary: #FFBC00`) 배경 위 텍스트는 반드시 `--color-on-primary`(다크톤)를 사용 — 흰 텍스트 금지 (대비 부족)
- 작업 완료 기준: `frontend/thinking/src/**/*.vue`와 `main.css` 전체에서 `grep -rn "#[0-9a-fA-F]\{3,6\}"`로 검색했을 때 `main.css`의 `:root` 블록 외에는 매치가 없어야 함
- 참조 스펙: `docs/superpowers/specs/2026-07-07-kb-style-redesign-design.md`

---

### Task 1: 컬러/반경 토큰 재정의 (`main.css`)

**Files:**
- Modify: `frontend/thinking/src/assets/main.css:1-12`

**Interfaces:**
- Produces: 이후 모든 태스크가 참조하는 CSS 변수 전체 목록 (아래 최종 `:root` 참고). 특히 `--color-on-primary`(노란 배경 위 텍스트), `--color-on-accent`(채도 있는 배경/어두운 배경 위 흰 텍스트), `--color-danger`/`--color-danger-bg`(에러 텍스트/배경), `--color-disabled`(비활성 버튼), `--radius-input`(8px), `--radius-pill`(999px)는 스펙에 없던 신규 토큰이므로 이후 태스크에서 이 정확한 이름을 사용한다.

- [ ] **Step 1: `:root` 블록을 새 토큰으로 교체**

`frontend/thinking/src/assets/main.css`의 기존 코드:

```css
:root {
  --color-primary: #4a90d9;
  --color-learned: #4caf50;
  --color-lacked: #ff9800;
  --color-good: #e91e63;
  --color-background: #f5f5f5;
  --color-card: #ffffff;
  --color-text-primary: #212121;
  --color-text-secondary: #757575;
  --color-border: #e0e0e0;
  --radius-base: 12px;
}
```

다음으로 교체:

```css
:root {
  /* KB Main Color */
  --color-primary: #ffbc00;
  --color-primary-hover: #e6a800;
  --color-on-primary: #2b2313;

  /* 채도 있는 배경(카테고리 색, 다크 뉴트럴 버튼) 위에 올라가는 흰 텍스트 */
  --color-on-accent: #ffffff;

  /* 카테고리 구분색 (기존 값 유지, 변수로 승격) */
  --color-learned: #4caf50;
  --color-lacked: #ff9800;
  --color-good: #e91e63;

  /* KB Gray 계열 */
  --color-background: #f7f5f0;
  --color-card: #ffffff;
  --color-text-primary: #545045;
  --color-text-secondary: #60584c;
  --color-border: #e6e1d6;

  /* 의미 색상 (브랜드색 아님, 범용 에러/비활성 표시) */
  --color-danger: #d32f2f;
  --color-danger-bg: #fdecea;
  --color-disabled: #c9c2b0;

  /* 반경 */
  --radius-base: 12px;
  --radius-input: 8px;
  --radius-pill: 999px;
}
```

- [ ] **Step 2: 검증**

Run: `grep -n "^  --color\|^  --radius" "frontend/thinking/src/assets/main.css"`

Expected: 위 18개 변수 선언이 모두 출력됨 (오탈자 없이 정확한 이름으로).

- [ ] **Step 3: Commit**

```bash
git add frontend/thinking/src/assets/main.css
git commit -m "style: KB 브랜드 컬러 기준으로 CSS 변수 토큰 재정의"
```

---

### Task 2: Header 컴포넌트 색상 정리

**Files:**
- Modify: `frontend/thinking/src/components/Header.vue:52-65`

**Interfaces:**
- Consumes: `--color-primary`, `--color-primary-hover`, `--color-on-primary`, `--radius-pill` (Task 1에서 정의)

- [ ] **Step 1: `.write-btn` 스타일 교체**

기존 코드 (`Header.vue:52-65`):

```css
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
```

다음으로 교체:

```css
.write-btn {
  border: none;
  border-radius: var(--radius-pill);
  padding: 8px 18px;
  background: var(--color-primary);
  color: var(--color-on-primary);
  font-size: 14px;
  font-weight: 600;
  transition: background-color 0.15s ease;
}

.write-btn:hover {
  background-color: var(--color-primary-hover);
}
```

- [ ] **Step 2: 검증**

Run: `grep -n "#[0-9a-fA-F]\{3,6\}" "frontend/thinking/src/components/Header.vue"`

Expected: 아무 출력 없음 (매치 0건)

- [ ] **Step 3: Commit**

```bash
git add frontend/thinking/src/components/Header.vue
git commit -m "style: 헤더 글쓰기 버튼을 KB Yellow 토큰으로 교체"
```

---

### Task 3: 목록 페이지 색상 정리 (`ThinkingListView.vue`)

**Files:**
- Modify: `frontend/thinking/src/views/ThinkingListView.vue:30-34` (스크립트, `categoryColorMap`)
- Modify: `frontend/thinking/src/views/ThinkingListView.vue:263` (템플릿, fallback 색상)
- Modify: `frontend/thinking/src/views/ThinkingListView.vue:292-544` (`<style scoped>`)

**Interfaces:**
- Consumes: `--color-primary`, `--color-on-primary`, `--color-card`, `--color-text-primary`, `--color-text-secondary`, `--color-border`, `--color-danger`, `--radius-input`, `--color-learned`, `--color-lacked`, `--color-good` (Task 1)

- [ ] **Step 1: `categoryColorMap`과 fallback 색상을 CSS 변수 문자열로 교체**

기존 코드 (`ThinkingListView.vue:30-34`):

```js
const categoryColorMap = {
  LEARNED: '#4CAF50',
  LACKED: '#FF9800',
  GOOD: '#E91E63',
}
```

다음으로 교체:

```js
const categoryColorMap = {
  LEARNED: 'var(--color-learned)',
  LACKED: 'var(--color-lacked)',
  GOOD: 'var(--color-good)',
}
```

기존 코드 (`ThinkingListView.vue:263`):

```js
            :style="{ backgroundColor: categoryColorMap[item.category] || '#4A90D9' }"
```

다음으로 교체:

```js
            :style="{ backgroundColor: categoryColorMap[item.category] || 'var(--color-primary)' }"
```

- [ ] **Step 2: `.thinking-list-page` 텍스트 색상 교체**

기존:
```css
.thinking-list-page {
  width: min(1080px, calc(100% - 32px));
  margin: 0 auto;
  padding: 56px 0 72px;
  color: #20242a;
}
```
교체:
```css
.thinking-list-page {
  width: min(1080px, calc(100% - 32px));
  margin: 0 auto;
  padding: 56px 0 72px;
  color: var(--color-text-primary);
}
```

- [ ] **Step 3: 탭/필터/페이지네이션 버튼 공통 스타일 교체**

기존:
```css
.tab-button,
.category-filter-button,
.search-button,
.page-button {
  border: 1px solid #d9e2ec;
  border-radius: 8px;
  background: #fff;
  color: #374151;
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
  border-color: #4a90d9;
  color: #4a90d9;
}

.tab-button.active,
.category-filter-button.active,
.page-button.active {
  border-color: #4a90d9;
  background: #4a90d9;
  color: #fff;
}
```
교체:
```css
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
```

> 참고: hover 텍스트를 `var(--color-primary)`(노란색) 그대로 쓰면 흰 배경 위에서 대비가 너무 낮아 안 보인다. 대신 `--color-text-primary`로 강조하고, 테두리만 노란색으로 포커스를 준다.

- [ ] **Step 4: 날짜 인풋 / 조회 버튼 교체**

기존:
```css
.date-input {
  min-height: 42px;
  border: 1px solid #d9e2ec;
  border-radius: 8px;
  color: #20242a;
  font-size: 14px;
  padding: 0 14px;
}

.search-button {
  background: #20242a;
  border-color: #20242a;
  color: #fff;
}

.search-button:hover {
  background: #343b44;
  border-color: #343b44;
  color: #fff;
}
```
교체:
```css
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
```

- [ ] **Step 5: 에러/로딩 메시지 색상 교체**

기존:
```css
.error-message {
  color: #d93025;
}

.loading-message {
  color: #4a5568;
}
```
교체:
```css
.error-message {
  color: var(--color-danger);
}

.loading-message {
  color: var(--color-text-secondary);
}
```

- [ ] **Step 6: 게시글 카드 색상 교체**

기존:
```css
.thinking-card {
  min-height: 92px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  border: 1px solid #e5eaf0;
  border-radius: 8px;
  background: #fff;
  padding: 20px;
  box-shadow: 0 6px 18px rgba(20, 37, 63, 0.06);
  cursor: pointer;
  transition:
    box-shadow 0.2s ease,
    transform 0.2s ease,
    border-color 0.2s ease;
}

.thinking-card:hover,
.thinking-card:focus {
  border-color: #c7d8ec;
  box-shadow: 0 12px 28px rgba(20, 37, 63, 0.14);
  outline: none;
  transform: translateY(-3px);
}
```
교체:
```css
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
```

- [ ] **Step 7: 카테고리 뱃지 / 날짜 / 제목 / 메타 텍스트 색상 교체**

기존:
```css
.category-badge {
  display: inline-flex;
  align-items: center;
  min-height: 28px;
  border-radius: 999px;
  color: #fff;
  font-size: 12px;
  font-weight: 800;
  padding: 0 10px;
  white-space: nowrap;
}

.created-date {
  margin-left: auto;
  color: #6b7280;
  font-size: 13px;
  white-space: nowrap;
}

.card-title {
  flex: 1;
  margin: 0;
  color: #20242a;
  font-size: 16px;
  font-weight: 800;
  line-height: 1.45;
  word-break: keep-all;
  overflow-wrap: anywhere;
}

.card-meta {
  display: flex;
  gap: 16px;
  color: #4b5563;
  font-size: 14px;
  font-weight: 700;
}
```
교체:
```css
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
```

- [ ] **Step 8: 빈 상태(empty state) 색상 교체**

기존:
```css
.empty-state {
  min-height: 240px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 18px;
  border: 1px dashed #c8d5e2;
  border-radius: 8px;
  background: #f8fbfe;
  text-align: center;
  padding: 36px 18px;
}

.empty-state p {
  margin: 0;
  color: #4b5563;
  font-size: 16px;
  font-weight: 700;
}
```
교체:
```css
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
```

- [ ] **Step 9: 검증**

Run: `grep -n "#[0-9a-fA-F]\{3,6\}" "frontend/thinking/src/views/ThinkingListView.vue"`

Expected: 아무 출력 없음 (매치 0건)

- [ ] **Step 10: Commit**

```bash
git add frontend/thinking/src/views/ThinkingListView.vue
git commit -m "style: 목록 페이지 색상을 KB 토큰으로 교체"
```

---

### Task 4: 상세 페이지 색상 정리 (`ThinkingDetailView.vue`)

**Files:**
- Modify: `frontend/thinking/src/views/ThinkingDetailView.vue:359-458`, `:576-580`

**Interfaces:**
- Consumes: `--color-on-accent`, `--color-danger`, `--color-card`, `--color-on-primary` (Task 1)

- [ ] **Step 1: 카테고리 뱃지 텍스트 색상 교체**

기존 (`ThinkingDetailView.vue:359-366`):
```css
.badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 999px;
  color: #fff;
  font-size: 13px;
  font-weight: 600;
}
```
교체:
```css
.badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 999px;
  color: var(--color-on-accent);
  font-size: 13px;
  font-weight: 600;
}
```

- [ ] **Step 2: 공감 버튼 flash 색상 교체**

기존 (`ThinkingDetailView.vue:407-409`):
```css
.like-btn.flash {
  color: #e53935;
}
```
교체:
```css
.like-btn.flash {
  color: var(--color-danger);
}
```

- [ ] **Step 3: 액션 버튼 공통/제출 버튼 색상 교체**

기존 (`ThinkingDetailView.vue:437-458`):
```css
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
```
교체:
```css
.edit-btn,
.delete-btn,
.cancel-btn,
.save-btn,
.confirm-btn,
.submit-btn {
  border: 1px solid var(--color-border);
  background: var(--color-card);
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
  color: var(--color-on-primary);
  font-weight: 600;
}
```

- [ ] **Step 4: 모달 에러 텍스트 색상 교체**

기존 (`ThinkingDetailView.vue:576-580`):
```css
.modal-error {
  color: #e53935;
  font-size: 13px;
  margin: 8px 0 0;
}
```
교체:
```css
.modal-error {
  color: var(--color-danger);
  font-size: 13px;
  margin: 8px 0 0;
}
```

- [ ] **Step 5: 검증**

Run: `grep -n "#[0-9a-fA-F]\{3,6\}" "frontend/thinking/src/views/ThinkingDetailView.vue"`

Expected: 아무 출력 없음 (매치 0건)

- [ ] **Step 6: Commit**

```bash
git add frontend/thinking/src/views/ThinkingDetailView.vue
git commit -m "style: 상세 페이지 색상을 KB 토큰으로 교체"
```

---

### Task 5: 작성 페이지 색상 정리 (`ThinkingCreateView.vue`)

**Files:**
- Modify: `frontend/thinking/src/views/ThinkingCreateView.vue:212-397`

**Interfaces:**
- Consumes: `--color-danger`, `--color-danger-bg`, `--color-card`, `--color-on-accent`, `--color-on-primary`, `--color-primary-hover`, `--color-disabled`, `--color-border` (Task 1)

- [ ] **Step 1: 에러 배너 색상 교체**

기존 (`ThinkingCreateView.vue:212-220`):
```css
.error-banner {
  background-color: #ffebee;
  color: #c62828;
  padding: 12px 16px;
  border-radius: 8px;
  margin-bottom: 24px;
  font-size: 14px;
  border-left: 4px solid #e53935;
}
```
교체:
```css
.error-banner {
  background-color: var(--color-danger-bg);
  color: var(--color-danger);
  padding: 12px 16px;
  border-radius: 8px;
  margin-bottom: 24px;
  font-size: 14px;
  border-left: 4px solid var(--color-danger);
}
```

- [ ] **Step 2: 필수 표시(`*`) 색상 교체**

기존 (`ThinkingCreateView.vue:244-247`):
```css
.required {
  color: #e53935;
  margin-left: 2px;
}
```
교체:
```css
.required {
  color: var(--color-danger);
  margin-left: 2px;
}
```

- [ ] **Step 3: 카테고리 토글 버튼 기본/활성 색상 교체**

기존 (`ThinkingCreateView.vue:255-267`, `:292-309`):
```css
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
```
```css
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
```
교체:
```css
.category-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
  background: var(--color-card);
  border: 1px solid var(--color-border);
  color: var(--color-text-secondary);
  transition: all 0.2s ease;
}
```
```css
/* Active Category Styles */
.category-btn.learned.active {
  background: var(--color-learned);
  border-color: var(--color-learned);
  color: var(--color-on-accent);
}

.category-btn.lacked.active {
  background: var(--color-lacked);
  border-color: var(--color-lacked);
  color: var(--color-on-accent);
}

.category-btn.good.active {
  background: var(--color-good);
  border-color: var(--color-good);
  color: var(--color-on-accent);
}
```

- [ ] **Step 4: 인풋/텍스트에어리어 배경 교체**

기존 (`ThinkingCreateView.vue:312-322`, `:328-342`):
```css
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
```
```css
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
```
교체: 위 두 블록에서 `background-color: #ffffff;`를 각각 `background-color: var(--color-card);`로 변경 (다른 속성은 그대로 유지).

- [ ] **Step 5: 취소/제출 버튼 색상 교체**

기존 (`ThinkingCreateView.vue:358-397`):
```css
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
```
교체:
```css
.btn-cancel {
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 500;
  background: var(--color-card);
  border: 1px solid var(--color-border);
  color: var(--color-text-secondary);
  transition: all 0.2s ease;
}

.btn-cancel:hover {
  background: var(--color-background);
  border-color: var(--color-border);
  color: var(--color-text-primary);
}

.btn-submit {
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 500;
  background: var(--color-primary);
  border: 1px solid var(--color-primary);
  color: var(--color-on-primary);
  transition: all 0.2s ease;
}

.btn-submit:hover:not(:disabled) {
  background: var(--color-primary-hover);
  border-color: var(--color-primary-hover);
  box-shadow: 0 2px 8px rgba(255, 188, 0, 0.3);
}

.btn-submit:disabled {
  background: var(--color-disabled);
  border-color: var(--color-disabled);
  cursor: not-allowed;
  color: var(--color-on-accent);
}
```

- [ ] **Step 6: 검증**

Run: `grep -n "#[0-9a-fA-F]\{3,6\}" "frontend/thinking/src/views/ThinkingCreateView.vue"`

Expected: 아무 출력 없음 (매치 0건)

- [ ] **Step 7: Commit**

```bash
git add frontend/thinking/src/views/ThinkingCreateView.vue
git commit -m "style: 작성 페이지 색상을 KB 토큰으로 교체"
```

---

### Task 6: 수정 페이지 색상 및 반경 통일 (`ThinkingUpdateView.vue`)

이 파일은 다른 페이지와 달리 `border-radius: 4px`를 쓰고 자체 블루(`#2196f3`)를 하드코딩하고 있어, 색상 교체와 함께 반경도 다른 페이지 컨벤션(`--radius-input`, `--radius-pill`)에 맞춘다. 카테고리 active 스타일도 Create 페이지처럼 "카테고리색 배경 + 흰 텍스트"로 통일한다 (현재는 옅은 파스텔 배경 + 색 테두리로 Create와 다른 스타일).

**Files:**
- Modify: `frontend/thinking/src/views/ThinkingUpdateView.vue:213-271`

**Interfaces:**
- Consumes: `--color-border`, `--color-card`, `--color-on-accent`, `--color-primary`, `--color-on-primary`, `--color-disabled`, `--color-danger`, `--color-text-secondary`, `--radius-input`, `--radius-pill`, `--color-learned`, `--color-lacked`, `--color-good` (Task 1)

- [ ] **Step 1: 카테고리 버튼 기본 스타일 (반경 통일)**

기존 (`ThinkingUpdateView.vue:213-219`):
```css
.category-btn {
  padding: 10px 16px;
  border: 1px solid #ccc;
  background-color: #fff;
  cursor: pointer;
  border-radius: 4px;
}
```
교체:
```css
.category-btn {
  padding: 10px 16px;
  border: 1px solid var(--color-border);
  background-color: var(--color-card);
  cursor: pointer;
  border-radius: var(--radius-pill);
}
```

- [ ] **Step 2: 카테고리 active 스타일을 Create 페이지와 동일하게 통일**

기존 (`ThinkingUpdateView.vue:221-224`):
```css
/* design.md 카테고리별 색상 스펙 예시 매핑 */
.category-btn.active.learned { background-color: #e3f2fd; border-color: #2196f3; color: #1565c0; }
.category-btn.active.lacked { background-color: #ffebee; border-color: #f44336; color: #c62828; }
.category-btn.active.good { background-color: #e8f5e9; border-color: #4caf50; color: #2e7d32; }
```
교체:
```css
.category-btn.active.learned {
  background-color: var(--color-learned);
  border-color: var(--color-learned);
  color: var(--color-on-accent);
}
.category-btn.active.lacked {
  background-color: var(--color-lacked);
  border-color: var(--color-lacked);
  color: var(--color-on-accent);
}
.category-btn.active.good {
  background-color: var(--color-good);
  border-color: var(--color-good);
  color: var(--color-on-accent);
}
```

- [ ] **Step 3: 인풋/텍스트에어리어 스타일 (반경 통일)**

기존 (`ThinkingUpdateView.vue:226-232`):
```css
.form-input, .form-textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}
```
교체:
```css
.form-input, .form-textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-input);
  box-sizing: border-box;
}
```

- [ ] **Step 4: 버튼 공통/취소/제출 스타일 (색상 + 반경 통일)**

기존 (`ThinkingUpdateView.vue:240-259`):
```css
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
```
교체:
```css
.btn {
  padding: 10px 20px;
  border: 1px solid transparent;
  border-radius: var(--radius-pill);
  cursor: pointer;
}

.btn-cancel {
  background-color: var(--color-card);
  border-color: var(--color-border);
  color: var(--color-text-secondary);
}

.btn-submit {
  background-color: var(--color-primary);
  color: var(--color-on-primary);
}

.btn-submit:disabled {
  background-color: var(--color-disabled);
  color: var(--color-on-accent);
  cursor: not-allowed;
}
```

> 참고: `.btn`에 `border: none` 대신 `border: 1px solid transparent`를 둔 이유는 `.btn-cancel`이 다른 페이지(Create)처럼 테두리를 가지도록 하기 위함이다 (레이아웃 시프트 없이 테두리 추가).

- [ ] **Step 5: 에러 메시지 / 로딩 상태 텍스트 색상 교체**

기존 (`ThinkingUpdateView.vue:261-271`):
```css
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
```
교체:
```css
.error-message {
  color: var(--color-danger);
  font-size: 14px;
  margin-top: 6px;
}

.loading-state {
  text-align: center;
  padding: 4px 0;
  color: var(--color-text-secondary);
}
```

- [ ] **Step 6: 안내 주석 정리**

기존 (`ThinkingUpdateView.vue:188-191`):
```css
<style scoped>
/* 아래 스타일은 가이드라인의 구조 이해를 돕기 위한 예시 코드입니다. 
  실제 레이아웃과 색상은 design.md 스펙에 맞춰 수정하여 사용하세요.
*/
```
교체 (이미 design.md 스펙에 맞춰 정리했으므로 안내 주석 제거):
```css
<style scoped>
```

- [ ] **Step 7: 검증**

Run: `grep -n "#[0-9a-fA-F]\{3,6\}\|border-radius: 4px" "frontend/thinking/src/views/ThinkingUpdateView.vue"`

Expected: 아무 출력 없음 (매치 0건)

- [ ] **Step 8: Commit**

```bash
git add frontend/thinking/src/views/ThinkingUpdateView.vue
git commit -m "style: 수정 페이지 색상/반경을 다른 페이지 컨벤션에 맞춰 통일"
```

---

### Task 7: 에러 페이지 색상 정리 (`ErrorView.vue`)

**Files:**
- Modify: `frontend/thinking/src/views/ErrorView.vue:69-125`

**Interfaces:**
- Consumes: `--color-border`, `--color-card`, `--color-text-primary`, `--color-text-secondary`, `--color-primary`, `--color-primary-hover`, `--color-on-primary` (Task 1)

- [ ] **Step 1: 에러 카드 배경/테두리 교체**

기존 (`ErrorView.vue:69-81`):
```css
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
```
교체:
```css
.error-card {
  width: min(480px, 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  border: 1px solid var(--color-border);
  border-radius: 12px;
  background: var(--color-card);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.06);
  padding: 48px 32px;
  text-align: center;
}
```

- [ ] **Step 2: 상태 코드 / 제목 / 설명 텍스트 색상 교체**

기존 (`ErrorView.vue:89-109`):
```css
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
```
교체:
```css
.error-status {
  margin: 0;
  color: var(--color-border);
  font-size: 15px;
  font-weight: 800;
  letter-spacing: 0.05em;
}

.error-title {
  margin: 0;
  color: var(--color-text-primary);
  font-size: 22px;
  font-weight: 800;
}

.error-description {
  margin: 0;
  color: var(--color-text-secondary);
  font-size: 15px;
  line-height: 1.6;
}
```

- [ ] **Step 3: "목록으로 돌아가기" 버튼 색상 교체**

기존 (`ErrorView.vue:111-125`):
```css
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
```
교체:
```css
.back-button {
  margin-top: 12px;
  border: none;
  border-radius: 999px;
  padding: 12px 28px;
  background: var(--color-primary);
  color: var(--color-on-primary);
  font-size: 14px;
  font-weight: 700;
  transition: background-color 0.15s ease;
}

.back-button:hover {
  background-color: var(--color-primary-hover);
}
```

- [ ] **Step 4: 검증**

Run: `grep -n "#[0-9a-fA-F]\{3,6\}" "frontend/thinking/src/views/ErrorView.vue"`

Expected: 아무 출력 없음 (매치 0건)

- [ ] **Step 5: Commit**

```bash
git add frontend/thinking/src/views/ErrorView.vue
git commit -m "style: 에러 페이지 색상을 KB 토큰으로 교체"
```

---

### Task 8: `frontend/design.md` 문서 갱신

**Files:**
- Modify: `frontend/design.md:9-24`

**Interfaces:**
- Consumes: 없음 (문서만 갱신)

- [ ] **Step 1: 컬러 팔레트 섹션 교체**

기존 (`frontend/design.md:9-24`):
```markdown
## 디자인 방향

- **톤**: 따뜻하고 편안한 분위기. 익명이지만 진솔한 회고를 유도하는 느낌.
- **컬러 팔레트**:
  - Primary: #4A90D9 (차분한 블루)
  - 배운 것(LEARNED): #4CAF50 (그린)
  - 부족한 것(LACKED): #FF9800 (오렌지)
  - 좋았던 것(GOOD): #E91E63 (핑크)
  - Background: #F5F5F5 (라이트 그레이)
  - Card: #FFFFFF
  - Text Primary: #212121
  - Text Secondary: #757575
- **폰트**: Pretendard 또는 Noto Sans KR
- **스타일**: 카드 기반 레이아웃, 둥근 모서리(border-radius: 12px), 부드러운 그림자
- **반응형**: 데스크톱 기준 max-width 960px 중앙 정렬
```
교체:
```markdown
## 디자인 방향

- **톤**: KB 브랜드 아이덴티티를 반영한 신뢰감 있고 따뜻한 분위기.
- **컬러 팔레트** (KB 브랜드 컬러 기준):
  - Primary: #FFBC00 (KB Yellow Positive) — 주요 버튼, 선택된 탭, CTA
  - Primary Hover: #E6A800
  - On Primary(노란 배경 위 텍스트): #2B2313
  - 배운 것(LEARNED): #4CAF50 (그린, 유지)
  - 부족한 것(LACKED): #FF9800 (오렌지, 유지)
  - 좋았던 것(GOOD): #E91E63 (핑크, 유지)
  - Background: #F7F5F0
  - Card: #FFFFFF
  - Text Primary: #545045 (KB Dark Gray)
  - Text Secondary: #60584C (KB Gray)
  - Border: #E6E1D6
  - Danger: #D32F2F
- **폰트**: Pretendard 또는 Noto Sans KR
- **스타일**: 카드 기반 레이아웃, 둥근 모서리(카드 12px / 인풋·일반버튼 8px / 알약형 버튼 999px), 부드러운 그림자
- **헤더**: 흰 배경 유지, "글쓰기" CTA 버튼만 KB Yellow로 강조
- **반응형**: 데스크톱 기준 max-width 960px 중앙 정렬
```

- [ ] **Step 2: Commit**

```bash
git add frontend/design.md
git commit -m "docs: design.md 컬러 팔레트를 KB 브랜드 컬러로 갱신"
```

---

### Task 9: 전체 검증

**Files:** 없음 (읽기/실행만)

**Interfaces:**
- Consumes: Task 1~8의 모든 변경사항

- [ ] **Step 1: 하드코딩 hex 잔존 여부 전체 검증**

Run: `grep -rn "#[0-9a-fA-F]\{3,6\}" "frontend/thinking/src"`

Expected: `frontend/thinking/src/assets/main.css`의 `:root` 블록(18줄)만 출력되고, 다른 `.vue` 파일에서는 아무 매치도 없어야 함.

- [ ] **Step 2: Vite dev 서버 실행**

Run: `cd frontend/thinking && npm run dev`

Expected: 콘솔에 `Local: http://localhost:5173/` (포트는 환경에 따라 다를 수 있음) 형태로 서버 기동 로그 출력.

- [ ] **Step 3: 브라우저로 5개 페이지 순회 확인**

아래 항목을 각 페이지에서 육안으로 확인한다:

- `/thinking/list`: 헤더 "글쓰기" 버튼이 노란 배경 + 다크 텍스트인지, 필터 탭 선택 시 노란 배경 + 다크 텍스트인지, 카테고리 뱃지가 기존 녹/오렌지/핑크 그대로인지
- `/thinking/create`: 카테고리 선택 버튼(녹/오렌지/핑크)이 그대로인지, "작성하기" 버튼이 노란 배경 + 다크 텍스트인지
- `/thinking/{id}` (상세): "댓글 작성" 버튼이 노란 배경 + 다크 텍스트인지, 카테고리 뱃지 텍스트가 흰색으로 잘 보이는지
- `/thinking/update/{id}` (수정): 카테고리 active 버튼이 Create 페이지와 동일한 스타일(색상 배경 + 흰 텍스트)로 보이는지, 버튼/인풋 모서리가 다른 페이지와 동일하게 둥근지 (더 이상 각지지 않은지)
- `/error?status=404`: "목록으로 돌아가기" 버튼이 노란 배경 + 다크 텍스트인지

- [ ] **Step 4: 최종 커밋 여부 확인**

Run: `git log --oneline -9`

Expected: Task 1~8의 8개 커밋(main.css, Header, List, Detail, Create, Update, Error, design.md)이 순서대로 보임.
