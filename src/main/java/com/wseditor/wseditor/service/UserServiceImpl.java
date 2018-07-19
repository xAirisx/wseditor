package com.wseditor.wseditor.service;

import com.wseditor.wseditor.model.User;
import com.wseditor.wseditor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user)
    {
        userRepository.save(user);
    }

    public User findByLogin(String login){

        return userRepository.findByLogin(login);
    }

}
