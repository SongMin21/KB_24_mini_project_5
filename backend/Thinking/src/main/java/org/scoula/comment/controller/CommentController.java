package org.scoula.comment.controller;

import lombok.extern.log4j.Log4j2;
import org.scoula.comment.dto.CommentCreateDTO;
import org.scoula.comment.dto.CommentDTO;
import org.scoula.comment.dto.CommentDeleteDTO;
import org.scoula.comment.dto.CommentUpdateDTO;
import org.scoula.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@Log4j2
public class CommentController {

    @Autowired
    CommentService service;

    // 강민주
    @PostMapping("/{id}")
    public ResponseEntity<CommentDTO> create(@RequestBody CommentCreateDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    // 복원준

    // 이현서
    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long id,
            @RequestBody CommentUpdateDTO dto){

        dto.setId(id);
        CommentDTO comment = service.updateComment(dto);
        return ResponseEntity.ok(comment);
    }
    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteComment(
            @PathVariable Long id,
            @RequestBody CommentDeleteDTO dto){
        dto.setId(id);
        Long deletedId = service.deleteComment(dto);

        return ResponseEntity.ok(deletedId);
    }
    // 이현주
}
