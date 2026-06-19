package org.scoula.comment.mapper;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.scoula.comment.domain.CommentVO;
import org.scoula.config.RootConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes={RootConfig.class})
@Log4j2
class CommentMapperTest {
    @Autowired
    private CommentMapper commentMapper;

    @Test
    @DisplayName("Comment Mapper -> updateComment()")
    public void updateComment() {
        log.info("updateComment");
        log.info("--------- VO 기반 수정 테스트 시작");

        CommentVO updateCom = new CommentVO();
        updateCom.setId(1L);
        updateCom.setContent("updateComment test - 수정된 댓글입니다~~ 선플을 달아부아용~~");


        // Builder 사용 버전
        // CommentVO updateCom = CommentVO.builder()
               // .id(1L)                                     // 어떤 댓글을 고칠지 (기준키)
               // .content("updateComment test - 수정 댓글 내용입니다~~ 선플을 달아부아용~~") // 바꿀 내용
               // .build();

        int resultCount = commentMapper.updateComment(updateCom);

        // 결과 검증
        log.info("수정된 행의 개수: " + resultCount);
        Assertions.assertEquals(1, resultCount); // 1건 성공 단언

        log.info("--------- VO 기반 수정 테스트 종료");
    }
}