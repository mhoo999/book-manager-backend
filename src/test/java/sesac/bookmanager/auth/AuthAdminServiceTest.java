package sesac.bookmanager.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sesac.bookmanager.admin.AdminRepository;
import sesac.bookmanager.admin.data.Admin;
import sesac.bookmanager.auth.data.AdminLoginFormDto;
import sesac.bookmanager.auth.data.AdminRegisterRequest;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class AuthAdminServiceTest {

    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private final AdminRepository adminRepository = mock(AdminRepository.class);

    private final AuthAdminService authAdminService = new AuthAdminService(
            passwordEncoder,
            authenticationManager,
            adminRepository
    );

    @Test
    @DisplayName("로그인 성공 시 SecurityContextHolder에 인증 저장")
    void loginSuccess() {
        // given
        AdminLoginFormDto loginForm = new AdminLoginFormDto("admin1", "password");

        Authentication fakeAuth = mock(Authentication.class);
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(fakeAuth);

        // when
        authAdminService.login(loginForm);

        // then
        Authentication contextAuth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(contextAuth).isEqualTo(fakeAuth);

        // SecurityContext cleanup (테스트 격리)
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("로그인 실패 시 예외 발생")
    void loginFail() {
        // given
        AdminLoginFormDto loginForm = new AdminLoginFormDto("admin1", "wrongpw");
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willThrow(new BadCredentialsException("잘못된 자격 증명"));

        // when & then
        assertThatThrownBy(() -> authAdminService.login(loginForm))
                .isInstanceOf(BadCredentialsException.class);

        // SecurityContext cleanup (혹시 남아있을 수 있으니)
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("로그아웃 시 SecurityContextHolder가 비워짐")
    void logout() {
        // given
        Authentication fakeAuth = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(fakeAuth);

        // when
        authAdminService.logout();

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    @DisplayName("ID 중복 체크 true")
    void checkIdTrue() {
        // given
        given(adminRepository.existsByAccountId("admin1")).willReturn(true);

        // when
        boolean exists = authAdminService.checkId("admin1");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("ID 중복 체크 false")
    void checkIdFalse() {
        // given
        given(adminRepository.existsByAccountId("admin1")).willReturn(false);

        // when
        boolean exists = authAdminService.checkId("admin1");

        // then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("회원가입 성공 - 암호화 및 저장")
    void registerSuccess() {
        // given
        AdminRegisterRequest request = new AdminRegisterRequest("admin1", "password", "password", "홍길동", "개발팀", "팀장","010-1234-5678");
        given(adminRepository.existsByAccountId("admin1")).willReturn(false);
        given(passwordEncoder.encode("password")).willReturn("encryptedPW");

        // when
        authAdminService.register(request);

        // then
        ArgumentCaptor<Admin> captor = ArgumentCaptor.forClass(Admin.class);
        verify(adminRepository).save(captor.capture());

        Admin saved = captor.getValue();
        assertThat(saved.getAccountId()).isEqualTo("admin1");
        assertThat(saved.getPwd()).isEqualTo("encryptedPW");
        assertThat(saved.getName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("회원가입 실패 - 중복 아이디")
    void registerFailDuplicate() {
        // given
        AdminRegisterRequest request = new AdminRegisterRequest("admin1", "password", "password", "홍길동", "개발팀", "팀장","010-1234-5678");
        given(adminRepository.existsByAccountId("admin1")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> authAdminService.register(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 사용중인 아이디");
    }
}
