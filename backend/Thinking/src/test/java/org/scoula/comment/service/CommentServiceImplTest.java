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
    void update() {
        // impl클래스 내에 get 메서드 선언 필요
        CommentUpdateDTO comment = service.get(1L);
        comment.setContent("내용 수정함");
        log.info("update : " + service.update(comment));
    }
}