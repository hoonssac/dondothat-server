package org.bbagisix.classify.service;

import java.util.List;

import org.bbagisix.expense.domain.ExpenseVO;

public interface ClassifyService {
	List<ExpenseVO> classify(List<ExpenseVO> expense);
}
