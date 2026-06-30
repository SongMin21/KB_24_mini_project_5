package org.scoula.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.comment.domain.CommentVO;
import org.scoula.comment.dto.CommentDeleteDTO;
import org.scoula.comment.dto.CommentUpdateDTO;
import org.scoula.comment.dto.CommentDTO;
import org.scoula.comment.dto.CommentCreateDTO;
import org.scoula.comment.mapper.CommentMapper;
import org.springframework.stereotype.Service;

import javax.xml.stream.events.Comment;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    final private CommentMapper mapper;

    // 강민주
    @Override
    public CommentDTO create(CommentCreateDTO dto) {
        log.info("create service");
        // dto의 내용 vo로 변경하여 create 호출
        if (dto == null ||
                dto.getContent() == null || dto.getContent().trim().isEmpty() ||
                dto.getPassword() == null || dto.getPassword().trim().isEmpty()
        ) {
            log.warn("create service 실패");
            throw new IllegalArgumentException("내용은 필수입니다.");
        }
        CommentVO vo = dto.toVO();
        mapper.create(vo);
        return get(vo.getId());
    }

    public CommentDTO get(long id) {
        if(id <= 0) {
            throw new IllegalArgumentException("id 값이 음수입니다.");
        }
        CommentDTO dto = CommentDTO.of(mapper.get(id));
        return dto;
    }

    // 복원준

    // 이현서 : 수정/삭제
    @Override
//    public boolean updateComment(CommentUpdateDTO comment) {
    public CommentDTO updateComment(CommentUpdateDTO dto) {
        log.info("update : " + dto);

        // 1. 매퍼를 통해 DB의 비번 꺼내옴
        String realPassword = mapper.getPassword(dto.getId());

        // 2. 비밀번호 검사
         // a. DB 비번이 null일 때
        if (realPassword == null) {
            log.warn("update comment 실패 : 존재하지 않거나 이미 삭제된 댓글");

            // 유효하지 않은 인자값 예외
            throw new IllegalArgumentException("존재하지 않거나 이미 삭제된 댓글입니다.");
        }

         // b. 사용자 입력 비번과 DB 비번 불일치
        if (!realPassword.equals(dto.getPassword())) {
            log.warn("update comment 실패 : 비밀번호 불일치");

            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        // 3. 비밀번호 일치할 때만 매퍼 가동시켜서 수정 처리
        mapper.updateComment(dto.toVo());
        log.info("update comment 완료");

        // return mapper.updateComment(comment.toVo()) == 1;
        // dto를 return
        return get(dto.getId());
    }

    @Override
    public CommentDTO deleteComment(CommentDeleteDTO dto) {
        log.info("delete : " + dto);

        String realPassword = mapper.getPassword(dto.getId());

        if (realPassword == null) {
            log.warn("delete comment 실패 : 존재하지 않거나 이미 삭제된 댓글");
            throw new IllegalArgumentException("존재하지 않거나 이미 삭제된 댓글입니다.");
        }
        if (!realPassword.equals(dto.getPassword())) {
            log.warn("delete comment 실패 : 비밀번호 불일치");
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 삭제 전에 꺼내두기
        CommentDTO deletedComment = get(dto.getId());

        // 삭제 진행
        mapper.deleteComment(dto.toVo());
        log.info("[Comment Delete Success] 댓글 삭제 완료 - ID: {}", dto.getId());

        return deletedComment;
    }

    // 이현주
    @Override
    public List<CommentDTO> selectComment(long thinkingId) {
        log.info("getComment..........");

        return mapper.selectComment(thinkingId).stream()
                .map(CommentDTO::of)
                .toList();
    }
}
