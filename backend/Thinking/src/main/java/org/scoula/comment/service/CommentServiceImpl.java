package org.scoula.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.comment.dto.CommentDTO;
import org.scoula.comment.mapper.CommentMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    final private CommentMapper mapper;
    // 강민주

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
