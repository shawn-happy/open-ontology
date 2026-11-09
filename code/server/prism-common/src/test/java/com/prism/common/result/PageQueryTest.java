package com.prism.common.result;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 分页请求 PageQuery 测试
 */
@DisplayName("分页请求 PageQuery")
class PageQueryTest {

	private static ValidatorFactory validatorFactory;

	private static Validator validator;

	@BeforeAll
	static void initValidator() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@AfterAll
	static void closeValidator() {
		if (validatorFactory != null) {
			validatorFactory.close();
		}
	}

	@Test
	@DisplayName("默认值 - 第 1 页每页 20 条且关键词为 null")
	void constructor_defaultValues_pageOneSizeTwenty() {
		// Act
		PageQuery query = new PageQuery();

		// Assert
		assertThat(query.getCurrentPage()).isEqualTo(1);
		assertThat(query.getPageSize()).isEqualTo(20);
		assertThat(query.getKeyword()).isNull();
	}

	@Test
	@DisplayName("Setter 赋值 - 字段被正确更新")
	void setters_validValues_fieldsUpdated() {
		// Arrange
		PageQuery query = new PageQuery();

		// Act
		query.setCurrentPage(3);
		query.setPageSize(50);
		query.setKeyword("本体");

		// Assert
		assertThat(query.getCurrentPage()).isEqualTo(3);
		assertThat(query.getPageSize()).isEqualTo(50);
		assertThat(query.getKeyword()).isEqualTo("本体");
	}

	@Nested
	@DisplayName("Bean Validation 约束")
	class ValidationRules {

		@ParameterizedTest(name = "currentPage={0}, pageSize={1} → 校验通过")
		@CsvSource({ "1, 1", "1, 20", "10, 100", "999, 50" })
		@DisplayName("合法分页参数 - 无约束违反")
		void validate_legalPageParams_noViolations(int currentPage, int pageSize) {
			// Arrange
			PageQuery query = new PageQuery();
			query.setCurrentPage(currentPage);
			query.setPageSize(pageSize);

			// Act
			Set<ConstraintViolation<PageQuery>> violations = validator.validate(query);

			// Assert
			assertThat(violations).isEmpty();
		}

		@Test
		@DisplayName("页码为 0 - 违反 @Min(1)")
		void validate_zeroCurrentPage_minViolation() {
			// Arrange
			PageQuery query = new PageQuery();
			query.setCurrentPage(0);

			// Act
			Set<ConstraintViolation<PageQuery>> violations = validator.validate(query);

			// Assert
			assertThat(violations).hasSize(1);
			assertThat(violations.iterator().next().getMessage()).isEqualTo("页码最小为 1");
		}

		@Test
		@DisplayName("每页 0 条 - 违反 @Min(1)")
		void validate_zeroPageSize_minViolation() {
			// Arrange
			PageQuery query = new PageQuery();
			query.setPageSize(0);

			// Act
			Set<ConstraintViolation<PageQuery>> violations = validator.validate(query);

			// Assert
			assertThat(violations).hasSize(1);
			assertThat(violations.iterator().next().getMessage()).isEqualTo("每页条数最小为 1");
		}

		@Test
		@DisplayName("每页 101 条 - 违反 @Max(100)")
		void validate_pageSizeExceedsMax_maxViolation() {
			// Arrange
			PageQuery query = new PageQuery();
			query.setPageSize(101);

			// Act
			Set<ConstraintViolation<PageQuery>> violations = validator.validate(query);

			// Assert
			assertThat(violations).hasSize(1);
			assertThat(violations.iterator().next().getMessage()).isEqualTo("每页条数最大为 100");
		}

	}

}
