package org.scoula.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.comment.domain.CommentVO;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private long id;
    private long thinkingId;
    private String content;
//    private String password;
    private Date createdAt;
    private Date updatedAt;

    public static CommentDTO of(CommentVO vo) {
        return vo == null ? null : CommentDTO.builder()
                                   .id(vo.getId())
                                   .thinkingId(vo.getThinkingId())
                                   .content(vo.getContent())
                                   .createdAt(vo.getCreatedAt())
                                   .updatedAt(vo.getUpdatedAt())
                                   .build();
    }

    public CommentVO toVo() {
        return CommentVO.builder()
                .id(id)
                .thinkingId(thinkingId)
                .content(content)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
