package com.edu.virtuallab.experiment.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.virtuallab.audit.dao.ExperimentProjectMapper;
import com.edu.virtuallab.auth.dao.UserDao;
import com.edu.virtuallab.auth.dao.UserRoleDao;
import com.edu.virtuallab.auth.model.User;
import com.edu.virtuallab.auth.model.UserRole;
import com.edu.virtuallab.common.api.PageResult;
import com.edu.virtuallab.experiment.dao.*;
import com.edu.virtuallab.experiment.dto.ExperimentProjectPublishRequest;
import com.edu.virtuallab.experiment.dto.StudentExperimentProjectDTO;
import com.edu.virtuallab.experiment.model.ExperimentProject;
import com.edu.virtuallab.experiment.service.ExperimentProjectService;
import com.edu.virtuallab.experiment.model.StudentProjectProgress;
import com.edu.virtuallab.experiment.service.ProjectTeamService;
import com.edu.virtuallab.project.model.Project;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
    private ExperimentProjectMapper projectMapper;
    @Autowired
    private StudentProjectProgressDao studentProjectProgressDao; // ✅ 解决 Cannot resolve symbol
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private ProjectTeamService projectTeamService;
    @Autowired
    private ExperimentReportDao reportDao;

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
        project.setProjectType(request.getProjectType());
        project.setAuditStatus("pending");
        project.setPrinciple(request.getPrinciple());
        project.setPurpose(request.getPurpose());
        project.setMethod(request.getMethod());
        project.setSteps(request.getSteps());
        project.setScreenshot(request.getScreenshot());
        project.setSceneData(request.getSceneData());
        project.setExperimentParams(request.getExperimentParams());
        project.setSceneJsonPath(request.getSceneJsonPath());
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
    @Override
    public int markAsInProgress(Integer projectId, String studentId) {
        return projectDao.updateStatusToInProgress(projectId, studentId);
    }
    @Override
    public int markAsCompleted(Integer projectId, String studentId) {
        return projectDao.updateStatusToCompleted(projectId, studentId);
    }
    @Override
    public int countPendingGradingReports(String teacherName) {
        return projectDao.countPendingGradingReports(teacherName);
    }

    @Override
    public List<Integer> getScoresByProjectId(Long projectId) {
        return projectDao.getScoresByProjectId(projectId);
    }
    @Override
    public boolean updateProject(ExperimentProject project) {
        return projectDao.updateProject(project) > 0;
    }

    // 在实现类中添加实现
// ExperimentProjectServiceImpl.java
    @Override
    public Page<StudentExperimentProjectDTO> getProjectsByStudentId(
            Long studentId,
            String keyword,
            int pageNum,
            int pageSize) {

        System.out.println("===开始查询学生实验项目===");
        // 1. 根据学生ID查询班级ID列表
        List<Long> classIds = studentClassDao.findClassIdsByStudentId(studentId);
        if (classIds.isEmpty()) {
            return new Page<>(pageNum, pageSize);
        }

        // 2. 根据班级ID列表查询项目ID列表
        List<Long> projectIds = experimentProjectClassDao.findProjectIdsByClassIds(classIds);
        if (projectIds.isEmpty()) {
            return new Page<>(pageNum, pageSize);
        }

        // 3. 批量查询进度状态
        Map<Long, String> progressMap = studentProjectProgressDao.findByStudentId(studentId).stream()
                .filter(p -> projectIds.contains(p.getProjectId()))
                .collect(Collectors.toMap(
                        StudentProjectProgress::getProjectId,
                        StudentProjectProgress::getStatus
                ));

        // 修改成绩映射逻辑，处理null和类型转换
        Map<Long, BigDecimal> scoreMap = reportDao.findByStudentId(studentId).stream()
                .filter(r -> {
                    try {
                        Long.parseLong(r.getProjectId()); // 验证projectId可转为Long
                        return true;
                    } catch (NumberFormatException e) {
                        return false; // 跳过无效ID
                    }
                })
                .collect(Collectors.toMap(
                        r -> Long.parseLong(r.getProjectId()),
                        r -> r.getScore() != null ? r.getScore() : BigDecimal.ZERO // 处理null
                ));

        // 5. 根据项目ID列表分页获取项目详情
        // 计算偏移量
        long offset = (long) (pageNum - 1) * pageSize;

        // 查询总数
        long total = projectMapper.countApprovedProjectsByIds(projectIds, keyword);

        // 查询当前页数据
        List<ExperimentProject> records = projectMapper.selectApprovedProjectsByIdsManual(
                projectIds, keyword, offset, pageSize);

        // 创建Page对象
        Page<ExperimentProject> page = new Page<>(pageNum, pageSize);
        page.setTotal(total);
        page.setRecords(records);
        page.setPages((total + pageSize - 1) / pageSize); // 计算总页数

        // 6. 转换为DTO并添加进度状态和成绩
        List<StudentExperimentProjectDTO> dtoList = page.getRecords().stream()
                .map(project -> {
                    StudentExperimentProjectDTO dto = new StudentExperimentProjectDTO();
                    BeanUtils.copyProperties(project, dto);

                    // 设置进度状态，如果不存在则为"未开始"
                    dto.setProgressStatus(
                            progressMap.getOrDefault(project.getId(), "未开始")
                    );

                    // 设置成绩
                    // 修改成绩映射逻辑
                    dto.setScore(Optional.ofNullable(scoreMap.get(project.getId()))
                            .orElse(BigDecimal.ZERO)); // 处理null值

                    // 添加属性复制保护
                    try {
                        BeanUtils.copyProperties(project, dto);
                    } catch (Exception e) {
                        System.out.println("属性复制失败: project={}");
                        throw new RuntimeException("数据转换异常");
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        // 7. 创建分页结果
        Page<StudentExperimentProjectDTO> resultPage = new Page<>();
        BeanUtils.copyProperties(page, resultPage, "records");
        resultPage.setRecords(dtoList);

        return resultPage;
    }

    // 首页获取热门、最新、点赞、收藏项目排列
    @Override
    public PageResult<ExperimentProject> listWithSort(
            List<String> adminUsernames,
            String category,
            String level,
            String keyword,
            String sort,
            int page,
            int size
    ) {
        int offset = (page - 1) * size;
        List<ExperimentProject> projects = projectDao.listPageWithSort(
                adminUsernames, category, level, keyword, sort, offset, size
        );

        long total = projectDao.countWithSort(
                adminUsernames, category, level, keyword
        );

        return new PageResult<>(total, projects);
    }

    // 获取系统管理员的用户名列表
    public List<String> getAdminUsernames() {
        // 1. 获取系统管理员角色ID为1的用户ID列表
        List<UserRole> adminRoles = userRoleDao.findByRoleId(1L);
        if (adminRoles.isEmpty()) return Collections.emptyList();

        List<Long> userIds = adminRoles.stream()
                .map(UserRole::getUserId)
                .collect(Collectors.toList());

        // 2. 根据用户ID获取用户名列表
        List<User> users = userDao.findByIds(userIds);
        return users.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }
}