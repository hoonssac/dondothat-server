package org.bbagisix.user.controller;

import org.bbagisix.user.service.OAuth2Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/login/oauth2/code")
@RequiredArgsConstructor
public class OAuth2Controller {

	private final OAuth2Service oAuth2Service;

	@GetMapping("/google")
	public ResponseEntity<?> googleCallback(@RequestParam("code") String code) {
		String accessToken = oAuth2Service.getGoogleAccessToken(code);
		return ResponseEntity.ok("GOOGLE Access Token: " + accessToken);
	}
}
