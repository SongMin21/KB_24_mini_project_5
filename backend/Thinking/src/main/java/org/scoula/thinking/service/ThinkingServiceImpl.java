package org.scoula.thinking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.exception.PasswordMismatchException;
import org.scoula.exception.ResourceNotFoundException;
import org.scoula.thinking.domain.ThinkingVO;
import org.scoula.thinking.dto.ThinkingCreateDTO;
import org.scoula.thinking.dto.ThinkingDTO;
import org.scoula.thinking.dto.ThinkingUpdateDTO;
import org.scoula.thinking.dto.ThinkingDeleteDTO;
import org.scoula.thinking.mapper.ThinkingMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public ThinkingDTO updateLike(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 ID입니다.");
        }
        int count = mapper.updateLike(id);
        if(count != 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.");
        }
        return getListOne(id);
    }

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

    // 게시물 삭제
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

        // 게시글 자체가 없음 -> 404
        if (dbPassword == null || dbPassword.trim().isEmpty()) {
            log.warn("존재하지 않는 게시글 - id: {}", id);
            throw new ResourceNotFoundException("존재하지 않는 게시글입니다.");
        }

        // password가 일치하나?
        if(!dbPassword.equals(curPassword)) {
            log.warn("비밀번호가 일치하지 않음");
            throw new PasswordMismatchException("비밀번호가 일치하지 않음");
        }

        // dto를 vo로 변경?
        //ThinkingVO vo = thinking.toVo();
        return mapper.delete(id) == 1;
    }

    @Override
    public ThinkingDTO updateThinking(ThinkingUpdateDTO thinking) {
        // thinking null?
        if(thinking == null ||
                thinking.getId() <= 0 ||
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
            log.warn("존재하지 않는 게시글 - id: {}", id);
            throw new ResourceNotFoundException("존재하지 않는 게시글입니다.");
        }

        // password가 일치하나?
        if(!dbPassword.equals(curPassword)) {
            log.warn("비밀번호가 일치하지 않음");
            throw new PasswordMismatchException("비밀번호가 일치하지 않음");
        }
        // dto를 vo로 변경?
        ThinkingVO vo = thinking.toVo();
        // update?
        if(mapper.update(vo) != 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물 수정에 실패했습니다.");
        }
        return getListOne(vo.getId());
    }

    // 이현서
    @Override
    public List<ThinkingDTO> getByLike() {
        //  mapper vo 가져오기
        List<ThinkingVO> vo = mapper.getByLike();

        // 기존 vo를 dto로 변환
        List<ThinkingDTO> dto = vo.stream()
                .map(ThinkingDTO::of)
                .toList();
        return dto;
    }

    // 이현주
    @Override
    public ThinkingDTO getListOne(Long id) {
        log.info("get......" + id);
        ThinkingVO vo = mapper.getListOne(id);

        if (vo == null) {
            throw new ResourceNotFoundException("존재하지 않는 게시글입니다.");
        }

        ThinkingDTO dto = ThinkingDTO.of(vo);
        return dto;
    }

    @Override
    public ThinkingDTO create(ThinkingCreateDTO thinking) {
        log.info("create......." + thinking);
        ThinkingVO vo = thinking.toVo();
        mapper.create(vo);
        return getListOne(vo.getId());
    }
}