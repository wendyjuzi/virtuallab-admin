package com.edu.virtuallab.score.controller;

import com.edu.virtuallab.score.model.Score;
import com.edu.virtuallab.score.service.ScoreService;
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
@RequestMapping("/score")
public class ScoreController {
    @Autowired
    private ScoreService scoreService;

    @PostMapping
    public int create(@RequestBody Score score) {
        return scoreService.create(score);
    }

    @PutMapping
    public int update(@RequestBody Score score) {
        return scoreService.update(score);
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable Long id) {
        return scoreService.delete(id);
    }

    @GetMapping("/{id}")
    public Score getById(@PathVariable Long id) {
        return scoreService.getById(id);
    }

    @GetMapping("/list")
    public List<Score> listAll() {
        return scoreService.listAll();
    }
} 