package org.scoula.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.comment.dto.CommentCreateDTO;
import org.scoula.comment.mapper.CommentMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class CommentServiceImpl implements CommentService{
    final private CommentMapper mapper;

    // 강민주
    @Override
    public void create(CommentCreateDTO dto) {
        log.info("create service");
        // dto의 내용 vo로 변경하여 create 호출
        mapper.create(dto.toVO());
    }

    // 복원준

    // 이현서

    // 이현주
}
