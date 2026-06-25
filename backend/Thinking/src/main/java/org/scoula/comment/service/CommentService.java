package org.scoula.comment.service;

import org.scoula.comment.dto.CommentDeleteDTO;
import org.scoula.comment.dto.CommentUpdateDTO;

public interface CommentService {
    // 강민주

    // 복원준

    // 이현서 : 수정/삭제
    public boolean updateComment(CommentUpdateDTO comment);

    public boolean deleteComment(CommentDeleteDTO comment);

    // 이현주
}
