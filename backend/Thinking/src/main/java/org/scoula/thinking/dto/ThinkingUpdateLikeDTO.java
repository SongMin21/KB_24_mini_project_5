package org.scoula.thinking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.scoula.thinking.domain.ThinkingVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThinkingUpdateLikeDTO {
    private long id;
    private int likeCount;

    public static ThinkingDTO of(ThinkingVO vo) {
        return vo == null ? null : ThinkingDTO.builder()
                .id(vo.getId())
                .likeCount(vo.getLikeCount())
                .build();
    }

    public ThinkingVO toVo() {
        return ThinkingVO.builder()
                .id(id)
                .likeCount(likeCount)
                .build();
    }
}
