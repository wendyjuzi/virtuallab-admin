package com.edu.virtuallab.experiment.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("equipment")
public class Equipment {
    @TableId(value = "equipment_id", type = IdType.AUTO)
    private Integer equipmentId;

    @TableField("lab_id")
    private Integer labId;

    @TableField("equipment_name")
    private String equipmentName;

    private String model;
    private String category;

    @TableField("serial_number")
    private String serialNumber;

    @TableField("purchase_date")
    private Date purchaseDate;

    private BigDecimal price;
    private String manufacturer;

    private String status;

    private String description;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Date createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;

    // 设备状态枚举
    public enum Status {
        normal, maintenance, scrapped, borrowed
    }
}
