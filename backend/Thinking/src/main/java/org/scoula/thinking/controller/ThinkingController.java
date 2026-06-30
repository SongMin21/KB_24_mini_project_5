package org.scoula.thinking.controller;

import lombok.extern.slf4j.Slf4j;
import org.scoula.thinking.domain.ThinkingVO;
import org.scoula.thinking.dto.ThinkingDTO;
import org.scoula.thinking.dto.ThinkingUpdateDTO;
import org.scoula.thinking.service.ThinkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/thinking")
@RestController
@Slf4j

public class ThinkingController {

    @Autowired
    private ThinkingService service;
    // 강민주

    // 복원준

    @PutMapping("")
    public ResponseEntity<String> updateThinking(ThinkingUpdateDTO dto){
        log.info("게시글 수정 요청" + dto);

        //서비스 계층 로직 실행
        if(service.updateThinking(dto)){
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.status(401).body("fail");
        }
    }
    // 이현서

    // 이현주
}
