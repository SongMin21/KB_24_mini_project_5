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

    // 이현서
    @Override
    public List<ThinkingDTO> getByLike() {
        //  mapper vo 가져오기
        List<ThinkingVO> vo = mapper.getByLike();

        // 기존 vo를 dto로 변환
        List<ThinkingDTO> dto = vo.stream()
                // 좋아요 1개 이상인 글만 조회
                .filter(voList -> voList.getLikeCount() >= 20)
                .map(ThinkingDTO::of)
                .toList();

        return dto;
    }


    // 이현주
}
