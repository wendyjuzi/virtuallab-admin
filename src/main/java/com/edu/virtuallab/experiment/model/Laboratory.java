package com.edu.virtuallab.experiment.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("laboratories") // 明确指定表名
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

    @Column(nullable = false, length = 50)
    private String department;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('available', 'maintenance', 'unavailable') DEFAULT 'available'")
    private LabStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @TableField(exist = false)  // 告诉MyBatis-Plus这不是数据库字段
    @OneToMany(mappedBy = "laboratory", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    // 枚举定义
    public enum LabStatus {
        available, maintenance, unavailable
    }
}
