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

    public  void getbyCategory(){
        log.info("get-by-Category test");
        //여기서 어떻게 짜야하지...?
    }
}