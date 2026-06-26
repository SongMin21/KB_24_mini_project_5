package org.scoula.thinking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.thinking.domain.ThinkingVO;
import org.scoula.thinking.dto.ThinkingDTO;
import org.scoula.thinking.mapper.ThinkingMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ThinkingServiceImpl implements ThinkingService{
    final private ThinkingMapper mapper;

    // 전체
    @Override
    public List<ThinkingDTO> getThinking() {
        // mapper vo를 조회 -> list<thinkingvo>
        List<ThinkingVO> vo  = mapper.getList();
        // list<thinkingdto> 변환
        List<ThinkingDTO> dto = vo.stream().map(ThinkingDTO::of).toList();
        // 리턴
        return dto;
    }


    // 강민주

    // 복원준

    @Override
    public boolean deleteThinking(long id) {

        // id == 음수이면 안됨.

        if(id <= 0){
            log.warn("delete service 실패");
            throw new IllegalArgumentException("내용은 필수입니다.");
        }
        return mapper.delete(id) == 1;
    }


    // 이현서
    // 이현주
}
