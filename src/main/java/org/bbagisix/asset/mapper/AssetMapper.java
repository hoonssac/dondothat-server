package org.bbagisix.asset.mapper;

import java.util.List;

import org.bbagisix.asset.domain.AssetVO;

public interface AssetMapper {
	void insert(AssetVO asset);

	void update(AssetVO asset);

	AssetVO findByUserId(Long userId);

	AssetVO findById(Long assetId);

	int deleteByUserId(Long userId);
}
