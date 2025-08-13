package org.bbagisix.tier.controller;

import java.util.List;

import org.bbagisix.tier.dto.TierDTO;
import org.bbagisix.tier.service.TierService;
import org.bbagisix.user.dto.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TierController {

	private final TierService tierService;

	/**
	 * 개인 티어 조회
	 * GET /api/user/me/tiers
	 */
	@GetMapping("/user/me/tiers")
	public ResponseEntity<TierDTO> getUserCurrentTier(Authentication authentication) {
		CustomOAuth2User currentUser = (CustomOAuth2User)authentication.getPrincipal();
		TierDTO currentTier = tierService.getUserCurrentTier(currentUser.getUserId());
		return ResponseEntity.ok(currentTier);
	}

	/**
	 * 전체 티어 조회
	 * GET /api/tiers/all
	 */
	@GetMapping("/tiers/all")
	public ResponseEntity<List<TierDTO>> getAllTiers() {
		List<TierDTO> allTiers = tierService.getAllTiers();
		return ResponseEntity.ok(allTiers);
	}
	
	/**
	 * 사용자 tier 재계산 (테스트/디버깅용)
	 * POST /api/user/me/tiers/recalculate
	 */
	@PostMapping("/user/me/tiers/recalculate")
	public ResponseEntity<String> recalculateUserTier(Authentication authentication) {
		CustomOAuth2User currentUser = (CustomOAuth2User)authentication.getPrincipal();
		tierService.recalculateUserTier(currentUser.getUserId());
		return ResponseEntity.ok("Tier 재계산이 완료되었습니다.");
	}
}
