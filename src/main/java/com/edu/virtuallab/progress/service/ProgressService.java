package com.edu.virtuallab.progress.service;

import com.edu.virtuallab.progress.dto.ProgressDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.virtuallab.progress.entity.StudentProjectProgress;

import java.util.List;

public interface ProgressService extends IService<StudentProjectProgress> {

    List<ProgressDTO> getProgressByProjectId(Long projectId);

    ProgressDTO getProgress(Long studentId, Long projectId);


    boolean saveOrUpdateProgress(ProgressDTO progressDTO);
}
