package org.scoula.comment.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.scoula.comment.dto.CommentUpdateDTO;
import org.scoula.config.RootConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RootConfig.class})
@Log4j2

class CommentServiceImplTest {
    @Autowired
    private CommentService service;
    @Test
    void updateComment() {
        // impl클래스 내에 get 메서드 선언 필요
        CommentUpdateDTO comment = CommentUpdateDTO.builder()
                .id(1L)
                .content("댓글 내용 수정(service)")
                .password("c123")
                .build();

        log.info("update 결과 : " + service.updateComment(comment));
    }
}