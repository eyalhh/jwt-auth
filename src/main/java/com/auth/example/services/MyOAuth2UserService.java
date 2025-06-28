package com.auth.example.services;

import com.auth.example.models.User;
import com.auth.example.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MyOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired private UserRepository userRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        System.out.println(1);
        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(request);

        String provider = request.getClientRegistration().getRegistrationId();

        Map<String, Object> attributes = oauth2User.getAttributes();
        String email = (String) attributes.get("email");

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .email(email)
                    .password("oauth2")
                    .provider(provider)
                    .emailValidated(true)
                    .build();
            return userRepository.save(newUser);
        });

        return oauth2User;
    }
}
