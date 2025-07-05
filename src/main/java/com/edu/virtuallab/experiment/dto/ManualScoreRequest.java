package com.edu.virtuallab.experiment.dto;

import java.math.BigDecimal;

public class ManualScoreRequest {
    private Long sessionId;
    private BigDecimal score;
    private String comment;

    // Getter and Setter
    public Long getSessionId() {
        return sessionId;
    }
    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public BigDecimal getScore() {
        return score;
    }
    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}
