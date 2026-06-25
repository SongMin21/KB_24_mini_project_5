package org.scoula.thinking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.thinking.domain.ThinkingVO;
import org.scoula.thinking.dto.ThinkingDTO;
import org.scoula.thinking.dto.ThinkingUpdateDTO;
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
    public boolean updateThinking(ThinkingUpdateDTO thinking) {
        // thinking null?
        if(thinking == null ||
                thinking.getCategory() == null || thinking.getCategory().trim().isEmpty() ||
                thinking.getTitle() == null || thinking.getTitle().trim().isEmpty() ||
                thinking.getContent() == null || thinking.getContent().trim().isEmpty() ||
                thinking.getPassword() == null || thinking.getPassword().trim().isEmpty()
        ) {
            // exception 던질까?
            log.warn("입력 값이 null");
            throw new IllegalArgumentException("업데이트 입력 값이 없음");
        }

        // password 맞나?
        // password를 가져올 방법? -> mapper.getPassword(id) -> id는 어디서?
        // ThinkingUpdateDTO thinking -> id -> 매개변수에 넣기
        long id = thinking.getId(); // id 가져옴
        String dbPassword = mapper.getPassword(id);   // db에 저장된 password
        String curPassword = thinking.getPassword();    // 받아온 password

        // password null?
        if(dbPassword == null || dbPassword.trim().isEmpty()) {
            log.warn("db의 password가 null");
            throw new IllegalArgumentException("db password가 없음");
        }

        // password가 일치하나?
        if(!dbPassword.equals(curPassword)) {
            log.warn("비밀번호가 일치하지 않음");
            throw new IllegalArgumentException("비밀번호가 일치하지 않음");
        }
        // dto를 vo로 변경?
        ThinkingVO vo = thinking.toVo();
        // update?
        return mapper.update(vo) == 1;
    }


    // 이현서

    // 이현주
}
