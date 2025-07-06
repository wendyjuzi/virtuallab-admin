package com.edu.virtuallab.auth.model;

public class UserRegisterDTO {
    private String username;
    private String password;
    private String email;
    private String realName;
    private String studentId;
    private String department;
    private String major;
    private String grade;
    private String className;
    private String roleCode;
    private String userType; // 用户类型：user/admin
    private String emailCode; // 邮箱验证码（管理员用）
    private String code;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
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
    public String getRoleCode() { return roleCode; }
    public void setRoleCode(String roleCode) { this.roleCode = roleCode; }
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
    public String getEmailCode() {
        return emailCode;
    }
    public void setEmailCode(String emailCode) {
        this.emailCode = emailCode;
        this.code = emailCode;
    }
    public String getCode() {
        return code != null ? code : emailCode;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public User toUser() {
        User user = new User();
        user.setUsername(this.username);
        user.setPassword(this.password);
        user.setEmail(this.email);
        user.setRealName(this.realName);
        user.setStudentId(this.studentId);
        user.setDepartment(this.department);
        user.setMajor(this.major);
        user.setGrade(this.grade);
        user.setClassName(this.className);
        user.setStatus(User.STATUS_NORMAL);
        user.setCreateTime(new java.util.Date());
        user.setUpdateTime(new java.util.Date());
        return user;
    }
} 