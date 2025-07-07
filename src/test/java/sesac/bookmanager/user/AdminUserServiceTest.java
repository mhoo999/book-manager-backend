package sesac.bookmanager.user;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import sesac.bookmanager.user.data.User;
import sesac.bookmanager.user.data.UserInfoDto;
import sesac.bookmanager.user.data.UserStatusDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class AdminUserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final AdminUserService adminUserService = new AdminUserService(userRepository);

    @Test
    @DisplayName("회원 목록 조회: status=active")
    void getUsersList_active() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<User> fakePage = new PageImpl<>(List.of(User.builder().id(1).name("홍길동").email("test@naver.com").isDeleted(false).build()));

        given(userRepository.findByIsDeletedFalseWithSearch(anyString(), anyString(), eq(pageable)))
                .willReturn(fakePage);

        // when
        Page<UserInfoDto> result = adminUserService.getUsersList(0, 10, "name", "홍길동", "active");

        // then
        assertThat(result.getContent().get(0).getUserName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("회원 목록 조회: status=quit")
    void getUsersList_quit() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<User> fakePage = new PageImpl<>(List.of(User.builder().id(2).name("철수").email("quit@naver.com").isDeleted(true).build()));

        given(userRepository.findByIsDeletedTrueWithSearch(anyString(), anyString(), eq(pageable)))
                .willReturn(fakePage);

        Page<UserInfoDto> result = adminUserService.getUsersList(0, 10, "email", "quit@naver.com", "quit");

        assertThat(result.getContent().get(0).getUserName()).isEqualTo("철수");
        assertThat(result.getContent().get(0).getStatus()).isEqualTo("탈퇴회원");
    }

    @Test
    @DisplayName("회원 목록 조회: status=null")
    void getUsersList_all() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<User> fakePage = new PageImpl<>(List.of(User.builder().id(3).name("영희").email("all@naver.com").isDeleted(false).build()));

        given(userRepository.findAllWithSearch(anyString(), anyString(), eq(pageable)))
                .willReturn(fakePage);

        Page<UserInfoDto> result = adminUserService.getUsersList(0, 10, "name", "영희", null);

        assertThat(result.getContent().get(0).getUserName()).isEqualTo("영희");
    }

    @Test
    @DisplayName("정상 회원 목록 조회")
    void getActiveUsersList() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<User> fakePage = new PageImpl<>(List.of(User.builder().id(1).name("홍길동").email("test@naver.com").isDeleted(false).build()));

        given(userRepository.findByIsDeletedFalse(pageable)).willReturn(fakePage);

        Page<UserInfoDto> result = adminUserService.getActiveUsersList(0, 10);

        assertThat(result.getContent().get(0).getUserName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("탈퇴 회원 목록 조회")
    void getQuitUsersList() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<User> fakePage = new PageImpl<>(List.of(User.builder().id(2).name("철수").email("quit@naver.com").isDeleted(true).build()));

        given(userRepository.findByIsDeletedTrue(pageable)).willReturn(fakePage);

        Page<UserInfoDto> result = adminUserService.getQuitUsersList(0, 10);

        assertThat(result.getContent().get(0).getStatus()).isEqualTo("탈퇴회원");
    }

    @Test
    @DisplayName("회원 상세 조회 성공")
    void getUserInfo_success() {
        User user = User.builder().id(1).name("홍길동").email("test@naver.com").isDeleted(false).build();
        given(userRepository.findById(1)).willReturn(Optional.of(user));

        UserInfoDto dto = adminUserService.getUserInfo(1);

        assertThat(dto.getUserName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("회원 상세 조회 실패")
    void getUserInfo_fail() {
        given(userRepository.findById(1)).willReturn(Optional.empty());

        assertThatThrownBy(() -> adminUserService.getUserInfo(1))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("회원 탈퇴 처리")
    void withDrawUserByAdmin() {
        User user = User.builder().id(1).build();
        given(userRepository.findById(1)).willReturn(Optional.of(user));

        adminUserService.withDrawUserByAdmin(1);

        verify(userRepository).softDeleteById(eq(1), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("회원 탈퇴 처리 실패")
    void withDrawUserByAdmin_fail() {
        given(userRepository.findById(1)).willReturn(Optional.empty());

        assertThatThrownBy(() -> adminUserService.withDrawUserByAdmin(1))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("회원 복구 처리")
    void restoreUserByAdmin() {
        adminUserService.restoreUserByAdmin(1);
        verify(userRepository).restoreById(1L);
    }

    @Test
    @DisplayName("회원 통계 조회")
    void getUserStatus() {
        given(userRepository.countByCreatedAtBetween(any(), any())).willReturn(3);
        given(userRepository.countByIsDeletedFalse()).willReturn(10);
        given(userRepository.countByIsDeletedTrue()).willReturn(5);

        UserStatusDto status = adminUserService.getUserStatus();

        assertThat(status.getTodayNewUserCount()).isEqualTo(3);
        assertThat(status.getTotalUserCount()).isEqualTo(10);
        assertThat(status.getTotalWithdrawUserCount()).isEqualTo(5);
    }
}
