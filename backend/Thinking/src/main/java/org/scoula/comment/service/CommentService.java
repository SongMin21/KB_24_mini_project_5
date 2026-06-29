package org.scoula.comment.service;

import org.scoula.comment.dto.CommentDTO;
import org.scoula.comment.dto.CommentCreateDTO;
import java.util.List;

public interface CommentService {
    // 강민주
    public void create(CommentCreateDTO dto);

    // 복원준

    // 이현서

    // 이현주
    public List<CommentDTO> selectComment(long thinkingId);
}
