package com.wseditor.wseditor.domain;

import javax.persistence.*;

@Entity
@Table(name="Document")
public class Document {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;


    @Column(name = "text")
    private String text;

    @Column(name = "name")
    private String name;


    public Document()
    {

    }
    public Document(String name, String text) {
        this.text = text;
        this.name = name;
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
}

