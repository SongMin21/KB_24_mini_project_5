package org.scoula.comment.service;

import org.scoula.comment.dto.CommentDeleteDTO;
import org.scoula.comment.dto.CommentUpdateDTO;
import org.scoula.comment.dto.CommentDTO;
import org.scoula.comment.dto.CommentCreateDTO;
import java.util.List;

public interface CommentService {
    // 강민주
    public CommentDTO create(CommentCreateDTO dto);
    public CommentDTO get(long id);

    // 복원준

    // 이현서 : 수정/삭제
    public CommentDTO updateComment(CommentUpdateDTO comment);
    // public CommentDeleteDTO deleteComment(CommentDeleteDTO comment);
    public CommentDTO deleteComment(CommentDeleteDTO comment);

    // 이현주
    public List<CommentDTO> selectComment(long thinkingId);
}
