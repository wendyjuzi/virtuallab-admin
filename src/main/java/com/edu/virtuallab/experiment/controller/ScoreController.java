package com.edu.virtuallab.experiment.controller;

import com.edu.virtuallab.experiment.dto.ManualScoreRequest;
import com.edu.virtuallab.experiment.service.ExperimentReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/experiment") // 建议统一前缀
@RequiredArgsConstructor // 自动注入构造函数参数
@Slf4j
public class ScoreController {

    private final ExperimentReportService experimentReportService;

    @PostMapping("/manual-score")
    public ResponseEntity<?> manualScore(@RequestBody ManualScoreRequest request) {
        log.info("收到评分请求: sessionId={}, score={}, comment={}",
                request.getSessionId(), request.getScore(), request.getComment());

        boolean success = experimentReportService.updateManualScore(
                request.getSessionId(),
                request.getScore(),
                request.getComment()
        );

        if (success) {
            return ResponseEntity.ok(Map.of("message", "评分成功"));
        } else {
            return ResponseEntity.status(500).body(Map.of("message", "评分失败"));
        }
    }
}

