package org.scoula.thinking.mapper;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.scoula.config.RootConfig;
import org.scoula.thinking.domain.ThinkingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes={RootConfig.class})
@Log4j2
class ThinkingMapperTest {

    @Autowired
    private ThinkingMapper mapper;

    @Test
    @DisplayName("Thinking mapper getList")
    public void getList() {
        log.info("getList test");
        List<ThinkingVO> list = mapper.getList();
        for(ThinkingVO vo : list) {
            System.out.println(vo);
        }
    }

    @Test
    @DisplayName("Thinking mapper updateLike")
    public void updateLike() {
        log.info("update like");
        int cnt = mapper.updateLike(3L);
        log.info("update : " + cnt);
    }
  
    @Test
    @DisplayName("Thinking mapper getByDate")
    public void getByDate() {
        log.info("getByDate test");
        LocalDate localDate = LocalDate.of(2026, 6, 1);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<ThinkingVO> list = mapper.getByDate(date);
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    @DisplayName("Thinking mapper getByLike(Desc)")
    public void getByLike() {
        log.info("getByLike test");

        // 매퍼를 통해 좋아요 내림차순 정렬 리스트 가져오기
        List<ThinkingVO> list = mapper.getByLike();
        // 결과 검증 - 리스트가 비어있지 않은지 확인
        assertNotNull(list);
        assertFalse(list.isEmpty());

    }

}