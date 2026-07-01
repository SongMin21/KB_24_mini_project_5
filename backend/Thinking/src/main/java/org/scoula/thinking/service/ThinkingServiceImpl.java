package org.scoula.thinking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.thinking.domain.ThinkingVO;
import org.scoula.thinking.dto.ThinkingDTO;
import org.scoula.thinking.dto.ThinkingDeleteDTO;
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
    public boolean deleteThinking(ThinkingDeleteDTO thinking) {

        // password

        // id == 음수이면 안됨.
        if (thinking == null || thinking.getId() <= 0) {
            log.warn("유효하지 않은 삭제 요청");
            throw new IllegalArgumentException("id는 1 이상이어야 합니다.");
        }
        if (thinking.getPassword() == null || thinking.getPassword().trim().isEmpty()) {
            log.warn("삭제 비밀번호가 비어 있음");
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
        long id = thinking.getId();
        String dbPassword = mapper.getPassword(id);   // db에 저장된 password
        String curPassword = thinking.getPassword();    // 받아온 password

        // password가 일치하나?
        if(!dbPassword.equals(curPassword)) {
            log.warn("비밀번호가 일치하지 않음");
            throw new IllegalArgumentException("비밀번호가 일치하지 않음");
        }

        // dto를 vo로 변경?
//        ThinkingVO vo = thinking.toVo();
        return mapper.delete(id) == 1;
    }


    // 이현서
    // 이현주
}
