-- 댓글
CREATE TABLE comment_tbl
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    thinking_id BIGINT       NOT NULL,
    content     TEXT         NOT NULL,
    password    VARCHAR(255) NOT NULL,
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_comment_thinking FOREIGN KEY (thinking_id)
        REFERENCES thinking_tbl (id) ON DELETE CASCADE
);

INSERT INTO comment_tbl (thinking_id, content, password, created_at)
VALUES (1, 'Fetch Join 정말 유용하죠! 저도 덕분에 성능 개선 많이 했습니다.', 'c123', '2026-06-01 10:30:00'),
       (1, '혹시 데이터 뻥튀기 문제는 어떻게 해결하셨나요?', 'c456', '2026-06-01 11:00:00'),
       (2, '비동기는 시각적으로 실행 순서를 그려보면 이해하기 쉬워요. 파이팅!', 'c789', '2026-06-02 15:00:00'),
       (3, '소통이 잘 되는 팀 분위기 부럽습니다 ㅎㅎ', 'comm', '2026-06-03 10:00:00'),
       (4, '실행 계획 볼 줄 아는 개발자 멋집니다.', 'idx1', '2026-06-04 19:00:00'),
       (5, '전역 예외 처리기(@RestControllerAdvice) 도입하시면 깔끔해집니다!', 'advice', '2026-06-05 12:30:00'),
       (7, '도커 한번 쓰면 로컬에 DB 깔기 귀찮아지더라고요 ㅋㅋ 공감합니다.', 'dock', '2026-06-07 14:00:00'),
       (8, '테스트 코드는 미래의 나를 위한 투자입니다 ㅠㅠ 힘내세요!', 'tst1', '2026-06-08 23:00:00'),
       (9, 'CI/CD 구축 축하드려요! 개발 생산성 폭등이네요.', 'cicd', '2026-06-09 18:00:00'),
       (13, 'JWT 만료 시간(Access/Refresh Token) 처리는 어떻게 하셨는지 궁금해요.', 'jwtc', '2026-06-13 21:00:00'),
       (14, '앗.. 깃허브 퍼블릭 레포라면 당장 키 바꾸셔야 합니다! 큰일나요!', 'warn', '2026-06-14 14:30:00'),
       (16, '간혹 POST로 수정하는 분들 계신데, 원칙을 지키는 게 역시 깔끔하네요.', 'rest', '2026-06-16 11:15:00'),
       (17, '컨플릭트 해결하면서 실력이 늘긴 합니다만... 당시엔 지옥이죠.', 'gitc', '2026-06-17 13:00:00'),
       (17, 'Git Flow나 GitHub Flow 꼭 찾아보세요. 무조건 도입 추천합니다!', 'gitc2', '2026-06-17 13:10:00'),
       (18, 'Swagger 최고죠! Postman 따로 안 써도 돼서 너무 편해요.', 'swg1', '2026-06-17 17:00:00');