package org.scoula.thinking.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.scoula.config.RootConfig;
import org.scoula.thinking.dto.ThinkingDTO;
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
}