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
    @DisplayName("Thinking mapper getByCategory")
    public  void getByCategory(){
        log.info("get-by-Category test");

        // Learned , Lacked , Good
        String category = "LEARNED";

        // mapper 호출
        List<ThinkingVO> list = mapper.getByCategory(category);

        //검증
        assertNotNull(list);
        assertFalse(list.isEmpty());

        //조회된 결과물 콘솔 확인
        for(ThinkingVO vo : list){
            log.info(vo);
        }


    }
}