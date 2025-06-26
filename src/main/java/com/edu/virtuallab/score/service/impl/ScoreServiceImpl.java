package com.edu.virtuallab.score.service.impl;

import com.edu.virtuallab.score.model.Score;
import com.edu.virtuallab.score.dao.ScoreDao;
import com.edu.virtuallab.score.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {
    @Autowired
    private ScoreDao scoreDao;

    @Override
    public int create(Score score) {
        return scoreDao.insert(score);
    }

    @Override
    public int update(Score score) {
        return scoreDao.update(score);
    }

    @Override
    public int delete(Long id) {
        return scoreDao.delete(id);
    }

    @Override
    public Score getById(Long id) {
        return scoreDao.selectById(id);
    }

    @Override
    public List<Score> listAll() {
        return scoreDao.selectAll();
    }
} 