package com.edu.virtuallab.experiment.dto;

public class ChatMessage {
    private Long studentId;
    private Long teamId;
    private String content;

    // 构造器、getter、setter

    public Long getStudentId() {
        return studentId;
    }
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
    public Long getTeamId() {
        return teamId;
    }
    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
