package org.scoula.thinking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.thinking.domain.ThinkingVO;
import org.scoula.thinking.dto.ThinkingCreateDTO;
import org.scoula.thinking.dto.ThinkingDTO;
import org.scoula.thinking.mapper.ThinkingMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    @Override
    public List<ThinkingDTO> getByDate(Date date) {
        // date 유효성 검증
        if(date == null) {
            throw new IllegalArgumentException("날짜 입력은 필수입니다.");
        }
        // mapper -> vo를 조회
        List<ThinkingVO> vo = mapper.getByDate(date);

        // vo가 비었다면? 빈 리스트 반환
        if(vo == null) {
            return List.of();
        }

        // List<ThinkingDTO>로 변환
        List<ThinkingDTO> dto = vo.stream().map(ThinkingDTO::of).toList();
        // 리턴
        return dto;
    }


    // update like(like +1)
    @Override
    public boolean updateLike(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 ID입니다.");
        }
        int count = mapper.updateLike(id);
        return count == 1;
    }

    // 복원준

    // 이현서

    // 이현주
    @Override
    public ThinkingDTO getListOne(Long id) {
        log.info("get......" + id);
        ThinkingDTO dto = ThinkingDTO.of(mapper.getListOne(id));
        return dto;
    }
  
    @Override
    public void create(ThinkingCreateDTO thinking) {
        log.info("create......." + thinking);
        mapper.create(thinking.toVo());
    }
}
