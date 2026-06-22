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

import java.util.List;

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

        // test용 dummy 객체 생성 및 데이터 세팅
        CommentVO updateCom = new CommentVO();
        updateCom.setId(1L);
        updateCom.setPassword("c123");
        updateCom.setContent("updateComment test - 수정된 댓글입니다~~ 선플을 달아부아용~~");


        // Builder 사용 버전
        // CommentVO updateCom = CommentVO.builder()
               // .id(1L)                                     // 어떤 댓글을 고칠지 (기준키)
               // .content("updateComment test - 수정 댓글 내용입니다~~ 선플을 달아부아용~~") // 바꿀 내용
               // .build();

        // 매퍼 실행 및 결과 담기
        int resultCount = commentMapper.updateComment(updateCom);

        // 결과 검증
        log.info("수정된 행의 개수: " + resultCount);
        Assertions.assertEquals(1, resultCount); // 1건 성공 단언

        log.info("--------- VO 기반 수정 테스트 종료");
    }

    @Test
    @DisplayName("create comment")
    public void create() {
        CommentVO comment = CommentVO.builder()
                .thinkingId(1L)
                .content("content01")
                .password("password")
                .build();

        commentMapper.create(comment);
        log.info(comment);
    }

    @Test
    @DisplayName("Comment mapper getPassword")
    public void getPassword() {
        log.info("getPassword Test");
        String password = commentMapper.getPassword(1L);
        log.info("password : " + password);
    }
}
    @Test
    @DisplayName("Comment Mapper -> deleteComment()")
    public void deleteComment() {
        log.info("deleteComment");
        log.info("--------- 삭제 테스트 시작");

        // 기존 DB 데이터(id=3) 삭제 테스트
        // Builder 사용
         CommentVO deleteCom = CommentVO.builder()
             .id(3L)
             .password("c789")
             .build();

        // 매퍼 실행 및 결과 담기
        int resultCount = commentMapper.deleteComment(deleteCom);

        // 결과 검증
        log.info("삭제된 행의 개수: " + resultCount);
        Assertions.assertEquals(1, resultCount); // 1건 성공 단언

        log.info("--------- 삭제 테스트 종료");
    }
}
    @DisplayName("Comment mapper -> selectComment()")
    public void selectComment(){
        log.info("selectComment");

        Long thinkingId = 1L;
        List<CommentVO> list = commentMapper.selectComment(thinkingId);

        assertNotNull(list);

        log.info("조회된 댓글 총 개수: " + list.size());

        for(CommentVO vo : list){
            assertEquals(thinkingId, vo.getThinkingId()); // 조회한 댓글들이 요청한 thinkingId의 댓글인지 확인
            System.out.println("댓글:" + vo);
        }
    }
}
