package org.bbagisix.asset.controller;

import java.util.Map;

import org.bbagisix.asset.dto.AssetDTO;
import org.bbagisix.asset.service.AssetService;
import org.bbagisix.exception.BusinessException;
import org.bbagisix.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@Log4j2
public class AssetController {

	private final AssetService assetService;

	@PostMapping("/connect")
	public ResponseEntity<Map<String, Object>> connectMainAsset(
		@RequestBody AssetDTO assetDTO,
		@RequestParam Long userId
	) {

		assetService.connectMainAsset(userId, assetDTO);

		return ResponseEntity.ok(Map.of(
			"success", true,
			"message", "메인 계좌 연결이 완료되었습니다."
		));
	}

	@PostMapping("/connect/sub")
	public ResponseEntity<Map<String, Object>> connectSubAsset(
		@RequestBody AssetDTO assetDTO,
		@RequestParam Long userId
	) {
		assetService.connectSubAsset(userId, assetDTO);

		return ResponseEntity.ok(Map.of(
			"success", true,
			"message", "서브 계좌 연결이 완료되었습니다."
		));
	}

	@DeleteMapping
	public ResponseEntity<Map<String, Object>> deleteMainAsset(
		@RequestParam Long userId,
		@RequestParam String status // main or sub
	) {
		// 입력값 검증
		if (userId == null) {
			throw new BusinessException(ErrorCode.USER_ID_REQUIRED);
		}

		assetService.deleteMainAsset(userId, status);

		String message = "main".equals(status) ? "메인 계좌가" : "서브 계좌가";
		return ResponseEntity.ok(Map.of(
			"success", true,
			"message", message + " 성공적으로 삭제되었습니다."
		));
	}
}