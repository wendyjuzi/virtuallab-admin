package com.edu.virtuallab.experiment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "laboratories")
public class Laboratory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lab_id")
    private Integer labId;

    @Column(name = "lab_name", nullable = false, length = 100)
    private String labName;

    @Column(nullable = false, length = 100)
    private String location;

    @Column(nullable = false)
    private Integer capacity;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('available', 'maintenance', 'unavailable') DEFAULT 'available'")
    private LabStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "laboratory", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    // 枚举定义
    public enum LabStatus {
        available, maintenance, unavailable
    }
}
