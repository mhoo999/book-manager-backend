package sesac.bookmanager.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import sesac.bookmanager.user.data.ChangePasswordRequestDto;
import sesac.bookmanager.auth.data.UserResponseDto;
import sesac.bookmanager.user.data.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    private final UserService userService = new UserService(userRepository, passwordEncoder);

    @Test
    @DisplayName("유저 정보 조회 성공")
    void getUserInfoSuccess() {
        // given
        User user = User.builder()
                .id(1)
                .email("test@naver.com")
                .name("홍길동")
                .phone("010-1234-5678")
                .build();
        given(userRepository.findById(1)).willReturn(Optional.of(user));

        // when
        UserResponseDto dto = userService.getUserInfo(1);

        // then
        assertThat(dto.getEmail()).isEqualTo("test@naver.com");
        assertThat(dto.getName()).isEqualTo("홍길동");
        assertThat(dto.getPhoneNo()).isEqualTo("010-1234-5678");
    }

    @Test
    @DisplayName("유저 정보 조회 실패 - 없는 유저")
    void getUserInfoFail() {
        // given
        given(userRepository.findById(1)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getUserInfo(1))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    void changePasswordSuccess() {
        // given
        User user = User.builder()
                .id(1)
                .pwd("hashedPW")
                .build();
        given(userRepository.findById(1)).willReturn(Optional.of(user));

        ChangePasswordRequestDto request = new ChangePasswordRequestDto(
                "plainPW",
                "newPW"
        );

        given(passwordEncoder.matches("plainPW", "hashedPW")).willReturn(true);
        given(passwordEncoder.encode("newPW")).willReturn("newHashedPW");

        // when
        userService.changePassword(1, request);

        // then
        assertThat(user.getPwd()).isEqualTo("newHashedPW");
    }

    @Test
    @DisplayName("비밀번호 변경 실패 - 기존 비밀번호 불일치")
    void changePasswordFail() {
        // given
        User user = User.builder()
                .id(1)
                .pwd("hashedPW")
                .build();
        given(userRepository.findById(1)).willReturn(Optional.of(user));

        ChangePasswordRequestDto request = new ChangePasswordRequestDto(
                "wrongPW",
                "newPW"
        );

        given(passwordEncoder.matches("wrongPW", "hashedPW")).willReturn(false);

        // when & then
        assertThatThrownBy(() -> userService.changePassword(1, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("기존 비밀번호가 일치하지 않습니다");
    }

    @Test
    @DisplayName("소프트 딜리트 실행")
    void softDeleteUser() {
        // when
        userService.softDeleteUser(1);

        // then
        verify(userRepository).softDeleteById(eq(1), any(LocalDateTime.class));
    }
}
