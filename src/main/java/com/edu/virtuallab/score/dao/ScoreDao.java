package com.edu.virtuallab.score.dao;

import com.edu.virtuallab.score.model.Score;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ScoreDao {
    int insert(Score score);
    int update(Score score);
    int delete(Long id);
    Score selectById(Long id);
    List<Score> selectAll();
} 