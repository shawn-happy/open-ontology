package com.prism.common.context;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 请求上下文 RequestContextHolder 测试
 */
@DisplayName("请求上下文 RequestContextHolder")
class RequestContextHolderTest {

	@AfterEach
	void tearDown() {
		RequestContextHolder.clear();
	}

	@Nested
	@DisplayName("读写上下文属性")
	class SetAndGet {

		@Test
		@DisplayName("设置 requestId - 读取到相同值")
		void setRequestId_afterSet_returnsSameValue() {
			// Act
			RequestContextHolder.setRequestId("trace-123");

			// Assert
			assertThat(RequestContextHolder.getRequestId()).isEqualTo("trace-123");
		}

		@Test
		@DisplayName("设置 workspaceCode - 读取到相同值")
		void setWorkspaceCode_afterSet_returnsSameValue() {
			// Act
			RequestContextHolder.setWorkspaceCode("ws_fin");

			// Assert
			assertThat(RequestContextHolder.getWorkspaceCode()).isEqualTo("ws_fin");
		}

		@Test
		@DisplayName("设置 userId - 读取到相同值")
		void setUserId_afterSet_returnsSameValue() {
			// Act
			RequestContextHolder.setUserId(42L);

			// Assert
			assertThat(RequestContextHolder.getUserId()).isEqualTo(42L);
		}

		@Test
		@DisplayName("未设置任何值 - 三项均为 null")
		void get_withoutSet_returnsNull() {
			// Assert
			assertThat(RequestContextHolder.getRequestId()).isNull();
			assertThat(RequestContextHolder.getWorkspaceCode()).isNull();
			assertThat(RequestContextHolder.getUserId()).isNull();
		}

	}

	@Nested
	@DisplayName("清理与线程隔离")
	class ClearAndIsolation {

		@Test
		@DisplayName("调用 clear - 三项上下文全部清空")
		void clear_afterSet_allValuesRemoved() {
			// Arrange
			RequestContextHolder.setRequestId("r");
			RequestContextHolder.setWorkspaceCode("w");
			RequestContextHolder.setUserId(1L);

			// Act
			RequestContextHolder.clear();

			// Assert
			assertThat(RequestContextHolder.getRequestId()).isNull();
			assertThat(RequestContextHolder.getWorkspaceCode()).isNull();
			assertThat(RequestContextHolder.getUserId()).isNull();
		}

		@Test
		@DisplayName("调用 clearWorkspace - 仅清空 workspaceCode 其余保留")
		void clearWorkspace_afterSet_onlyWorkspaceCodeRemoved() {
			// Arrange
			RequestContextHolder.setRequestId("r");
			RequestContextHolder.setWorkspaceCode("w");
			RequestContextHolder.setUserId(1L);

			// Act
			RequestContextHolder.clearWorkspace();

			// Assert
			assertThat(RequestContextHolder.getWorkspaceCode()).isNull();
			assertThat(RequestContextHolder.getRequestId()).isEqualTo("r");
			assertThat(RequestContextHolder.getUserId()).isEqualTo(1L);
		}

		@Test
		@DisplayName("ThreadLocal 隔离 - 子线程读不到主线程上下文")
		void get_fromOtherThread_doesNotSeeMainThreadValues() throws Exception {
			// Arrange
			RequestContextHolder.setRequestId("main-thread");
			RequestContextHolder.setUserId(9L);
			AtomicReference<String> childRequestId = new AtomicReference<>("init");
			AtomicReference<Long> childUserId = new AtomicReference<>(-1L);

			// Act
			Thread child = new Thread(() -> {
				childRequestId.set(RequestContextHolder.getRequestId());
				childUserId.set(RequestContextHolder.getUserId());
			});
			child.start();
			child.join();

			// Assert
			assertThat(childRequestId.get()).isNull();
			assertThat(childUserId.get()).isNull();
			assertThat(RequestContextHolder.getRequestId()).isEqualTo("main-thread");
		}

	}

}
