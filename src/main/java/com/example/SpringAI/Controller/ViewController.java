package com.example.SpringAI.Controller;

import com.example.SpringAI.Model.LocalUser;
import com.example.SpringAI.Repository.LocalUserRepo;
import com.example.SpringAI.Repository.SlideRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${DELETE}")
    private  String deleteUserURL;
    @Value("${UPDATE}")
    private  String updateUserURL;
    @Value("${DELETE_REDIRECT}")
    private  String deleteRelocateURL;
    @Value("${UPLOAD_SLIDE}")
    private  String slideUploadURL;
    @Value("${GS}")
    private  String genrateSummaryURL;
    @Value("${GMCQ}")
    private  String generateMCQURL;
    @Value("${GSQ}")
    private  String generateShortQuestionURL;
    @Value("${DELETE_SLIDE}")
    private  String deleteSlideURL;
    @Value("${GET_ALL}")
    private  String getAllUserURL;


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


        model.addAttribute("deleteUserURL",deleteUserURL);
        model.addAttribute("updateUserURL",updateUserURL);
        model.addAttribute("deleteRelocateURL",deleteRelocateURL);
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
        model.addAttribute("slideUploadURL",slideUploadURL);

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

        model.addAttribute("genrateSummaryURL",genrateSummaryURL);
        model.addAttribute("generateMCQURL",generateMCQURL);
        model.addAttribute("generateShortQuestionURL",generateShortQuestionURL);
        model.addAttribute("deleteSlideURL",deleteSlideURL);
        model.addAttribute("getAllUserURL",getAllUserURL);
        return "allSlide";  // Thymeleaf template "register.html"
    }

    @GetMapping("/chat")
    public String ChatLawyer(@AuthenticationPrincipal OAuth2User principal, Model model) {
        String email = principal.getAttribute("email");
        // Fetch user data from the database
        LocalUser user=localUserRepo.findByEmail(email);
        model.addAttribute("user", user);
        return "LawyerChat";  // Thymeleaf template "register.html"
    }

}
