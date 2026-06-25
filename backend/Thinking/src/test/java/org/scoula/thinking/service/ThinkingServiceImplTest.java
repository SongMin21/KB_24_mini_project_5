package org.scoula.thinking.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.scoula.config.RootConfig;
import org.scoula.thinking.dto.ThinkingDTO;
import org.scoula.thinking.dto.ThinkingUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.*;

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes={RootConfig.class})
@Log4j2
class ThinkingServiceImplTest {

    @Autowired
    private ThinkingService service;

    @Test
    @DisplayName("getThinking service test")
    void getThinking() {
        log.info("getThinking service test");
        List<ThinkingDTO> list = service.getThinking();
        for(ThinkingDTO dto : list) {
            log.info(dto);
        }
    }

    // 복원준
    // updateThinking test
    @Test
    @DisplayName("updateThinking service test")
    void updateThinking() {
        log.info("updateThinking service test");
        ThinkingUpdateDTO dto = ThinkingUpdateDTO.builder()
                .id(1L)
                .category("LEARNED")
                .title("[수정]제목")
                .content("[수정]내용")
                .password("1234")
                .build();

        log.info("update 성공 여부 : " + service.updateThinking(dto));
    }
}