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
import com.edu.virtuallab.log.annotation.OperationLogRecord;

@RestController
@RequestMapping("/score")
public class ScoreController {
    @Autowired
    private ScoreService scoreService;

    @OperationLogRecord(operation = "CREATE_SCORE", module = "SCORE", action = "录入成绩", description = "教师录入实验成绩", permissionCode = "SCORE_MANAGE")
    @PostMapping("/create")
    public int create(@RequestBody Score score) {
        return scoreService.create(score);
    }

    @OperationLogRecord(operation = "UPDATE_SCORE", module = "SCORE", action = "修改成绩", description = "教师修改实验成绩", permissionCode = "SCORE_MANAGE")
    @PutMapping("/update")
    public int update(@RequestBody Score score) {
        return scoreService.update(score);
    }

    @OperationLogRecord(operation = "DELETE_SCORE", module = "SCORE", action = "删除成绩", description = "教师删除实验成绩", permissionCode = "SCORE_MANAGE")
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