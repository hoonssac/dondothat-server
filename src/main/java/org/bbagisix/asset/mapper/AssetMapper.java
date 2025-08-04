package org.bbagisix.asset.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.bbagisix.asset.domain.AssetVO;
import org.bbagisix.expense.domain.ExpenseVO;

import java.util.List;

@Mapper
public interface AssetMapper {
	void insertUserAsset(AssetVO assetVO);

	AssetVO selectAssetByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

	int deleteUserAssetByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

	int deleteExpensesByUserId(Long userId);
	int insertExpenses(List<ExpenseVO> expenseVO);
}
