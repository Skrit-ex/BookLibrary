package com.example.booklib.controller;

import com.example.booklib.dto.LoginDto;
import com.example.booklib.dto.RegUserDto;
import com.example.booklib.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/home")
    public String home(Model model) {
//NOTE:        User user = userService.getCurrentUser();
//        model.addAttribute("username", user.getUserName());
        Authentication auth = userService.isAuthenticated();
        if (auth instanceof AnonymousAuthenticationToken) {
            model.addAttribute("username", "Guest");
        } else {
            model.addAttribute("username", auth.getName());
        }
        return "home";
    }

    @GetMapping("/regUser")
    public String regUser(Model model) {
        model.addAttribute("regUserDto", new RegUserDto());
        return "registration";
    }

    @PostMapping("/regUser")
    public String saveUser(@ModelAttribute("regUserDto") RegUserDto regUserDto,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("Error in saving user");
            return "registrationError";
        }
        userService.saveUser(regUserDto);
        return "redirect:/user/home";
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }
    @GetMapping("/loginError")
    public String loginError() {
        return "loginError";
    }

}
