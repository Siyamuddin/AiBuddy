package com.example.SpringAI.Configurations;

import com.example.SpringAI.DTOs.UserDTO;
import com.example.SpringAI.Model.LocalUser;
import com.example.SpringAI.Repository.LocalUserRepo;
import com.example.SpringAI.Services.ServiceImpl.MailSenderServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizationSuccessHandler;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
@Component
@Slf4j
public class OauthService implements AuthenticationSuccessHandler {
    @Autowired
    private LocalUserRepo localUserRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MailSenderServices mailSenderServices;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//            new DefaultRedirectStrategy().sendRedirect(request,response,"/view/get/all/slide");
        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
        String email = user.getAttribute("email").toString();
        String firstName = user.getAttribute("given_name").toString();
        String lastName = user.getAttribute("family_name").toString();
        String image = user.getAttribute("picture");

        List<LocalUser> users = localUserRepo.findByEmailContainingIgnoreCase(email);
        if (users.size() == 0) {
            LocalUser localUser = new LocalUser();
            localUser.setProvider("GOOGLE");
            localUser.setProviderId(user.getAttribute("sub").toString());
            localUser.setEmail(email);
            localUser.setFirstName(firstName);
            localUser.setLastName(lastName);
            localUser.setImage(image);

            LocalUser saved = localUserRepo.save(localUser);
            mailSenderServices.sendEmail(email,"AiBuddy","Hi,"+firstName+" "+lastName+"\nWelcome to AiBuddy");
            new DefaultRedirectStrategy().sendRedirect(request, response, "/view/profile");
        } else {

            new DefaultRedirectStrategy().sendRedirect(request, response, "/view/profile");
        }

    }

}
