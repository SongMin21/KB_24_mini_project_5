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
    @DisplayName("Thinking mapper update")
    public void update(){
        ThinkingVO thinking = new ThinkingVO();
        //수정사항들 적기
        thinking.setId(1L);
        thinking.setCategory("LEARNED");
        thinking.setTitle("수정된 제목");
        thinking.setContent("수정된 내용");

        int count = mapper.update(thinking);
        log.info("Update count : " + count);
    }

}