package org.scoula.thinking.controller;

import lombok.extern.log4j.Log4j2;
import org.scoula.thinking.dto.ThinkingDTO;
import org.scoula.thinking.dto.ThinkingUpdateDTO;
import org.scoula.thinking.service.ThinkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @Controller
// @RequestMapping("/thinking")
@RestController
@Log4j2
@RequestMapping("/api/thinking")
public class ThinkingController {
    @Autowired
    private ThinkingService service;

    // 전체
    // http://localhost:8080/thinking/getlist
//    @GetMapping("/getlist")
//    public String getList(Model model) {
//        model.addAttribute("list", service.getThinking());
//        return "/thinking/list";
//    }

    @GetMapping("")
    public ResponseEntity<List<ThinkingDTO>> getList() {
        List<ThinkingDTO> list = service.getThinking();
        return ResponseEntity.ok(list);
    }

    // 강민주

    // 복원준

    @PutMapping("")
    public ResponseEntity<String> updateThinking(@RequestBody ThinkingUpdateDTO dto){
        log.info("게시글 수정 요청" + dto);

        //서비스 계층 로직 실행
        if(service.updateThinking(dto)){
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.status(401).body("fail");
        }
    }

    @GetMapping("/category")
    public ResponseEntity<List<ThinkingDTO>> getByCategory(@RequestParam("category") String category){
        log.info("REST API 카테고리별 조회 요청 : " + category);
        List<ThinkingDTO> list = service.getByCategory(category);
        return ResponseEntity.ok(list);
    }
    // 이현서

    // 이현주
}
