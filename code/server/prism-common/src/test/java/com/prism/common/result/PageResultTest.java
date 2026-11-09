package com.prism.common.result;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 分页响应 PageResult 测试
 */
@DisplayName("分页响应 PageResult")
class PageResultTest {

	@Nested
	@DisplayName("从 MyBatis-Plus Page 构建（直接取 records）")
	class FromPage {

		@Test
		@DisplayName("中间页 - hasNext/hasPrevious 均为 true")
		void from_middlePage_bothNeighborsTrue() {
			// Arrange
			Page<String> page = new Page<>(2, 10, 100);
			page.setRecords(List.of("a", "b"));

			// Act
			PageResult<String> result = PageResult.from(page);

			// Assert
			assertThat(result.getData()).containsExactly("a", "b");
			assertThat(result.getPageInfo().getCurrentPage()).isEqualTo(2);
			assertThat(result.getPageInfo().getPageSize()).isEqualTo(10);
			assertThat(result.getPageInfo().getTotal()).isEqualTo(100);
			assertThat(result.getPageInfo().getTotalPages()).isEqualTo(10);
			assertThat(result.getPageInfo().isHasNext()).isTrue();
			assertThat(result.getPageInfo().isHasPrevious()).isTrue();
		}

		@Test
		@DisplayName("首页 - hasPrevious 为 false")
		void from_firstPage_hasPreviousFalse() {
			// Arrange
			Page<String> page = new Page<>(1, 10, 30);
			page.setRecords(List.of("x"));

			// Act
			PageResult<String> result = PageResult.from(page);

			// Assert
			assertThat(result.getPageInfo().isHasPrevious()).isFalse();
			assertThat(result.getPageInfo().isHasNext()).isTrue();
		}

		@Test
		@DisplayName("末页 - hasNext 为 false")
		void from_lastPage_hasNextFalse() {
			// Arrange
			Page<String> page = new Page<>(3, 10, 30);
			page.setRecords(List.of());

			// Act
			PageResult<String> result = PageResult.from(page);

			// Assert
			assertThat(result.getPageInfo().isHasNext()).isFalse();
			assertThat(result.getPageInfo().isHasPrevious()).isTrue();
			assertThat(result.getData()).isEmpty();
		}

		@ParameterizedTest(name = "第{0}页/共{2}条/每页{1} → 总页数{3}")
		@CsvSource({ "1, 10, 0, 0", "1, 10, 10, 1", "2, 10, 25, 3", "5, 20, 100, 5" })
		@DisplayName("总页数计算 - 与 Page.getPages 一致")
		void from_variousPages_totalPagesComputed(int current, int size, int total, int expectedPages) {
			// Arrange
			Page<String> page = new Page<>(current, size, total);

			// Act
			PageResult<String> result = PageResult.from(page);

			// Assert
			assertThat(result.getPageInfo().getTotalPages()).isEqualTo(expectedPages);
		}

	}

	@Nested
	@DisplayName("从 MyBatis-Plus Page 构建（Entity→VO 转换）")
	class FromPageWithData {

		@Test
		@DisplayName("传入转换后的 VO 列表 - data 使用传入列表而非 records")
		void from_withConvertedData_usesGivenList() {
			// Arrange
			Page<Long> page = new Page<>(1, 10, 2);
			page.setRecords(List.of(1L, 2L));
			List<String> voList = List.of("VO-1", "VO-2");

			// Act
			PageResult<String> result = PageResult.from(page, voList);

			// Assert
			assertThat(result.getData()).containsExactly("VO-1", "VO-2");
			assertThat(result.getPageInfo().getTotal()).isEqualTo(2);
		}

	}

	@Test
	@DisplayName("Builder 构建 - 字段完整赋值")
	void builder_allFields_valuesPreserved() {
		// Arrange
		PageResult.PageInfo pageInfo = PageResult.PageInfo.builder()
			.currentPage(1)
			.pageSize(20)
			.total(5)
			.totalPages(1)
			.hasNext(false)
			.hasPrevious(false)
			.build();

		// Act
		PageResult<String> result = PageResult.<String>builder().data(List.of("a")).pageInfo(pageInfo).build();

		// Assert
		assertThat(result.getData()).containsExactly("a");
		assertThat(result.getPageInfo().getPageSize()).isEqualTo(20);
		assertThat(result.getPageInfo().isHasNext()).isFalse();
	}

}
