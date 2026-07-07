package org.scoula.thinking.controller;

import lombok.extern.log4j.Log4j2;
import org.scoula.thinking.dto.ThinkingCreateDTO;
import org.scoula.thinking.dto.ThinkingDTO;
import org.scoula.thinking.dto.ThinkingDeleteDTO;
import org.scoula.thinking.dto.ThinkingUpdateDTO;
import org.scoula.thinking.service.ThinkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    @GetMapping("/date/{date}")
    public ResponseEntity<List<ThinkingDTO>> getByDate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        List<ThinkingDTO> list = service.getByDate(date);
        return ResponseEntity.ok(list);
    }

    // update like
    @PostMapping("/{id}/like")
    public ResponseEntity<ThinkingDTO> updateLike(@PathVariable long id) {
        ThinkingDTO thinking = service.updateLike(id);
        return ResponseEntity.ok(thinking);
    }

    // 복원준

    @PutMapping("/{id}")
    public ResponseEntity<ThinkingDTO> updateThinking(@PathVariable long id, @RequestBody ThinkingUpdateDTO dto){
        log.info("게시글 수정 요청" + dto);
        dto.setId(id);
        return ResponseEntity.ok(service.updateThinking(dto));
    }

    @GetMapping("/category")
    public ResponseEntity<List<ThinkingDTO>> getByCategory(@RequestParam("category") String category){
        log.info("REST API 카테고리별 조회 요청 : " + category);
        List<ThinkingDTO> list = service.getByCategory(category);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ThinkingDTO> deleteThinking(@PathVariable long id, @RequestParam String password) {
        ThinkingDeleteDTO dto = ThinkingDeleteDTO.builder()
                .id(id)
                .password(password)
                .build();
        return ResponseEntity.ok(service.deleteThinking(dto));
    }
  
    // 이현서
    @GetMapping("/like")
    public ResponseEntity<List<ThinkingDTO>> getByLike(){
        List<ThinkingDTO> list = service.getByLike();
        return ResponseEntity.ok(list);
    }

    // 이현주
    @PostMapping("")
    public ResponseEntity<ThinkingDTO> create(@RequestBody ThinkingCreateDTO thinking){
        return ResponseEntity.ok(service.create(thinking));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThinkingDTO> getListOne(@PathVariable Long id){
        return ResponseEntity.ok(service.getListOne(id));
    }
}
