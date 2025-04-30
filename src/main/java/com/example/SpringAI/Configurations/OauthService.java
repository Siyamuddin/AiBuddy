package com.example.SpringAI.Configurations;

import com.example.SpringAI.Model.LocalUser;
import com.example.SpringAI.Repository.LocalUserRepo;
import com.example.SpringAI.Services.ServiceImpl.MailSenderServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.List;
@Component
@Slf4j
public class OauthService implements AuthenticationSuccessHandler {
    @Autowired
    private LocalUserRepo localUserRepo;
    @Autowired
    private MailSenderServices mailSenderServices;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
        String email = user.getAttribute("email").toString();
        String firstName = user.getAttribute("given_name").toString();
        String lastName = user.getAttribute("family_name").toString();
        String image = user.getAttribute("picture");

        List<LocalUser> users = localUserRepo.findByEmailContainingIgnoreCase(email);
        if (users.size() == 0) {
            LocalUser localUser = new LocalUser();
            localUser.setProvider(user.getAuthorities().toString());
            localUser.setProviderId(user.getAttribute("sub").toString());
            localUser.setEmail(email);
            localUser.setFirstName(firstName);
            localUser.setLastName(lastName);
            localUser.setImage(image);

            LocalUser saved = localUserRepo.save(localUser);
            String subject = "Welcome to AiBuddy, " + firstName + "!";
            String body = "Hi " + firstName + " " + lastName + ",\n\n" +
                    "We’re thrilled to welcome you to AiBuddy! 🎉\n\n" +
                    "Get ready to explore AI-powered solutions that make your journey smarter and more exciting. If you need any assistance, feel free to reach out to us.\n\n" +
                    "Best Regards,\n" +
                    "The AiBuddy Team";

            mailSenderServices.sendEmail(email, subject, body);
            new DefaultRedirectStrategy().sendRedirect(request, response, "/view/profile");
        } else {

            new DefaultRedirectStrategy().sendRedirect(request, response, "/view/profile");
        }

    }

}
