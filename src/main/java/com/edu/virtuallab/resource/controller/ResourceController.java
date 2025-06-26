package com.edu.virtuallab.resource.controller;

import com.edu.virtuallab.resource.model.Resource;
import com.edu.virtuallab.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@RestController
@RequestMapping("/resource")
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
} 