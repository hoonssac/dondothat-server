package org.bbagisix.challenge.controller;

import lombok.RequiredArgsConstructor;
import org.bbagisix.challenge.dto.ChallengeDTO;
import org.bbagisix.challenge.service.ChallengeService;
import org.bbagisix.exception.BusinessException;
import org.bbagisix.exception.ErrorCode;
import org.bbagisix.user.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/challenges")
@RequiredArgsConstructor
public class ChallengeController {

	private final ChallengeService challengeService;
	private final JwtUtil jwtUtil;

	// 1. challengeId가 없는 경우: /api/challenges 또는 /api/challenges/
	@GetMapping
	public ResponseEntity<ChallengeDTO> handleMissingChallengeId() {
		throw new BusinessException(ErrorCode.CHALLENGE_ID_REQUIRED);
	}

	// 2. challengeId가 명시되었지만 "null", 빈값, 숫자 아님 등 예외 처리 포함
	@GetMapping("/{challengeId}")
	public ResponseEntity<ChallengeDTO> getChallengeById(@PathVariable Long challengeId) {
		try {
			ChallengeDTO result = challengeService.getChallengeById(challengeId);
			return ResponseEntity.ok(result);
		} catch (NumberFormatException e) {
			throw new BusinessException(ErrorCode.CHALLENGE_ID_REQUIRED);
		}
	}

	// 3. 챌린지 참여 API (JWT 토큰 기반)
	@PostMapping("/{challengeId}/join")
	public ResponseEntity<String> joinChallenge(
		@PathVariable String challengeId,
		HttpServletRequest request) {

		// challengeId 유효성 검사
		if (challengeId == null || challengeId.trim().isEmpty() || challengeId.equalsIgnoreCase("null")) {
			throw new BusinessException(ErrorCode.CHALLENGE_ID_REQUIRED);
		}

		try {
			Long parsedChallengeId = Long.parseLong(challengeId);

			// JWT 토큰에서 사용자 ID 추출
			Long userId = getCurrentUserId(request);

			challengeService.joinChallenge(parsedChallengeId, userId);
			return ResponseEntity.ok("챌린지 참여가 완료되었습니다.");

		} catch (NumberFormatException e) {
			throw new BusinessException(ErrorCode.CHALLENGE_ID_REQUIRED);
		}
	}

	// 4. 챌린지 탈퇴 API (JWT 토큰 기반)
	@DeleteMapping("/{challengeId}/leave")
	public ResponseEntity<String> leaveChallenge(
		@PathVariable String challengeId,
		HttpServletRequest request) {

		// challengeId 유효성 검사
		if (challengeId == null || challengeId.trim().isEmpty() || challengeId.equalsIgnoreCase("null")) {
			throw new BusinessException(ErrorCode.CHALLENGE_ID_REQUIRED);
		}

		try {
			Long parsedChallengeId = Long.parseLong(challengeId);

			// JWT 토큰에서 사용자 ID 추출
			Long userId = getCurrentUserId(request);

			challengeService.leaveChallenge(parsedChallengeId, userId);
			return ResponseEntity.ok("챌린지 탈퇴가 완료되었습니다.");

		} catch (NumberFormatException e) {
			throw new BusinessException(ErrorCode.CHALLENGE_ID_REQUIRED);
		}
	}

	/**
	 * JWT 토큰에서 현재 로그인한 사용자 ID 추출
	 */
	private Long getCurrentUserId(HttpServletRequest request) {
		String token = extractTokenFromRequest(request);
		if (token == null || token.isEmpty()) {
			throw new BusinessException(ErrorCode.USER_UNAUTHORIZED);
		}

		try {
			// JWT 토큰이 만료되었는지 확인
			if (jwtUtil.isExpired(token)) {
				throw new BusinessException(ErrorCode.USER_UNAUTHORIZED);
			}

			// JWT에서 사용자 ID 추출
			return jwtUtil.getUserId(token);
		} catch (Exception e) {
			throw new BusinessException(ErrorCode.USER_UNAUTHORIZED);
		}
	}

	/**
	 * Authorization 헤더에서 Bearer 토큰 추출
	 */
	private String extractTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}