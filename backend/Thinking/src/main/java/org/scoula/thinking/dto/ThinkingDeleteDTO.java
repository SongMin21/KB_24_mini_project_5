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
public class ThinkingDeleteDTO {
    private Long id;          // 삭제할 글 번호
    private String password;  // 권한 검증용 비밀번호

    // (DTO)의 데이터를 기반으로 (VO) 껍데기를 만드는 메서드
    public ThinkingVO toVo() {
        return ThinkingVO.builder()
                .id(this.id)
                .password(this.password)
                .build();
    }
}
