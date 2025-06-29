package com.edu.virtuallab.experiment.service.impl;

import com.edu.virtuallab.experiment.dao.ExperimentProjectClassDao;
import com.edu.virtuallab.experiment.dto.ExperimentProjectPublishRequest;
import com.edu.virtuallab.experiment.model.ExperimentProject;
import com.edu.virtuallab.experiment.dao.ExperimentProjectDao;
import com.edu.virtuallab.experiment.service.ExperimentProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExperimentProjectServiceImpl implements ExperimentProjectService {
    @Autowired
    private ExperimentProjectDao projectDao;
    @Autowired
    private ExperimentProjectClassDao projectClassDao;
    @Autowired
    private ExperimentProjectClassDao experimentProjectClassDao;

    @Override
    public int create(ExperimentProject project) {
        return projectDao.insert(project);
    }

    @Override
    public int update(ExperimentProject project) {
        return projectDao.update(project);
    }

    @Override
    public int delete(Long id) {
        return projectDao.delete(id);
    }

    @Override
    public ExperimentProject getById(Long id) {
        return projectDao.selectById(id);
    }

    @Override
    public List<ExperimentProject> listAll() {
        return projectDao.selectAll();
    }

    @Override
    public List<ExperimentProject> search(String category, String level, String keyword) {
        return projectDao.search(category, level, keyword);
    }

    @Override
    public void publishToClasses(Long projectId, List<Long> classIds) {
        for (Long classId : classIds) {
            projectClassDao.insert(projectId, classId);
        }
    }

    @Override
    public int publishProject(ExperimentProjectPublishRequest request, String createdBy) {
        ExperimentProject project = new ExperimentProject();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setCategory(request.getCategory());
        project.setLevel(request.getLevel());
        project.setImageUrl(request.getImageUrl());
        project.setVideoUrl(request.getVideoUrl());

        // ✅ 设置创建者用户名
        project.setCreatedBy(createdBy);

        // 1. 插入实验项目信息
        projectDao.insert(project);

        System.out.println("请求内容：" + request);
        System.out.println("班级ID列表：" + request.getClassIds());

        // 2. 插入班级关联
        for (Long classId : request.getClassIds()) {
            System.out.println("准备插入：projectId=" + project.getId() + " classId=" + classId);
            try {
                int rows = experimentProjectClassDao.insert(project.getId(), classId);
                System.out.println("插入结果：rows=" + rows);
            } catch (Exception e) {
                System.out.println("插入失败！");
                e.printStackTrace(); // 打印具体异常
            }
        }

        return 1; // 成功
    }


    @Override
    public List<ExperimentProject> getProjectsByCreatedBy(String createdBy) {
        return projectDao.getProjectsByCreatedBy(createdBy);
    }


} 