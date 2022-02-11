package com.example.events.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Repetition {
    private String id;
    private String description;
    private LocalDateTime created;
    private LocalDateTime modified;
    private boolean completed;
    public Repetition(){
        LocalDateTime date = LocalDateTime.now();
        this.id = UUID.randomUUID().toString();
        this.created = date;
        this.modified = date;
    }

    public Repetition(String description) {
        this();
        this.description = description;
    }

    public void setId(String id) {
    }

    public String getId() {
        return null;
    }
}
