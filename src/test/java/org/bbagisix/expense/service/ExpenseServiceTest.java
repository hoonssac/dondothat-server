package org.bbagisix.expense.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

import org.bbagisix.config.RootConfig;
import org.bbagisix.expense.dto.ExpenseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RootConfig.class})
@Transactional
@Log4j2
class ExpenseServiceTest {
	@Autowired
	private ExpenseService service;

	private ExpenseDTO createTestExpenseDTO() {
		ExpenseDTO expenseDTO = new ExpenseDTO();
		expenseDTO.setUserId(1L); // user_id를 1로 고정
		expenseDTO.setCategoryId(1L);
		expenseDTO.setAssetId(1L);
		expenseDTO.setAmount(50000L);
		expenseDTO.setDescription("test");
		expenseDTO.setExpenditureDate(new Date());
		return expenseDTO;
	}

	@Test
	@DisplayName("소비 내역 생성 테스트")
	void createExpense() {
		// given
		ExpenseDTO createExpense = service.createExpense(createTestExpenseDTO());

		// then
		assertNotNull(createExpense.getExpenditureId());
		assertEquals("test", createExpense.getDescription());
		log.info("생성 소비 내역: {}", createExpense);
	}

	@Test
	@DisplayName("소비 내역 건당 조회 테스트")
	void getExpenseById() {
		// given
		ExpenseDTO createExpense = service.createExpense(createTestExpenseDTO());
		Long id = createExpense.getExpenditureId();

		// when
		ExpenseDTO foundExpense = service.getExpenseById(id);

		// then
		assertNotNull(foundExpense);
		assertEquals(id, foundExpense.getExpenditureId());
		log.info("조회된 소비 내역: {}", foundExpense);
	}

	@Test
	@DisplayName("사용자 모든 소비 내역 조회 테스트")
	void getExpensesByUserId() {
		// given
		service.createExpense(createTestExpenseDTO());
		service.createExpense(createTestExpenseDTO());

		// when
		List<ExpenseDTO> expenses = service.getExpensesByUserId(1L);

		// then
		assertEquals(2, expenses.size());
		log.info("사용자 1L의 소비 내역 {}건 조회", expenses.size());
	}

	@Test
	@DisplayName("소비 내역 수정 테스트")
	void updateExpense() {
		// given
		ExpenseDTO createExpense = service.createExpense(createTestExpenseDTO());
		Long id = createExpense.getExpenditureId();

		// when
		ExpenseDTO updateExpense = new ExpenseDTO();
		updateExpense.setUserId(1L);
		updateExpense.setCategoryId(2L);
		updateExpense.setAssetId(1L);
		updateExpense.setAmount(99000L);
		updateExpense.setDescription("수정 내역");
		updateExpense.setExpenditureDate(new Date());

		ExpenseDTO updatedExpense = service.updateExpense(id, updateExpense);

		// then
		assertNotNull(updatedExpense);
		assertEquals("수정 내역", updatedExpense.getDescription());
		assertEquals(99000L, updatedExpense.getAmount());
		log.info("수정된 소비 내역: {}", updatedExpense);
	}

	@Test
	@DisplayName("소비 내역 삭제 테스트")
	void deleteExpense() {
		// given
		ExpenseDTO createExpense = service.createExpense(createTestExpenseDTO());
		Long id = createExpense.getExpenditureId();

		// when
		service.deleteExpense(id);

		// then
		assertNull(service.getExpenseById(id), "삭제된 내역은 조회 시 null이어야 함.");
		log.info("{}번 소비 내역 삭제 완료", id);
	}
}