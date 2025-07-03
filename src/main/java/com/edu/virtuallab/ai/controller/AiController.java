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
        return Map.of("reply", reply);
    }
}