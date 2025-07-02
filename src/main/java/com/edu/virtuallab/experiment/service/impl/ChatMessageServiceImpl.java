package com.edu.virtuallab.experiment.service.impl;

import com.edu.virtuallab.experiment.dao.ProjectDiscussionDao;
import com.edu.virtuallab.experiment.dto.ChatMessage;
import com.edu.virtuallab.experiment.model.ProjectDiscussion;
import com.edu.virtuallab.experiment.service.ChatMessageService;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mysql.cj.conf.PropertyKey.logger;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ProjectDiscussionDao discussionDao;

    @Override
    public void saveMessage(ChatMessage message) {
        ProjectDiscussion discussion = new ProjectDiscussion();
        discussion.setTeamId(message.getTeamId());
        Long teamId = message.getTeamId();

        System.out.println("teamId = " + teamId);
        discussion.setProjectId(discussionDao.findProjectIdByTeamId(message.getTeamId()));
        discussion.setStudentId(message.getStudentId());
        discussion.setContent(message.getContent());
        discussion.setCreatedAt(LocalDateTime.now());

        discussionDao.insert(discussion);
    }
    @Autowired
    private ProjectDiscussionDao chatDao;  // 改成你的 DAO 名称

    // Service 层代码示例
    public List<ChatMessage> selectByTeamId(Long teamId) {
        List<ChatMessage> discussions = discussionDao.selectByTeamId(teamId);

        // 转换为 ChatMessage 类型
        return discussions.stream().map(discussion -> {
            ChatMessage msg = new ChatMessage();
            msg.setStudentId(discussion.getStudentId());
            msg.setTeamId(discussion.getTeamId());
            msg.setContent(discussion.getContent());
            return msg;
        }).collect(Collectors.toList());
    }

}
