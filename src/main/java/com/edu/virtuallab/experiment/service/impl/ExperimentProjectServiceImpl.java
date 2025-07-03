package com.edu.virtuallab.experiment.service.impl;

import com.edu.virtuallab.experiment.dao.ExperimentProjectClassDao;
import com.edu.virtuallab.experiment.dto.ExperimentProjectPublishRequest;
import com.edu.virtuallab.experiment.model.ExperimentProject;
import com.edu.virtuallab.experiment.dao.ExperimentProjectDao;
import com.edu.virtuallab.experiment.service.ExperimentProjectService;
import com.edu.virtuallab.experiment.dao.StudentClassDao;
import com.edu.virtuallab.experiment.dao.StudentProjectProgressDao;
import com.edu.virtuallab.experiment.model.StudentProjectProgress;
import com.edu.virtuallab.experiment.service.ProjectTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Date;

@Service
public class ExperimentProjectServiceImpl implements ExperimentProjectService {
    @Autowired
    private ExperimentProjectDao projectDao;
    @Autowired
    private ExperimentProjectClassDao projectClassDao;
    @Autowired
    private ExperimentProjectClassDao experimentProjectClassDao;
    @Autowired
    private StudentClassDao studentClassDao; // ✅ 解决 Cannot resolve symbol

    @Autowired
    private StudentProjectProgressDao studentProjectProgressDao; // ✅ 解决 Cannot resolve symbol

    @Autowired
    private ProjectTeamService projectTeamService;


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
    public Long publishProject(ExperimentProjectPublishRequest request, String createdBy) {
        ExperimentProject project = new ExperimentProject();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setCategory(request.getCategory());
        project.setLevel(request.getLevel());
        project.setImageUrl(request.getImageUrl());
        project.setVideoUrl(request.getVideoUrl());
//        project.setProjectType(request.getProjectType());
        project.setAuditStatus("pending");

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
        // 3. 查询这些班级的所有学生
        System.out.println("开始查询班级对应的学生ID...");
        List<Long> studentsInClasses = studentClassDao.findStudentIdsByClassIds(request.getClassIds());
        System.out.println("查询到学生ID列表: " + studentsInClasses);

        Set<Long> studentIds = new HashSet<>(studentsInClasses);
        System.out.println("去重后学生ID集合大小: " + studentIds.size());

        // 4. 构造进度记录
        List<StudentProjectProgress> progressList = studentIds.stream().map(studentId -> {
            StudentProjectProgress progress = new StudentProjectProgress();
            progress.setProjectId(project.getId());
            progress.setStudentId(studentId);
            progress.setStatus("not_started"); // 初始状态
            return progress;
        }).collect(Collectors.toList());

        System.out.println("准备批量插入进度记录，数量: " + progressList.size());

        // 5. 批量插入进度记录
        if (!progressList.isEmpty()) {
            try {
                studentProjectProgressDao.insertBatch(progressList);
                System.out.println("批量插入进度记录成功");
            } catch (Exception e) {
                System.out.println("批量插入进度记录失败");
                e.printStackTrace();
            }
        } else {
            System.out.println("没有进度记录需要插入");
        }



//        return 1; // 成功
        return project.getId();  // 返回自增ID

    }


    @Override
    public List<ExperimentProject> getProjectsByCreatedBy(String createdBy) {
        return projectDao.getProjectsByCreatedBy(createdBy);
    }

    @Override
    public List<ExperimentProject> listPage(String category, String sort, int page, int size) {
        int offset = (page - 1) * size;
        return projectDao.listPage(category, sort, offset, size);
    }

    @Override
    public long countPage(String category) {
        return projectDao.countPage(category);
    }
    @Override
    public List<Long> getTeamsByStudentId(Long studentId) {
        return projectDao.getTeamsByStudentId(studentId);
    }

    @Override
    public void save(ExperimentProject project) {
        if (project.getId() == null) {
            project.setCreateTime(new Date());
            project.setUpdateTime(new Date());
            projectDao.insert(project);
        } else {
            project.setUpdateTime(new Date());
            projectDao.update(project);
        }
    }

    @Override
    public void approve(Long id, boolean approve, String comment) {
        ExperimentProject project = projectDao.findById(id);
        if (project == null) throw new RuntimeException("实验不存在");
        project.setStatus(approve ? "APPROVED" : "REJECTED");
        project.setApproveComment(comment);
        project.setUpdateTime(new Date());
        projectDao.update(project);
    }

    @Override
    public ExperimentProject findById(Long id) {
        return projectDao.findById(id);
    }

    @Override
    public Long getStudentIdByUserId(Long userId) {
        return projectDao.getStudentIdByUserId(userId);
    }
}