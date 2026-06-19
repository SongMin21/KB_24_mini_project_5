# 🪞 Thinking - 익명 회고 게시판

> 수업·프로젝트가 끝난 뒤, 배운 것·부족한 것·좋았던 것을 **익명으로** 남기고 공유하는 웹 서비스
>
> Spring Legacy MVC · MyBatis · JSP · MySQL 로 구현한 4인 팀 프로젝트

<br>

## 📌 프로젝트 소개

회원가입 없이 **비밀번호 기반 인증**만으로 누구나 솔직한 회고를 작성하고,
다른 사람의 글에 **공감**을 눌러 좋은 피드백이 자연스럽게 상위에 노출되도록 만든 익명 회고 게시판입니다.

Spring Legacy MVC 패턴의 전체 흐름(**Controller → Service → Mapper → DB**)을 팀원 전원이 직접 구현하며 체득하고,
이후 동일한 비즈니스 로직 위에서 **JSP → REST API 전환**을 진행해 프론트엔드/백엔드 분리 구조까지 학습하는 것을 목표로 합니다.

<br>

## 🛠️ 기술 스택

| 구분 | 기술 |
| --- | --- |
| Language | Java 17 |
| Framework | Spring 5 MVC (5.3.37), MyBatis 3 (mybatis-spring) |
| View | JSP / JSTL |
| Database | MySQL 8 |
| Connection Pool | HikariCP |
| Build / WAS | Gradle (War), Apache Tomcat |
| Logging | Log4j2, log4jdbc |
| Library | Lombok, Jackson |
| Test | JUnit 5 |
| 협업 | Git · GitHub · Notion · Slack |

<br>

## ✨ 주요 기능

### 📝 회고 게시글 작성
- 카테고리(`배운 것` / `부족한 것` / `좋았던 것`)를 선택해 회고를 작성합니다.
- 수정·삭제를 위한 **비밀번호**를 함께 입력합니다.

### 🔍 게시글 조회
- **전체 조회** — 페이지네이션으로 목록을 탐색
- **날짜별 조회** — 특정 날짜의 회고만 필터링
- **카테고리별 조회** — `LEARNED` / `LACKED` / `GOOD` 분류
- **공감순 조회** — 공감 수가 높은 글부터 정렬

### 👍 공감 기능
- 별도 로그인 없이 누구나 공감 버튼을 눌러 공감 수를 +1 증가시킬 수 있습니다.

### 💬 댓글 시스템
- 글 상세 페이지에서 댓글을 작성·수정·삭제할 수 있습니다.
- 댓글도 **비밀번호 기반**으로 수정·삭제 권한을 검증합니다.

### 🔑 비밀번호 기반 인증
- 회원가입 없이 글·댓글 작성 시 비밀번호를 입력하고, 수정·삭제 시 일치 여부를 검증합니다.

<br>

## 🗄️ 데이터베이스 구조

**`thinking_tbl`** — 회고 글

| 컬럼 | 타입 | 설명 |
| --- | --- | --- |
| id | BIGINT (PK, AI) | 글 번호 |
| category | ENUM(`LEARNED`,`LACKED`,`GOOD`) | 카테고리 |
| title | VARCHAR(255) | 제목 |
| content | TEXT | 내용 |
| password | VARCHAR(255) | 수정·삭제용 비밀번호 |
| like_count | INT (default 0) | 공감 수 |
| created_at / updated_at | DATETIME | 작성·수정 일시 |

**`comment_tbl`** — 댓글

| 컬럼 | 타입 | 설명 |
| --- | --- | --- |
| id | BIGINT (PK, AI) | 댓글 번호 |
| thinking_id | BIGINT (FK) | 원본 글 (`ON DELETE CASCADE`) |
| content | TEXT | 댓글 내용 |
| password | VARCHAR(255) | 수정·삭제용 비밀번호 |
| created_at / updated_at | DATETIME | 작성·수정 일시 |

> 글이 삭제되면 해당 글의 댓글도 `ON DELETE CASCADE`로 함께 삭제됩니다.

<br>

## 👥 팀원 및 역할 분담

| 팀원 | JSP 담당 | REST API 담당 |
| --- | --- | --- |
| **공통** | 전체 목록(페이지네이션) | 전체 목록(페이지네이션) |
| **이현주** | 글 상세 + 댓글 조회 / 게시글 작성  | 게시글 작성 · 상세 조회 · 글 상세 |
| **강민주** | 날짜별 목록 / 공감 +1 / 댓글 작성 | 날짜별 목록 · 공감 +1 · 댓글 작성 |
| **복원준** | 카테고리별 목록 / 글 수정 / 글 삭제(비밀번호 검증) | 카테고리별 목록 · 글 수정 · 글 삭제 |
| **이현서** | 공감순 목록 / 댓글 수정 / 댓글 삭제 | 공감순 목록 · 댓글 수정 · 댓글 삭제 |

<br>

## 🚀 실행 방법

**1. 데이터베이스 준비**

```sql
CREATE DATABASE thinking_db;
GRANT ALL PRIVILEGES ON thinking_db.* TO 'scoula'@'%';
```

```bash
# 테이블 생성 + 샘플 데이터 삽입
mysql -u scoula -p thinking_db < thinking.sql
mysql -u scoula -p thinking_db < comments.sql
```

**2. DB 접속 정보 확인** — `src/main/resources/application.properties`

```properties
jdbc.url=jdbc:log4jdbc:mysql://localhost:3306/thinking_db
jdbc.username=[username]
jdbc.password=[db_password]
```

**3. 빌드 & 배포**

```bash
./gradlew build      # WAR 빌드
# 생성된 build/libs/Thinking-1.0-SNAPSHOT.war 를 Tomcat에 배포
```

<br>

## 🤝 협업 방식

- **형상 관리** — Git + GitHub, 기능별 브랜치(`feature/*`) → Pull Request → 코드 리뷰 → `main` merge
- **충돌 방지** — 공유 파일은 담당자별 주석 영역 분리, 뼈대 생성 후 동시 작업
- **커뮤니케이션** — Notion으로 API 명세서·회의록·진행 현황 관리, 매일 데일리 스크럼
- **테스트** — Mapper 단위 테스트(JUnit 5) + Tomcat 배포 후 수동 통합 테스트

<br>

## 🎯 기대 효과

- Spring Legacy MVC 패턴의 전체 흐름과 MyBatis SQL 매핑, JSP 서버 사이드 렌더링을 직접 구현하며 체득
- **JSP → REST API 전환 시 Controller 레이어만 교체**하면 되는 아키텍처의 장점 체감
- Git 브랜치 전략과 PR 기반 협업, API 명세서 기반 역할 분담 등 실무 수준의 팀 개발 경험
- 익명 회고 공유와 공감 기반 정렬로 솔직하고 효율적인 피드백 수집 환경 제공

<br>

---

<p align="center">
  <b>이현주 · 강민주 · 복원준 · 이현서</b><br>
</p>
