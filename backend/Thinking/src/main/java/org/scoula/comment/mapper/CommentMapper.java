package org.scoula.comment.mapper;

import org.apache.ibatis.annotations.Param;
import org.scoula.comment.domain.CommentVO;

import java.util.List;

public interface CommentMapper {
    // update comment
    // 💡 VO 객체를 통째로 넘기고, 결과는 성공한 행의 개수(int)를 받습니다.
    public int updateComment(CommentVO comment);

    public List<CommentVO> selectComment(
            @Param("thinkingId") Long thinkingId
    );

    // create comment
    public void create(CommentVO comment);

    // get password
    public String getPassword(long id);

    // delete comment
    public int deleteComment(CommentVO comment);
}