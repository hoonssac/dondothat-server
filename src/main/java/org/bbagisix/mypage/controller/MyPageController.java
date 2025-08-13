package org.bbagisix.mypage.controller;

import java.util.List;

import org.bbagisix.mypage.domain.MyPageAccountDTO;
import org.bbagisix.mypage.domain.UserChallengeDTO;
import org.bbagisix.mypage.service.MyPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
@Log4j2
public class MyPageController {

	private final MyPageService myPageService;

	@GetMapping("/accounts")
	public ResponseEntity<MyPageAccountDTO> getUserAccounts(Authentication authentication) {
		MyPageAccountDTO response = myPageService.getUserAccountData(authentication);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/challenges/completed")
	public ResponseEntity<List<UserChallengeDTO>> getUserChallenges(Authentication authentication) {
		List<UserChallengeDTO> challenges = myPageService.getUserChallenges(authentication);
		return ResponseEntity.ok(challenges);
	}
	// @GetMapping("/tier")          // 뱃지 목록
}
