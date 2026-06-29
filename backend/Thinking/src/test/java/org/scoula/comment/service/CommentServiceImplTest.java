package org.scoula.comment.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.scoula.comment.dto.CommentCreateDTO;
import org.scoula.config.RootConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RootConfig.class})
@Log4j2
class CommentServiceImplTest {
    @Autowired
    private CommentService service;

    // 강민주
    @Test
    @DisplayName("comment create service")
    void create() {
        CommentCreateDTO dto = CommentCreateDTO.builder()
                .thinkingId(1L)
                .content("content01")
                .password("12345")
                .build();

        service.create(dto);
        log.info("comment create service");
    }
}