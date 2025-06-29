package com.edu.virtuallab.experiment.service.impl;

import com.edu.virtuallab.experiment.dao.StudentProjectProgressDao;
import com.edu.virtuallab.experiment.dto.ProgressDTO;
import com.edu.virtuallab.experiment.model.StudentProjectProgress;
import com.edu.virtuallab.experiment.service.ProgressService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgressServiceImpl implements ProgressService {

    @Autowired
    private StudentProjectProgressDao studentProjectProgressDao;

    @Override
    public List<ProgressDTO> getProgressByProjectId(Long projectId) {
        List<StudentProjectProgress> list = studentProjectProgressDao.findByProjectId(projectId);
        return list.stream().map(item -> {
            ProgressDTO dto = new ProgressDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());
    }

//    @Override
//    public ProgressDTO getProgress(Long studentId, Long projectId) {
//        StudentProjectProgress progress = studentProjectProgressDao.findByStudentIdAndProjectId(studentId, projectId);
//        if (progress == null) return null;
//        ProgressDTO dto = new ProgressDTO();
//        BeanUtils.copyProperties(progress, dto);
//        return dto;
//    }
//
//    @Override
//    public boolean saveOrUpdateProgress(ProgressDTO progressDTO) {
//        StudentProjectProgress entity = new StudentProjectProgress();
//        BeanUtils.copyProperties(progressDTO, entity);
//        // 你可以自己判断是否存在再决定 insert 还是 update，也可以交给 SQL
//        return studentProjectProgressDao.saveOrUpdate(entity) > 0;
//    }
}

