package com.edu.virtuallab.experiment.service;
import com.edu.virtuallab.experiment.dto.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    void saveMessage(ChatMessage message);
    List<ChatMessage> selectByTeamId(Long teamId);

}

