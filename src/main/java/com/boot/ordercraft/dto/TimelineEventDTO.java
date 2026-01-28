package com.boot.ordercraft.dto;

import java.time.LocalDateTime;

public class TimelineEventDTO {
    private LocalDateTime date;
    private String status;
    private String note;

    public TimelineEventDTO() {}

    public TimelineEventDTO(LocalDateTime date, String status, String note) {
        this.date = date;
        this.status = status;
        this.note = note;
    }

    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
}
