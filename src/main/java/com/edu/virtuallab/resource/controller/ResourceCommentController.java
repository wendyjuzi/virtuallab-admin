package com.edu.virtuallab.resource.controller;

import com.edu.virtuallab.resource.model.ResourceComment;
import com.edu.virtuallab.resource.service.ResourceCommentService;
// 添加以下导入
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class ResourceCommentController {
    @Autowired
    private ResourceCommentService resourceCommentService;

    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody ResourceComment comment) {
        return ResponseEntity.ok(resourceCommentService.addComment(comment));
    }

    @GetMapping("/resource/{resourceId}")
    public ResponseEntity<?> getCommentsByResourceId(@PathVariable Long resourceId) {
        return ResponseEntity.ok(resourceCommentService.getCommentsByResourceId(resourceId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(resourceCommentService.getCommentById(id));
    }

    @PutMapping
    public ResponseEntity<?> updateComment(@RequestBody ResourceComment comment) {
        return ResponseEntity.ok(resourceCommentService.updateComment(comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        return ResponseEntity.ok(resourceCommentService.deleteComment(id));
    }
}