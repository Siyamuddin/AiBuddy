package com.example.SpringAI.Controller;

import com.example.SpringAI.Model.LocalUser;
import com.example.SpringAI.Repository.LocalUserRepo;
import com.example.SpringAI.Repository.SlideRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
@Slf4j
public class ViewController {
    @Autowired
    private LocalUserRepo localUserRepo;
    @Autowired
    private SlideRepo slideRepo;


    @GetMapping("/users")
    public String listUsers() {
        return "listUser";  // This refers to the `listUsers.html` template in `src/main/resources/templates/`
    }


    @GetMapping("/profile")
    public String userProfile(@AuthenticationPrincipal OAuth2User principal, Model model) {

        String email = principal.getAttribute("email");
        // Fetch user data from the database
        LocalUser user=localUserRepo.findByEmail(email);
        model.addAttribute("user", user);
        int size=slideRepo.findAllByLocalUserId(user.getId()).size();
        model.addAttribute("size",size);
        return "profile";
    }




    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";  // Thymeleaf template "register.html"
    }

    @GetMapping("/class")
    public String userClass() {
        return "userClass";  // Thymeleaf template "register.html"
    }

    @GetMapping("/upload/slide")
    public String uploadSlide(@AuthenticationPrincipal OAuth2User principal, Model model) {

        String email = principal.getAttribute("email");
        // Fetch user data from the database
        LocalUser user=localUserRepo.findByEmail(email);
        model.addAttribute("user", user);
        int size=slideRepo.findAllByLocalUserId(user.getId()).size();
        model.addAttribute("size",size);
        return "uploadSlide";  // Thymeleaf template "register.html"
    }

    @GetMapping("/get/all/slide")
    public String getAllSlide(@AuthenticationPrincipal OAuth2User principal, Model model) {
        String email = principal.getAttribute("email");
        // Fetch user data from the database
        LocalUser user=localUserRepo.findByEmail(email);
        int size=slideRepo.findAllByLocalUserId(user.getId()).size();
        model.addAttribute("user", user);
        model.addAttribute("size",size);
        return "allSlide";  // Thymeleaf template "register.html"
    }

}
