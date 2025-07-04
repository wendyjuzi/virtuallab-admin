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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
@RestController
@RequestMapping("/pdf")
public class PDFController {

    @Value("${zhipu.api.url}")
    private String zhipuUrl;

    @Value("${zhipu.api.key}")
    private String zhipuApiKey;

    @Value("${zhipu.api.model}")
    private String zhipuModel;


    /**
     * 上传 PDF 并自动评分
     */
    @PostMapping(value = "/score", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> scoreReport(@RequestParam("file") MultipartFile file) throws Exception {
        // 1. 读取 PDF 内容
        System.out.println("接收到评分请求，文件大小: " + file.getSize());
        String content = extractTextFromPdf(file);
        if (content.length() > 3000) {
            content = content.substring(0, 3000); // 避免内容过长
        }

        // 2. 构造评分提示词（要求AI返回无**Markdown标记的文本，并添加学习建议）
        String prompt = "请你作为虚拟实验平台的智能评分助手，阅读以下学生实验报告内容，"
                + "从操作规范性、数据准确性、逻辑清晰性、语言表达等维度给出一个总分（满分100），并提供评分理由。\n"
                + "要求：\n"
                + "1. 不要使用**加粗**或任何Markdown标记\n"
                + "2. 评分理由按以下格式返回：\n"
                + "   - 操作规范性：[分数]/100，[评语]\n"
                + "   - 数据准确性：[分数]/100，[评语]\n"
                + "   - 逻辑清晰性：[分数]/100，[评语]\n"
                + "   - 语言表达：[分数]/100，[评语]\n"
                + "3. 总分：[总分]/100\n"
                + "4. 最后添加一个'学习建议'部分，提供2-3条具体的改进建议\n\n"
                + "实验报告内容：\n" + content;

        // 3. 调用智谱 AI
        String scoreFeedback = callZhipuApi(prompt);

        // 4. 返回评分结果
        return Map.of("scoreFeedback", scoreFeedback);
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document).trim();
        }
    }

    private String callZhipuApi(String userInput) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();

        // 构造请求体（自动处理JSON转义）
        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("model", zhipuModel);

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of(
                "role", "user",
                "content", userInput
        ));
        requestBodyMap.put("messages", messages);

        String requestBody = mapper.writeValueAsString(requestBodyMap);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(zhipuUrl))
                .header("Authorization", "Bearer " + zhipuApiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode root = mapper.readTree(response.body());
        if (root.has("error")) {
            throw new RuntimeException("智谱接口错误：" + root.get("error"));
        }
        return root.at("/choices/0/message/content").asText();
    }
}