package org.scoula.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.comment.dto.CommentUpdateDTO;
import org.scoula.comment.mapper.CommentMapper;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    final private CommentMapper mapper;

    // 강민주

    // 복원준

    // 이현서
    @Override
    public boolean update(CommentUpdateDTO dto) {
        log.info("update : " + dto);
        return mapper.updateComment(dto.toVo()) == 1;
    }

    // 이현주
}
