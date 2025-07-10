package com.edu.virtuallab.ai.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Value("${zhipu.api.url}")
    private String zhipuUrl;

    @Value("${zhipu.api.key}")
    private String zhipuApiKey;

    @Value("${zhipu.api.model}")
    private String zhipuModel;

    @PostMapping("/chat")
    public Map<String, String> chatZhipu(@RequestBody Map<String, String> body) throws IOException, InterruptedException {
        String userInput = body.get("message");

        // 新增：判断是否为实验相关问题
        String[] keywords = {"实验", "虚拟实验室", "3D场景", "实验项目", "实验发布", "实验设计"};
        boolean isRelated = false;
        if (userInput != null) {
            for (String keyword : keywords) {
                if (userInput.contains(keyword)) {
                    isRelated = true;
                    break;
                }
            }
        }
        if (!isRelated) {
            return Map.of("reply", "不知道，请问实验相关内容");
        }

        String requestBody = """
        {
          "model": "%s",
          "messages": [
            { "role": "user", "content": "%s" }
          ]
        }
        """.formatted(zhipuModel, userInput);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(zhipuUrl))
                .header("Authorization", "Bearer " + zhipuApiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 打印智谱原始响应
        System.out.println("Zhipu原始响应: " + response.body());

        ObjectMapper mapper = new ObjectMapper();
        String reply = "";
        try {
            JsonNode root = mapper.readTree(response.body());
            if (root.has("error")) {
                reply = "智谱接口错误：" + root.get("error").toString();
            } else {
                reply = root.at("/choices/0/message/content").asText();
                if (reply == null || reply.isEmpty()) {
                    reply = "智谱未返回内容，原始响应：" + response.body();
                }
            }
        } catch (Exception e) {
            reply = "智谱响应解析异常：" + e.getMessage() + "，原始响应：" + response.body();
        }
        return Map.of("reply", removeMarkdown(reply));
    }

    // 新增：去除Markdown格式的方法
    private static String removeMarkdown(String text) {
        if (text == null) return "";
        // 去除代码块
        text = text.replaceAll("```[\\s\\S]*?```", "");
        // 去除标题
        text = text.replaceAll("^#+\\s*", "");
        // 去除无序/有序列表
        text = text.replaceAll("^[\\-*+]\\s+", "");
        text = text.replaceAll("^\\d+\\.\\s+", "");
        // 去除加粗/斜体
        text = text.replaceAll("[*_]{1,3}", "");
        // 去除引用
        text = text.replaceAll("^>\\s*", "");
        // 去除链接格式 [xxx](url)
        text = text.replaceAll("\\[(.*?)\\]\\((.*?)\\)", "$1");
        // 去除图片格式 ![alt](url)
        text = text.replaceAll("!\\[(.*?)\\]\\((.*?)\\)", "");
        // 去除行首多余空格
        text = text.replaceAll("^[ \t]+", "");
        // 去除多余空行
        text = text.replaceAll("(?m)^[ \t]*\r?\n", "");
        return text.trim();
    }
}