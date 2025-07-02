package com.edu.virtuallab.experiment.controller;

import com.edu.virtuallab.experiment.dto.ChatMessage;
import com.edu.virtuallab.experiment.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // ✅ 改成 RestController，所有方法默认返回 JSON
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatService;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/chat") // 接收来自前端 /app/chat 的消息
    public void processMessage(@Payload ChatMessage message) {
        chatService.saveMessage(message);

        messagingTemplate.convertAndSend("/topic/team/" + message.getTeamId(), message);
    }

    @GetMapping("/history")
    public List<ChatMessage> getChatHistory(@RequestParam Long teamId) {
        return chatService.selectByTeamId(teamId);
    }
}

