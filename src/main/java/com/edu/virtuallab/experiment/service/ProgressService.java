package com.edu.virtuallab.experiment.service;

import com.edu.virtuallab.experiment.dto.ProgressDTO;

import java.util.List;

public interface ProgressService {

    /**
     * 获取某个实验项目下所有学生的进度
     */
    List<ProgressDTO> getProgressByProjectId(Long projectId);

//    /**
//     * 获取某个学生在某个实验项目下的进度
//     */
//    ProgressDTO getProgress(Long studentId, Long projectId);
//
//    /**
//     * 新增或更新进度记录
//     */
//    boolean saveOrUpdateProgress(ProgressDTO progressDTO);
}

