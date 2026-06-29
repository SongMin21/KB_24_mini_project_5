package org.scoula.comment.controller;

import lombok.extern.log4j.Log4j2;
import org.scoula.comment.dto.CommentDTO;
import org.scoula.comment.service.CommentService;
import org.scoula.thinking.dto.ThinkingDTO;
import org.scoula.thinking.service.ThinkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@Log4j2
@RequestMapping("/api/comment")

public class CommentController {
    @Autowired
    private CommentService service;

    // 강민주

    // 복원준

    // 이현서

    // 이현주
    @GetMapping("/{thinkingId}")
    public ResponseEntity<List<CommentDTO>> selectComment(@PathVariable long thinkingId) {
        return ResponseEntity.ok(service.selectComment(thinkingId));
    }
}
