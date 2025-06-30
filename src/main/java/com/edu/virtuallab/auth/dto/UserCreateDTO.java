package com.edu.virtuallab.auth.dto;

import java.util.List;

public class UserCreateDTO {
    private String username;
    private String password;
    private String phone;
    private String email;
    private String realName;
    private String studentId;
    private String department;
    private String major;
    private String grade;
    private String className;
    private List<Long> roleIds; // 角色ID列表
    private Long departmentId; // 所属院系ID
    private String userType; // 用户类型：SYSTEM_ADMIN, DEPARTMENT_ADMIN, TEACHER, STUDENT, GUEST
    private String reason; // 创建原因

    // getter/setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public List<Long> getRoleIds() { return roleIds; }
    public void setRoleIds(List<Long> roleIds) { this.roleIds = roleIds; }
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
} 