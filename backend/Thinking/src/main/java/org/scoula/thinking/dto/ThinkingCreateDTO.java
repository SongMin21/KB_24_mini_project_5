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
public class ThinkingCreateDTO {
    private String category;
    private String title;
    private String content;
    private String password;

    public static ThinkingCreateDTO of(ThinkingVO vo) {
        return vo == null ? null : ThinkingCreateDTO.builder()
                                   .category(vo.getCategory())
                                   .title(vo.getTitle())
                                   .content(vo.getContent())
                                   .password(vo.getPassword())
                                   .build();
    }

    public ThinkingVO toVo() {
        return ThinkingVO.builder()
                .category(category)
                .title(title)
                .content(content)
                .password(password)
                .build();
    }
}
