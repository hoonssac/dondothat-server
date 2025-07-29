package org.bbagisix.oauth2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.bbagisix.oauth2.dto.CustomOAuth2User;
import org.bbagisix.oauth2.dto.UserDTO;
import org.bbagisix.oauth2.dto.GoogleResponse;
import org.bbagisix.oauth2.dto.NaverResponse;
import org.bbagisix.oauth2.dto.OAuth2Response;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("OAuth2 로그인 진행중...");
        
        // 기본 OAuth2UserService로 사용자 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);
        
        // OAuth2 제공자 정보 (google, naver 등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String name = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        UserDTO userDTO = new UserDTO();
        userDTO.setName(name);
        userDTO.setUsername(oAuth2User.getName());
        userDTO.setRole("ROLE_USER");

        return new CustomOAuth2User(userDTO);
    }
}
