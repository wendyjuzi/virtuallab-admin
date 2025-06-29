package com.edu.virtuallab.progress.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.virtuallab.progress.dto.ProgressDTO;
import com.edu.virtuallab.progress.entity.StudentProjectProgress;
import com.edu.virtuallab.progress.mapper.ProgressMapper;
import com.edu.virtuallab.progress.service.ProgressService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgressServiceImpl extends ServiceImpl<ProgressMapper, StudentProjectProgress> implements ProgressService {

    @Override
    public List<ProgressDTO> getProgressByProjectId(Long projectId) {
        List<StudentProjectProgress> list = list(new QueryWrapper<StudentProjectProgress>()
                .eq("project_id", projectId));
        return list.stream().map(item -> {
            ProgressDTO dto = new ProgressDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public ProgressDTO getProgress(Long studentId, Long projectId) {
        StudentProjectProgress progress = getOne(new QueryWrapper<StudentProjectProgress>()
                .eq("student_id", studentId)
                .eq("project_id", projectId));
        if (progress == null) return null;
        ProgressDTO dto = new ProgressDTO();
        BeanUtils.copyProperties(progress, dto);
        return dto;
    }

    @Override
    public boolean saveOrUpdateProgress(ProgressDTO progressDTO) {
        StudentProjectProgress entity = new StudentProjectProgress();
        BeanUtils.copyProperties(progressDTO, entity);
        return saveOrUpdate(entity);
    }
}

