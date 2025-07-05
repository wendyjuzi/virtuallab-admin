package com.edu.virtuallab.resource.controller;

import com.edu.virtuallab.resource.model.ResourceComment;
import com.edu.virtuallab.resource.service.ResourceCommentService;
import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.common.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;
import com.edu.virtuallab.log.annotation.OperationLogRecord;

@RestController
@RequestMapping("/resource-comment")
@Validated
public class ResourceCommentController {
    @Autowired
    private ResourceCommentService resourceCommentService;

    @OperationLogRecord(operation = "CREATE_RESOURCE_COMMENT", module = "RESOURCE", action = "创建资源评论", description = "用户创建资源评论", permissionCode = "RESOURCE_MANAGE")
    @PostMapping
    public ResponseEntity<Integer> createComment(@RequestBody ResourceComment comment) {
        int result = resourceCommentService.addComment(comment);
        return ResponseEntity.ok(result);
    }

    @OperationLogRecord(operation = "CREATE_RESOURCE_COMMENT", module = "RESOURCE", action = "创建资源评论", description = "用户创建资源评论", permissionCode = "RESOURCE_MANAGE")
    @PostMapping("/create")
    public ResponseEntity<Integer> create(@RequestBody ResourceComment comment) {
        int result = resourceCommentService.addComment(comment);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/resource/{resourceId}")
    public ResponseEntity<?> getCommentsByResourceId(@PathVariable Long resourceId) {
        return ResponseEntity.ok(resourceCommentService.getCommentsByResourceId(resourceId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(resourceCommentService.getCommentById(id));
    }

    @OperationLogRecord(operation = "UPDATE_RESOURCE_COMMENT", module = "RESOURCE", action = "更新资源评论", description = "用户更新资源评论", permissionCode = "RESOURCE_MANAGE")
    @PutMapping("/update")
    public ResponseEntity<Integer> update(@RequestBody ResourceComment comment) {
        int result = resourceCommentService.updateComment(comment);
        return ResponseEntity.ok(result);
    }

    @OperationLogRecord(operation = "DELETE_RESOURCE_COMMENT", module = "RESOURCE", action = "删除资源评论", description = "用户删除资源评论", permissionCode = "RESOURCE_MANAGE")
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> delete(@PathVariable Long id) {
        int result = resourceCommentService.deleteComment(id);
        return ResponseEntity.ok(result);
    }

    @OperationLogRecord(operation = "REPLY_RESOURCE_COMMENT", module = "RESOURCE", action = "回复资源评论", description = "用户回复资源评论", permissionCode = "RESOURCE_MANAGE")
    @PostMapping("/reply")
    public ResponseEntity<Integer> reply(@RequestBody ResourceComment reply) {
        int result = resourceCommentService.addComment(reply);
        return ResponseEntity.ok(result);
    }
}