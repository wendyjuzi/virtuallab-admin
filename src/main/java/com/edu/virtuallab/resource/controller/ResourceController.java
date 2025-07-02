package com.edu.virtuallab.resource.controller;

import com.edu.virtuallab.resource.model.*;
import com.edu.virtuallab.resource.service.ResourceService;
import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/resource")
@Validated
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @PostMapping
    public int create(@RequestBody Resource resource) {
        return resourceService.create(resource);
    }

    @PutMapping
    public int update(@RequestBody Resource resource) {
        return resourceService.update(resource);
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable Long id) {
        return resourceService.delete(id);
    }

    @GetMapping("/{id}")
    public Resource getById(@PathVariable Long id) {
        return resourceService.getById(id);
    }

    @GetMapping("/list")
    public List<Resource> listAll() {
        return resourceService.listAll();
    }

    // 设置资源共享权限
    @PostMapping("/share")
    public ResponseEntity<?> setSharePermission(@RequestBody ResourceShare resourceShare) {
        int result = resourceService.setSharePermission(resourceShare);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/copy")
    public ResponseEntity<?> copyResource(@RequestBody ResourceCopy resourceCopy) {
        return ResponseEntity.ok(resourceService.copyResource(resourceCopy));
    }

    @PostMapping("/interaction")
    public ResponseEntity<?> addInteraction(@RequestBody ResourceInteraction interaction) {
        return ResponseEntity.ok(resourceService.addInteraction(interaction));
    }

    @GetMapping("/interaction/{resourceId}")
    public ResponseEntity<?> getInteractions(@PathVariable Long resourceId) {
        return ResponseEntity.ok(resourceService.getInteractions(resourceId));
    }
}