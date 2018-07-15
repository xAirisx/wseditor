package com.wseditor.wseditor.domain;

import javax.persistence.*;

@Entity
@Table(name="Document")
public class Document {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name="version")
    private int version;

    @Column(name="user_id")
    private String userId;

    @Column(name = "text")
    private String text;

    public Document() {
    }

    public Document(int version, String userId, String text) {
        this.version = version;
        this.userId = userId;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}

