package com.edu.virtuallab.experiment.controller;

import com.edu.virtuallab.experiment.model.Laboratory;
import com.edu.virtuallab.experiment.model.Reservation;
import com.edu.virtuallab.experiment.service.LabReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/experiment")
public class LabReservationController {

    private final LabReservationService labReservationService;

    @Autowired
    public LabReservationController(LabReservationService labReservationService) {
        this.labReservationService = labReservationService;
    }

    @GetMapping("/laboratories")
    public ResponseEntity<List<Laboratory>> getLaboratories() {
        log.info("接收到获取实验室列表请求");
        List<Laboratory> laboratories = labReservationService.getLaboratories();
        log.info("返回{}条实验室记录", laboratories.size());
        return ResponseEntity.ok(laboratories != null ? laboratories : Collections.emptyList());
    }

    @PostMapping("/laboratories/{labId}")
    public ResponseEntity<Laboratory> updateLabStatus(
            @PathVariable Long labId,
            @RequestBody Map<String, String> statusRequest) {
        log.info("接收到更新实验室状态请求，实验室ID: {}, 新状态: {}", labId, statusRequest.get("status"));
        try {
            // 验证状态值是否有效
            String statusStr = statusRequest.get("status");
            if (statusStr == null) {
                throw new IllegalArgumentException("状态值不能为空");
            }

            // 转换状态值为枚举
            Laboratory.LabStatus status;
            try {
                status = Laboratory.LabStatus.valueOf(statusStr.toLowerCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("无效的状态值: " + statusStr);
            }

            // 调用服务层更新状态
            Laboratory updatedLab = labReservationService.updateStatus(labId, status);

            log.info("实验室状态更新成功，实验室ID: {}", labId);
            return ResponseEntity.ok(updatedLab);

        } catch (IllegalArgumentException e) {
            log.error("参数错误: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            log.error("更新实验室状态失败: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }

    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getReservations() {
        log.info("接收到获取预约列表请求");
        List<Reservation> reservations = labReservationService.getReservations();
        log.info("返回{}条预约记录", reservations.size());
        return ResponseEntity.ok(reservations != null ? reservations : Collections.emptyList());
    }

    @GetMapping("/reservations/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsById(
            @PathVariable Long userId
    ){
        log.info("接收到获取预约列表请求");
        List<Reservation> reservations = labReservationService.getReservationsById(userId);
        log.info("返回{}条预约记录", reservations.size());
        return ResponseEntity.ok(reservations != null ? reservations : Collections.emptyList());

    }

    @PostMapping("/reservations/{userId}/reserve")
    public ResponseEntity<Reservation> createReservation(
            @RequestBody Reservation reservation){
        log.info("接收到创建预约请求，实验室ID:{}", reservation.getLaboratory().getLabId());
        Reservation created = labReservationService.createReservation(reservation);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/reservations/{reservationId}/approve")
    public ResponseEntity<Reservation> approveReservation(
            @PathVariable Long reservationId,
            @RequestParam Long adminId) {
        log.info("接收到批准预约请求，预约ID: {}, 管理员ID: {}", reservationId, adminId);
        Reservation approved = labReservationService.approveReservation(reservationId, adminId);
        return ResponseEntity.ok(approved);
    }

    @PostMapping("/reservations/{reservationId}/reject")
    public ResponseEntity<Reservation> rejectReservation(
            @PathVariable Long reservationId,
            @RequestParam Long adminId,
            @RequestParam String comment) {
        log.info("接收到拒绝预约请求，预约ID: {}, 管理员ID: {}, 理由: {}",
                reservationId, adminId, comment);
        Reservation rejected = labReservationService.rejectReservation(reservationId, adminId, comment);
        return ResponseEntity.ok(rejected);
    }

    @PostMapping("/reservations/{reservationId}/cancel")
    public ResponseEntity<Reservation> cancelReservation(
            @PathVariable Long reservationId,
            @RequestParam Long userId) {
        log.info("接收到取消预约请求，预约ID: {}, 用户ID: {}", reservationId, userId);
        Reservation cancelled = labReservationService.cancelReservation(reservationId, userId);
        return ResponseEntity.ok(cancelled);
    }
}
