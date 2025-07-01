package com.edu.virtuallab.resource.controller;

import com.edu.virtuallab.resource.model.ResourceFavorite;
import com.edu.virtuallab.resource.service.ResourceFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/resource/favorite")
public class ResourceFavoriteController {
    @Autowired
    private ResourceFavoriteService resourceFavoriteService;

    @PostMapping("/add")
    public int addFavorite(@RequestBody ResourceFavorite resourceFavorite) {
        return resourceFavoriteService.addFavorite(resourceFavorite);
    }

    @DeleteMapping("/remove/{id}")
    public int removeFavorite(@PathVariable Long id) {
        return resourceFavoriteService.removeFavorite(id);
    }

    @GetMapping("/get/{id}")
    public ResourceFavorite getFavoriteById(@PathVariable Long id) {
        return resourceFavoriteService.getFavoriteById(id);
    }

    @GetMapping("/list/{userId}")
    public List<ResourceFavorite> getFavoritesByUserId(@PathVariable Long userId,
                                                      @RequestParam(defaultValue = "0") int offset,
                                                      @RequestParam(defaultValue = "10") int size) {
        return resourceFavoriteService.getFavoritesByUserId(userId, offset, size);
    }

    @GetMapping("/count/{userId}")
    public int countFavoritesByUserId(@PathVariable Long userId) {
        return resourceFavoriteService.countFavoritesByUserId(userId);
    }

    @GetMapping("/getByUserAndResource")
    public ResourceFavorite getFavoriteByUserAndResource(@RequestParam Long userId, @RequestParam Long resourceId) {
        return resourceFavoriteService.getFavoriteByUserAndResource(userId, resourceId);
    }
} 