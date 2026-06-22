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
public class CommentUpdateDTO {
    private long id;
    private String content;
    private String password;

    //VO -> DTO
    public static CommentUpdateDTO of(CommentVO vo){
        return vo == null ? null : CommentUpdateDTO.builder()
                                   .id(vo.getId())
                                   .content(vo.getContent())
                                   .password(vo.getPassword())
                                   .build();
    }

    //DTO -> VO
    public CommentVO toVo(){
        return CommentVO.builder()
                .id(id)
                .content(content)
                .password(password)
                .build();
    }
}
