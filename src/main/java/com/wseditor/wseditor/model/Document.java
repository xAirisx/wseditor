package com.wseditor.wseditor.model;

import org.springframework.web.socket.WebSocketSession;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Entity
@Table(name="Document")
public class Document {

    public enum DocumentType {MAIN, EXTRA}
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;


    @Column(name = "text")
    private String text;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private DocumentType type;

    @Column(name = "maindoc_id")
    private Integer mainDocId;




    public Document()
    {

    }
    public Document(String name, String text, Integer mainDocId) {
        this.text = text;
        this.name = name;
        this.mainDocId = mainDocId;
    }
    public Document(String name) {

        this.name = name;
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getType() {
        return type.toString();
    }

    public void setType(DocumentType type) {
        this.type=type;
    }

    public Integer getMainDocId() {
        return mainDocId;
    }

    public void setMainDocId(Integer mainDocId) {
        this.mainDocId = mainDocId;
    }

}

