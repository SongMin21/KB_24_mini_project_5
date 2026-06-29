package org.scoula.thinking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.thinking.domain.ThinkingVO;
import org.scoula.thinking.dto.ThinkingCreateDTO;
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

    public ThinkingVO toVO(){
        return ThinkingVO.builder()
                .category(this.category)
                .title(this.title)
                .content(this.content)
                .password(this.password)
                .build();
    }

    @Override
    public boolean createThinking(ThinkingDTO dto){
        log.info("service createThinking.. " + dto);

        // 필수값 검증 (from 제미나이....)
         if(dto.getPasswrod() == null || dto.getPassword().trim().isEmpty()){
             throw new IllegalArgumentException("수정/삭제를 위한 비밀번호가 필요합니다.")
         }

        // DTO를 VO로 변환
        ThinkingVO vo =dto.toVO();

        // Mapper 호출 및 결과 반환
        int result = mapper.create(vo);
        return result == 1;
    }
    // 이현서

    // 이현주

    @Override
    public void create(ThinkingCreateDTO thinking) {
        log.info("create......." + thinking);
        mapper.create(thinking.toVo());
    }
}
