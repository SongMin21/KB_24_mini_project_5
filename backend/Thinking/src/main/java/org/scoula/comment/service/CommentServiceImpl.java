package org.scoula.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.comment.dto.CommentDTO;
import org.scoula.comment.dto.CommentCreateDTO;
import org.scoula.comment.mapper.CommentMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    final private CommentMapper mapper;

    // 강민주
    @Override
    public void create(CommentCreateDTO dto) {
        log.info("create service");
        // dto의 내용 vo로 변경하여 create 호출
        if (dto == null ||
                dto.getContent() == null || dto.getContent().trim().isEmpty() ||
                dto.getPassword() == null || dto.getPassword().trim().isEmpty()
        ) {
            log.warn("create service 실패");
            throw new IllegalArgumentException("내용은 필수입니다.");
        }
        mapper.create(dto.toVO());
    }

    // 복원준

    // 이현서

    // 이현주
    @Override
    public List<CommentDTO> selectComment(long thinkingId) {
        log.info("getComment..........");

        return mapper.selectComment(thinkingId).stream()
                .map(CommentDTO::of)
                .toList();
    }
}
