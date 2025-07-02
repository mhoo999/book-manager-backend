package sesac.bookmanager.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac.bookmanager.security.CustomUserDetails;
import sesac.bookmanager.security.RefreshTokenService;
import sesac.bookmanager.security.jwtTokenProvider;
import sesac.bookmanager.auth.data.FindPasswordRequestDto;
import sesac.bookmanager.auth.data.LoginRequestDto;
import sesac.bookmanager.auth.data.LoginResponseDto;
import sesac.bookmanager.auth.data.RegisterRequestDto;
import sesac.bookmanager.user.data.User;
import sesac.bookmanager.user.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final jwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;


    @Transactional
    public LoginResponseDto login(LoginRequestDto request) {
        Authentication authentication =  authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String accessToken = jwtTokenProvider.createAccessToken(customUserDetails);
        String refreshToken = refreshTokenService.CreateRefreshToken(customUserDetails.getUser().getId());

        return new LoginResponseDto(customUserDetails.getUsername(), accessToken,refreshToken);
    }

    @Transactional
    public void logout(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        refreshTokenService.delete(customUserDetails.getUser().getId());
    }

    @Transactional(readOnly = true)
    public String findPassword(FindPasswordRequestDto request) {
        User user = userRepository.findByEmailAndName(request.getUserEmail(), request.getUserName())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: "));
        return user.getPwd();
    }

    @Transactional(readOnly = true)
    public Boolean isIdDuplicate(String userEmail) {
        return userRepository.findByEmail(userEmail).isPresent();
    }

    @Transactional
    public void register(RegisterRequestDto request) {
        if (userRepository.findByEmail(request.getUserEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .email(request.getUserEmail())
                .pwd(encodedPassword)
                .name(request.getUserName())
                .phone(request.getPhoneNo())
                .isDeleted(false)
                .build();
        userRepository.save(user);
    }
}
