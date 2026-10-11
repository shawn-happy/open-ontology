package com.prism.common.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Mockito 使用示例
 * 演示 Mock 对象和依赖注入测试
 */
@ExtendWith(MockitoExtension.class)
class MockExampleTest {

    // 模拟依赖
    @Mock
    private UserRepository userRepository;

    // 自动注入 Mock 到被测对象
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("通过 ID 查询用户应返回用户对象")
    void shouldReturnUserWhenIdExists() {
        // Given
        Long userId = 1L;
        User expectedUser = new User(userId, "张三");
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        // When
        User result = userService.getUserById(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("张三");
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("用户不存在时应返回 null")
    void shouldReturnNullWhenUserNotFound() {
        // Given
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        User result = userService.getUserById(userId);

        // Then
        assertThat(result).isNull();
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("保存用户应调用 repository.save")
    void shouldCallRepositorySaveWhenCreatingUser() {
        // Given
        User newUser = new User(null, "李四");
        User savedUser = new User(1L, "李四");
        when(userRepository.save(newUser)).thenReturn(savedUser);

        // When
        User result = userService.createUser(newUser);

        // Then
        assertThat(result.getId()).isEqualTo(1L);
        verify(userRepository).save(newUser);
    }

    // ===== 内部测试类 =====

    interface UserRepository {
        Optional<User> findById(Long id);
        User save(User user);
    }

    static class User {
        private Long id;
        private String name;

        User(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
    }

    static class UserService {
        private final UserRepository userRepository;

        UserService(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        User getUserById(Long id) {
            return userRepository.findById(id).orElse(null);
        }

        User createUser(User user) {
            return userRepository.save(user);
        }
    }
}
