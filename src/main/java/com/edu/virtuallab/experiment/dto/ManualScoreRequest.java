package com.edu.virtuallab.experiment.dto;

import java.math.BigDecimal;

public class ManualScoreRequest {
    private String sessionId;
    private BigDecimal score;
    private String comment;

    // Getter and Setter
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
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
