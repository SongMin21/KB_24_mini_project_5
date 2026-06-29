package org.scoula.thinking.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.scoula.config.RootConfig;
import org.scoula.thinking.dto.ThinkingCreateDTO;
import org.scoula.thinking.dto.ThinkingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes={RootConfig.class})
@Log4j2
class ThinkingServiceImplTest {

    @Autowired
    private ThinkingService service;

    // 전체
    @Test
    @DisplayName("getThinking service test")
    void getThinking() {
        log.info("getThinking service test");
        List<ThinkingDTO> list = service.getThinking();
        for(ThinkingDTO dto : list) {
            log.info(dto);
        }
    }

    // 이현서
    @Test
    @DisplayName("getByLike service test")
    void getByLike(){
        log.info("getByLike service test");
        List<ThinkingDTO> list = service.getByLike();
        for(ThinkingDTO dto : list){
            log.info(dto);
        }
    }
  
    // 이현주
    @Test
    @DisplayName("getThinkingOne service test")
    void getListOne(){
        log.info("getThinkingOne service test");
        log.info(service.getListOne(1L));
    }
  
    @Test
    @DisplayName("createThinking service test")
    void create(){
        log.info("createThinking service test");
        ThinkingCreateDTO dto = ThinkingCreateDTO.builder()
                .category("LEARNED")
                .title("배운 것")
                .content("Spring Service에 대해")
                .password("1234")
                .build();

        service.create(dto);
        log.info(dto);
    }
  
    // 강민주
    @Test
    @DisplayName("getByDate service test")
    void getByDate() {
        log.info("getByDate service test");
        // 형식에 맞게 date 선언
        LocalDate localDate = LocalDate.of(2026, 6, 1);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        // service 실행
        List<ThinkingDTO> list = service.getByDate(date);
        for(ThinkingDTO dto : list) {
            log.info(dto);
        }
    }
  
    @Test
    @DisplayName("update like service test")
    void updateLike() {
        log.info("update like service test");
        log.info(service.updateLike(1L));
    }
}