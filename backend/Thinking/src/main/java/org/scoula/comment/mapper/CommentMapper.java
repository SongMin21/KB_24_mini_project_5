package org.scoula.comment.mapper;

import org.apache.ibatis.annotations.Param;
import org.scoula.comment.domain.CommentVO;

public interface CommentMapper {
    // update comment
    // 💡 VO 객체를 통째로 넘기고, 결과는 성공한 행의 개수(int)를 받습니다.
    public int updateComment(CommentVO comment);

    // create comment
    public void create(CommentVO comment);
}