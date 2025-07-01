package com.edu.virtuallab.ai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final String apiKey = "sk-1HX9z9ijmL2DbZq5aCtavdI9iepxXjEs8o8MsHDMDtj4NzW7"; // 替换为你的 OpenAI 密钥

    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody Map<String, String> body) throws IOException, InterruptedException {
        String userInput = body.get("message");

        String requestBody = """
        {
          \"model\": \"gpt-4\",
          \"messages\": [
            { \"role\": \"system\", \"content\": \"你是虚拟实验室中的智能助教，负责帮助学生解答实验相关问题。\" },
            { \"role\": \"user\", \"content\": \"%s\" }
          ]
        }
        """.formatted(userInput);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        String reply = mapper.readTree(response.body()).at("/choices/0/message/content").asText();

        return Map.of("reply", reply);
    }
} 