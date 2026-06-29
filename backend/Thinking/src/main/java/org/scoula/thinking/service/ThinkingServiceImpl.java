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
    //카테고리별 조회 구현
    @Override
    public List<ThinkingDTO> getByCategory(String category){
        log.info("service getByCategory ---------" + category);

        // Mapper 호출하여 DB에서 특정 카테고리에 해당하는 VO 리스트 가져오기
        List<ThinkingVO> voList = mapper.getByCategory(category);

        // 받아온 VO 리스트를 DTO 리스트로 변환
        List<ThinkingDTO> dtoList = voList.stream().map(ThinkingDTO::of).toList();

        // 변환된 DTO 리스트 반환
        return dtoList;
    }
    // 이현서

    // 이현주
}
