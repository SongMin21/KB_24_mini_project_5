package org.scoula.thinking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.thinking.domain.ThinkingVO;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThinkingUpdateDTO {
    private long id;
    private String category;
    private String title;
    private String content;
    private String password;

    public static ThinkingUpdateDTO of(ThinkingVO vo) {
        return vo == null ? null : ThinkingUpdateDTO.builder()
                                   .id(vo.getId())
                                   .category(vo.getCategory())
                                   .title(vo.getTitle())
                                   .content(vo.getContent())
                                   .build();
    }

    public ThinkingVO toVo() {
        return ThinkingVO.builder()
                .id(id)
                .category(category)
                .title(title)
                .content(content)
                .build();
    }
}
