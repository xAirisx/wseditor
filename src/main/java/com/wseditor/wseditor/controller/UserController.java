package com.wseditor.wseditor.controller;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import com.wseditor.wseditor.model.User;
import com.wseditor.wseditor.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    UserServiceImpl userService;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView showAuthPage(ModelAndView modelAndView, User user) {
        modelAndView.addObject("user", user);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView processAuthForm(ModelAndView modelAndView, User user) {

        User userExists = userService.findByLogin(user.getLogin());
        if (encoder.matches(user.getPassword(), userExists.getPassword())) {

            System.out.println(userExists.getLogin());
            modelAndView.setViewName("docs");
            return modelAndView;
        }
        else
        {
            modelAndView.addObject("errorMessage", "The password is incorrect!");
            modelAndView.setViewName("index");
            return modelAndView;
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView showRegistrationPage(ModelAndView modelAndView, User user) {
        modelAndView.addObject("user", user);
        modelAndView.setViewName("1");
        return modelAndView;
    }

    // Process form input data
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView processRegistrationForm(ModelAndView modelAndView, @Valid User user, BindingResult bindingResult) {

        User userExists = userService.findByLogin(user.getLogin());
        System.out.println(userExists);

        if (userExists != null) {
            modelAndView.addObject("errorMessage", "Oops!  There is already a user registered with the login provided.");
            modelAndView.setViewName("1");
            bindingResult.reject("login");
        }

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("1");
        } else {

            Zxcvbn passwordCheck = new Zxcvbn();
            Strength strength = passwordCheck.measure(user.getPassword());

            if (strength.getScore() < 3) {

                bindingResult.reject("password");
                modelAndView.addObject("errorMessage", "Your password is too weak.  Choose a stronger one.");
                return modelAndView;
            } else {

                user.setPassword(encoder.encode(user.getPassword()));
                userService.saveUser(user);
                modelAndView.addObject("successMessage", "Successfully registered!");
                modelAndView.setViewName("index");
                return modelAndView;
            }
        }
        return modelAndView;
    }


}
