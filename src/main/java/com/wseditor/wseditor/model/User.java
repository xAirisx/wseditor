package com.wseditor.wseditor.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="User")
public class User {

    User() { }

    User(String login, String password)
    {
        this.login = login;
        this.password = password;

    }

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int userId;

    @NotNull
    @NotEmpty(message = "Please provide your login name")
    @Column(name = "login")
    private String login;

    @NotNull
    @NotEmpty(message = "Please provide your password")
    @Column(name = "password")
    private String password;

    public int getUserId() {
        return userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return this.login + "/" + this.password;
    }

}
