package com.edu.virtuallab.score.service;

import com.edu.virtuallab.score.model.Score;
import java.util.List;

public interface ScoreService {
    int create(Score score);
    int update(Score score);
    int delete(Long id);
    Score getById(Long id);
    List<Score> listAll();
} 