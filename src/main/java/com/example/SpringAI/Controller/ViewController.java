package com.example.SpringAI.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class ViewController {
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";  // Thymeleaf template "register.html"
    }

    @GetMapping("/users")
    public String listUsers() {
        return "listUser";  // This refers to the `listUsers.html` template in `src/main/resources/templates/`
    }

    @GetMapping("/class")
    public String userClass() {
        return "userClass";  // Thymeleaf template "register.html"
    }

    @GetMapping("/upload/slide")
    public String uploadSlide() {
        return "uploadSlide";  // Thymeleaf template "register.html"
    }

    @GetMapping("/get/all/slide")
    public String getAllSlide() {
        return "allSlide";  // Thymeleaf template "register.html"
    }

}
