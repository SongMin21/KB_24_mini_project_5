package org.scoula.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.comment.domain.CommentVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentCreateDTO {
    private long thinkingId;
    private String content;
    private String password;

    public static CommentCreateDTO of(CommentVO vo) {
        return vo == null ? null : CommentCreateDTO.builder()
                .thinkingId(vo.getThinkingId())
                .content(vo.getContent())
                .password(vo.getPassword())
                .build();
    }

    public CommentVO toVO() {
        return CommentVO.builder()
                .thinkingId(thinkingId)
                .content(content)
                .password(password)
                .build();
    }
}
