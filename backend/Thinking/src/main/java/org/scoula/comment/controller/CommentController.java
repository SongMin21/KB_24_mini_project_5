package org.scoula.comment.controller;

import lombok.extern.log4j.Log4j2;
import org.scoula.thinking.service.ThinkingService;
import org.scoula.thinking.dto.ThinkingDTO;
import org.scoula.comment.dto.CommentCreateDTO;
import org.scoula.comment.dto.CommentDTO;
import org.scoula.comment.dto.CommentDeleteDTO;
import org.scoula.comment.dto.CommentUpdateDTO;
import org.scoula.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.stream.events.Comment;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
@Log4j2
public class CommentController {
    @Autowired
    private CommentService service;

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
    public ResponseEntity<CommentDTO> deleteComment(
            @PathVariable Long id,
            @RequestBody CommentDeleteDTO dto){
        dto.setId(id);
        CommentDTO comment = service.deleteComment(dto);

        return ResponseEntity.ok(comment);
    }
    // 이현주
    @GetMapping("/{thinkingId}")
    public ResponseEntity<List<CommentDTO>> selectComment(@PathVariable long thinkingId) {
        return ResponseEntity.ok(service.selectComment(thinkingId));
    }
}
