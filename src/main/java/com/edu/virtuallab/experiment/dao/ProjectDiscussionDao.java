package com.edu.virtuallab.experiment.dao;

import com.edu.virtuallab.experiment.dto.ChatMessage;
import com.edu.virtuallab.experiment.model.ProjectDiscussion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectDiscussionDao {

    void insert(ProjectDiscussion discussion);

    List<ProjectDiscussion> findByTeamId(@Param("teamId") Long teamId);

    Long findProjectIdByTeamId(@Param("teamId") Long teamId); // 供消息入库时使用
    List<ChatMessage> selectByTeamId(@Param("teamId") Long teamId);

}