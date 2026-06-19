CREATE DATABASE thinking_db;
GRANT ALL PRIVILEGES ON thinking_db.* to 'scoula'@'%';

drop table comment_tbl;
drop table thinking_tbl;

use thinking_db;

-- 날짜별 조회
SELECT * FROM thinking_tbl WHERE DATE(created_at) = '2026-06-18';

-- 게시물 update 쿼리(참고)
-- content, title 등 set 한 뒤 updated_at=now() 추가 필요
UPDATE thinking_tbl
SET updated_at = now()
WHERE id = 1
;