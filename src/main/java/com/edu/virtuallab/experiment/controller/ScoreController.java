package com.edu.virtuallab.experiment.controller;

import com.edu.virtuallab.experiment.dto.ManualScoreRequest;
import com.edu.virtuallab.experiment.model.ExperimentReport;
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
    @GetMapping("/manual-score/show")
    public ResponseEntity<?> getManualScore(@RequestParam String sessionId) {
        log.info("请求获取人工评分，sessionId: {}", sessionId);  // ✅ 请求参数日志

        ExperimentReport report = experimentReportService.getManualScore(sessionId);

        if (report == null) {
            log.warn("未找到对应的实验报告，sessionId: {}", sessionId);  // ⚠️ 无报告
            return ResponseEntity.status(404).body(Map.of("message", "未找到评分记录"));
        }

        if (report.getScore() == null) {
            log.warn("实验报告存在但没有人工评分，sessionId: {}", sessionId);  // ⚠️ 报告存在但未评分
            return ResponseEntity.status(404).body(Map.of("message", "未找到评分记录"));
        }

        log.info("成功获取评分，score: {}, comment: {}", report.getScore(), report.getComment());  // ✅ 返回数据日志
        return ResponseEntity.ok(report);
    }
    @DeleteMapping("/manual-score/delete")
    public ResponseEntity<?> deleteManualScore(@RequestParam String sessionId) {
        boolean success = experimentReportService.deleteManualScore(sessionId);
        return success
                ? ResponseEntity.ok(Map.of("message", "评分记录已撤回"))
                : ResponseEntity.status(500).body(Map.of("message", "撤回失败"));
    }
}

