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
public class CommentDeleteDTO {
    private long id;
    private String password;

    // VO -> DTO
    public static CommentDeleteDTO of(CommentVO vo){
        return vo == null ? null : CommentDeleteDTO.builder()
                                   .id(vo.getId())
                                   .password(vo.getPassword())
                                   .build();
    }

    // DTO -> VO
    public CommentVO toVo(){
        return CommentVO.builder()
                .id(id)
                .password(password)
                .build();
    }
}
