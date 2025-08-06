package org.bbagisix.classify.service;

import java.util.List;

import org.bbagisix.expense.domain.ExpenseVO;

public interface ClassifyService {
	ExpenseVO classify(ExpenseVO expense);

	List<ExpenseVO> classifyBatch(List<ExpenseVO> expense);
}
