package com.prism.platform.service.support;

import com.prism.common.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 空间角色等级约束校验器测试（PRD FR-22，5×5 等级矩阵穷举）
 */
@DisplayName("空间角色等级约束校验器")
class WorkspaceRoleCheckerTest {

    private final WorkspaceRoleChecker checker = new WorkspaceRoleChecker();

    @ParameterizedTest(name = "操作人等级{0} 对目标等级{1} → {2}")
    @CsvSource({
            // operatorLevel, targetLevel, allowed
            "5, 4, true", "5, 3, true", "5, 2, true", "5, 1, true", "5, 5, false",
            "4, 3, true", "4, 2, true", "4, 1, true", "4, 4, false", "4, 5, false",
            "3, 2, true", "3, 1, true", "3, 3, false", "3, 4, false", "3, 5, false",
            "2, 1, true", "2, 2, false", "2, 3, false", "2, 4, false", "2, 5, false",
            "1, 1, false", "1, 2, false", "1, 3, false", "1, 4, false", "1, 5, false",
    })
    @DisplayName("仅能操作等级严格低于自己的成员（020207）")
    void assertCanOperate_levelMatrix_enforcesStrictlyLower(int operatorLevel, int targetLevel,
            boolean allowed) {
        if (allowed) {
            assertThatCode(() -> checker.assertCanOperate(operatorLevel, targetLevel))
                    .doesNotThrowAnyException();
        }
        else {
            assertThatThrownBy(() -> checker.assertCanOperate(operatorLevel, targetLevel))
                    .isInstanceOf(BusinessException.class)
                    .extracting(e -> ((BusinessException) e).getCode())
                    .isEqualTo("020207");
        }
    }

    @Test
    @DisplayName("非 Owner 授予 Owner 角色被拒绝（020208）")
    void assertCanGrant_nonOwnerGrantsOwner_throws020208() {
        assertThatThrownBy(() -> checker.assertCanGrant("MAINTAINER", "OWNER"))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getCode())
                .isEqualTo("020208");
    }

    @Test
    @DisplayName("Owner 授予 Owner 角色放行")
    void assertCanGrant_ownerGrantsOwner_passes() {
        assertThatCode(() -> checker.assertCanGrant("OWNER", "OWNER")).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @CsvSource({ "MAINTAINER, MAINTAINER", "MAINTAINER, DEVELOPER", "DEVELOPER, REPORTER",
            "OWNER, MAINTAINER", "MAINTAINER, CUSTOM_ROLE" })
    @DisplayName("授予非 Owner 角色不受限制")
    void assertCanGrant_nonOwnerRole_alwaysPasses(String operatorRole, String newRole) {
        assertThatCode(() -> checker.assertCanGrant(operatorRole, newRole)).doesNotThrowAnyException();
    }
}
