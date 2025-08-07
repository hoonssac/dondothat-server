package org.bbagisix.expense.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.bbagisix.expense.domain.ExpenseVO;

@Mapper
public interface ExpenseMapper {
	
	void insert(ExpenseVO expense);

	ExpenseVO findById(Long expenditureId);

	List<ExpenseVO> findAllByUserId(Long userId);

	int update(ExpenseVO expense);

	int delete(Long expenditureId, Long userId);

	List<ExpenseVO> getRecentExpenses(Long userId);

	List<Long> getTodayExpenseCategories(Long userId);
}
