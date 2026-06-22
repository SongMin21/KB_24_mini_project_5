package org.scoula.comment.mapper;

import org.apache.ibatis.annotations.Param;
import org.scoula.comment.domain.CommentVO;

public interface CommentMapper {
    // 💡 VO 객체를 통째로 넘기고, 결과는 성공한 행의 개수(int)를 받습니다.
    public int updateComment(CommentVO comment);

    // 💡 번호와 내용을 콕 집어서 배달합니다. 성공 개수(int) 리턴!
    // public int updateComment(@Param("id") long id, @Param("content") String content);

    // create comment
    public void create(CommentVO comment);
}