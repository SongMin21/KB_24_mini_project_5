package org.scoula.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.comment.dto.CommentUpdateDTO;
import org.scoula.comment.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
@Log4j2
public class CommentController {

    private final CommentService service;

    // 강민주

    // 복원준

    // 이현서
    @PutMapping("/{id}")
    public ResponseEntity<CommentUpdateDTO> updateComment(
            @PathVariable Long id,
            @RequestBody CommentUpdateDTO comment){
        //
        comment.setId(id);
        CommentUpdateDTO dto = service.updateComment(comment);
        return ResponseEntity.ok(dto);
    }

    // 이현주
}
