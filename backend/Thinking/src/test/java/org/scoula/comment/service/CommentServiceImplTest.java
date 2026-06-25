package org.scoula.comment.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.scoula.comment.dto.CommentDTO;
import org.scoula.config.RootConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RootConfig.class})
@Log4j2
class CommentServiceImplTest {

    @Autowired
    private CommentService service;

    @Test
    @DisplayName("selectComment service test")
    void selectComment() {
        Long thinkingId = 1L;

        List<CommentDTO> list = service.selectComment(thinkingId);

        assertNotNull(list);

        for (CommentDTO dto : list) {
            assertEquals(thinkingId, dto.getThinkingId());
            log.info(dto);
        }
    }
}
