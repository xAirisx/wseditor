package com.wseditor.wseditor.service;

import com.wseditor.wseditor.model.User;

public interface UserService {

    void saveUser(User user);
    User findByLogin(String login);
}
