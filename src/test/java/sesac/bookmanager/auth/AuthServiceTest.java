package sesac.bookmanager.auth;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import sesac.bookmanager.global.exceptions.InvalidRefreshTokenException;
import sesac.bookmanager.global.exceptions.UserWasDeletedException;
import sesac.bookmanager.security.CustomUserDetails;
import sesac.bookmanager.security.RefreshTokenService;
import sesac.bookmanager.security.jwtTokenProvider;
import sesac.bookmanager.auth.data.FindPasswordRequestDto;
import sesac.bookmanager.auth.data.LoginRequestDto;
import sesac.bookmanager.auth.data.LoginResponseDto;
import sesac.bookmanager.auth.data.RegisterRequestDto;
import sesac.bookmanager.user.UserRepository;
import sesac.bookmanager.user.data.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class AuthServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private final jwtTokenProvider jwtTokenProvider = mock(jwtTokenProvider.class);
    private final RefreshTokenService refreshTokenService = mock(RefreshTokenService.class);

    private final AuthService authService = new AuthService(
            userRepository,
            passwordEncoder,
            authenticationManager,
            jwtTokenProvider,
            refreshTokenService
    );

    @Test
    @DisplayName("로그인 성공: 인증, 토큰 생성, 응답 DTO")
    void loginSuccess() {
        // given
        LoginRequestDto request = new LoginRequestDto("test@naver.com", "password");

        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
        User fakeUser = User.builder().id(1).email("test@naver.com").build();
        given(customUserDetails.getUser()).willReturn(fakeUser);
        given(customUserDetails.getUsername()).willReturn("test@naver.com");

        Authentication fakeAuth = mock(Authentication.class);
        given(fakeAuth.getPrincipal()).willReturn(customUserDetails);
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(fakeAuth);

        given(jwtTokenProvider.createAccessToken(fakeUser)).willReturn("accessToken");
        given(refreshTokenService.createRefreshToken(fakeUser.getId())).willReturn("refreshToken");

        // when
        LoginResponseDto result = authService.login(request);

        // then
        assertThat(result.getName()).isEqualTo("test@naver.com");
        assertThat(result.getAccessToken()).isEqualTo("accessToken");
        assertThat(result.getRefreshToken()).isEqualTo("refreshToken");
        // SecurityContextHolder에 인증이 저장됐는지 확인
        Authentication contextAuth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(contextAuth).isEqualTo(fakeAuth);

        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("로그인 실패: 인증 실패")
    void loginFail() {
        // given
        LoginRequestDto request = new LoginRequestDto("test@naver.com", "wrongpw");
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willThrow(new BadCredentialsException("인증 실패"));

        // when & then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    @DisplayName("로그아웃: RefreshTokenService에서 삭제 호출됨")
    void logout() {
        // given
        User user = User.builder().id(1).build();
        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
        given(customUserDetails.getUser()).willReturn(user);

        // when
        authService.logout(customUserDetails);

        // then
        verify(refreshTokenService).delete(user.getId());
    }

    @Test
    @DisplayName("비밀번호 찾기 성공")
    void findPassword() {
        // given
        FindPasswordRequestDto request = new FindPasswordRequestDto("test@naver.com", "홍길동");
        User user = User.builder().email("test@naver.com").name("홍길동").pwd("secretPW").build();
        given(userRepository.findByEmailAndName(anyString(), anyString()))
                .willReturn(Optional.of(user));

        // when
        String result = authService.findPassword(request);

        // then
        assertThat(result).isEqualTo("secretPW");
    }

    @Test
    @DisplayName("비밀번호 찾기 실패 - 없는 사용자")
    void findPasswordFail() {
        // given
        FindPasswordRequestDto request = new FindPasswordRequestDto("test@naver.com", "홍길동");
        given(userRepository.findByEmailAndName("test@naver.com", "홍길동"))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authService.findPassword(request))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    @DisplayName("ID 중복 체크 true")
    void isIdDuplicateTrue() {
        given(userRepository.findByEmail("test@naver.com"))
                .willReturn(Optional.of(new User()));

        assertThat(authService.isIdDuplicate("test@naver.com")).isTrue();
    }

    @Test
    @DisplayName("ID 중복 체크 false")
    void isIdDuplicateFalse() {
        given(userRepository.findByEmail("test@naver.com"))
                .willReturn(Optional.empty());

        assertThat(authService.isIdDuplicate("test@naver.com")).isFalse();
    }

    @Test
    @DisplayName("회원가입 성공: 암호화 + 저장")
    void registerSuccess() {
        // given
        RegisterRequestDto request = new RegisterRequestDto("test@naver.com", "password", "홍길동", "010-1111-1111");
        given(userRepository.findByEmail("test@naver.com")).willReturn(Optional.empty());
        given(passwordEncoder.encode("password")).willReturn("encryptedPW");

        // when
        authService.register(request);

        // then
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();
        assertThat(saved.getEmail()).isEqualTo("test@naver.com");
        assertThat(saved.getPwd()).isEqualTo("encryptedPW");
        assertThat(saved.getIsDeleted()).isFalse();
    }

    @Test
    @DisplayName("회원가입 실패: 중복 이메일")
    void registerFailDuplicate() {
        RegisterRequestDto request = new RegisterRequestDto("test@naver.com", "password", "홍길동", "010-1111-1111");
        given(userRepository.findByEmail("test@naver.com")).willReturn(Optional.of(new User()));

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 사용 중");
    }

    @Test
    @DisplayName("RefreshToken 재발급 성공")
    void refreshAccessTokenSuccess() {
        // given
        String refreshToken = "refreshToken";
        Claims claims = mock(Claims.class);
        given(jwtTokenProvider.getAllClaimsFromToken(refreshToken)).willReturn(claims);
        given(claims.getSubject()).willReturn("1");

        User user = User.builder().id(1).isDeleted(false).build();
        given(userRepository.findById(1)).willReturn(Optional.of(user));
        given(refreshTokenService.isValid(1, refreshToken)).willReturn(true);
        given(jwtTokenProvider.createAccessToken(user)).willReturn("newAccessToken");

        // when
        String newAccessToken = authService.refreshAccessToken(refreshToken);

        // then
        assertThat(newAccessToken).isEqualTo("newAccessToken");
    }

    @Test
    @DisplayName("RefreshToken 실패: 탈퇴 유저")
    void refreshAccessTokenFailDeletedUser() {
        // given
        String refreshToken = "refreshToken";
        Claims claims = mock(Claims.class);
        given(jwtTokenProvider.getAllClaimsFromToken(refreshToken)).willReturn(claims);
        given(claims.getSubject()).willReturn("1");

        User user = User.builder().id(1).isDeleted(true).build();
        given(userRepository.findById(1)).willReturn(Optional.of(user));

        // when & then
        assertThatThrownBy(() -> authService.refreshAccessToken(refreshToken))
                .isInstanceOf(UserWasDeletedException.class);
    }

    @Test
    @DisplayName("RefreshToken 실패: 유효하지 않은 토큰")
    void refreshAccessTokenFailInvalidToken() {
        // given
        String refreshToken = "refreshToken";
        Claims claims = mock(Claims.class);
        given(jwtTokenProvider.getAllClaimsFromToken(refreshToken)).willReturn(claims);
        given(claims.getSubject()).willReturn("1");

        User user = User.builder().id(1).isDeleted(false).build();
        given(userRepository.findById(1)).willReturn(Optional.of(user));
        given(refreshTokenService.isValid(1, refreshToken)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.refreshAccessToken(refreshToken))
                .isInstanceOf(InvalidRefreshTokenException.class);
    }
}
