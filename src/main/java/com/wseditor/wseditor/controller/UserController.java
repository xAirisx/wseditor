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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import javax.validation.Valid;


@Controller
public class UserController {

    @Autowired
    UserServiceImpl userService;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView showAuthPage(ModelAndView modelAndView, User user, @RequestParam(value = "error", required = false) String errorMessage, @RequestParam(value = "successMessage", required = false) String successMessage,
                                     @RequestParam(value = "logout", required = false) String logout) {
        if (successMessage != null) {

            modelAndView.addObject("successMessage", successMessage);
        }

        if (errorMessage != null) {

            modelAndView.addObject("errorMessage", "Invalid username or password!");
        }
        if (logout != null) {
            modelAndView.addObject("msg", "You've been logged out successfully.");
        }
        modelAndView.setViewName("index");
        return modelAndView;
    }


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView showRegistrationPage(ModelAndView modelAndView, User user) {
        modelAndView.addObject("user", user);
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView processRegistrationForm(ModelAndView modelAndView, @Valid User user, BindingResult bindingResult) {

        User userExists = userService.findByLogin(user.getLogin());

        if (userExists != null) {

            modelAndView.addObject("errorMessage", "Login is already taken");
            bindingResult.reject("login");
            return modelAndView;
        }

        Zxcvbn passwordCheck = new Zxcvbn();
        Strength strength = passwordCheck.measure(user.getPassword());

        if (strength.getScore() < 1) {

            bindingResult.reject("password");
            modelAndView.addObject("errorMessage", "Your password should be an appropriate length, contain some symbols and mixed-case letters. Avoid using only dictionary words or sequential numbers or characters. ");
            return modelAndView;

        } else {

            user.setPassword(encoder.encode(user.getPassword()));
            userService.saveUser(user);
            modelAndView = new ModelAndView(new RedirectView("/"));
            modelAndView.addObject("successMessage", "Successfully registered");
            return modelAndView;
        }

    }

}
