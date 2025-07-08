package com.edu.virtuallab.experiment.model;

import com.edu.virtuallab.auth.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Transient;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "lab_id", nullable = false)
    private Laboratory laboratory;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Lob
    @Column(nullable = false)
    private String purpose;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('pending', 'approved', 'rejected', 'cancelled') DEFAULT 'pending'")
    private ReservationStatus status;


    @JoinColumn(name = "admin_id")
    private Long adminId;

    @Lob
    @Column(name = "admin_comment")
    private String adminComment;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    private String labName;
    private String username;

    // 枚举定义
    public enum ReservationStatus {
        pending, approved, rejected, cancelled
    }

    // 省略getter和setter方法
}