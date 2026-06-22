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
public class ThinkingDTO {
    private long id;
    private String category;
    private String title;
    private String content;
    //    private String password;
    private int likeCount;
    private Date createdAt;
    private Date updatedAt;

    public static ThinkingDTO of(ThinkingVO vo) {
        return vo == null ? null : ThinkingDTO.builder()
                                   .id(vo.getId())
                                   .category(vo.getCategory())
                                   .title(vo.getTitle())
                                   .content(vo.getContent())
                                   .likeCount(vo.getLikeCount())
                                   .createdAt(vo.getCreatedAt())
                                   .updatedAt(vo.getUpdatedAt())
                                   .build();
    }

    public ThinkingVO toVo() {
        return ThinkingVO.builder()
                .id(id)
                .category(category)
                .title(title)
                .content(content)
                .likeCount(likeCount)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
