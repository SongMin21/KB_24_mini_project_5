package org.scoula.comment.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.scoula.comment.dto.CommentDeleteDTO;
import org.scoula.comment.dto.CommentUpdateDTO;
import org.scoula.comment.dto.CommentCreateDTO;
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

    // 이현주
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

    // 이현서
    @Test
    void updateComment() {
        // impl클래스 내에 get 메서드 선언 필요
        CommentUpdateDTO comment = CommentUpdateDTO.builder()
                .id(1L)
                .content("댓글 내용 수정(controller 구현중)")
                .password("c123")
                .build();

        log.info("update 결과 : " + service.updateComment(comment));
    }

    @Test
    void deleteComment() {
        CommentDeleteDTO comment = CommentDeleteDTO.builder()
                .id(1L)
                .password("c123")
                .build();
//                .id(18L)
//                .password("password")
//                .build();

        log.info("delete 결과: " + service.deleteComment(comment));
//        boolean result = service.deleteComment(comment);
//        log.info("delete 결과 : " + result);
//        assertTrue(result);
    }
}