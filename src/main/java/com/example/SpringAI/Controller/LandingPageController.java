package com.example.SpringAI.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class LandingPageController {

    @GetMapping()
    public String landingPage() {

        return "index"; // This should map to a `landing.html` file in your templates folder
    }
}