package org.bbagisix.asset.mapper;

import java.util.List;

import org.bbagisix.asset.domain.AssetVO;

public interface AssetMapper {
	void insert(AssetVO asset);
	List<AssetVO> findByUserId(Long userId);
	AssetVO findById(Long assetId);
	int delete(Long assetId);
}
