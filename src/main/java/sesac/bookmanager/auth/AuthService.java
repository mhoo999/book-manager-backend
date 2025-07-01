package sesac.bookmanager.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac.bookmanager.Security.CustomUserDetails;
import sesac.bookmanager.Security.jwt.RefreshTokenDto;
import sesac.bookmanager.Security.jwt.RefreshTokenService;
import sesac.bookmanager.Security.jwt.jwtTokenProvider;
import sesac.bookmanager.auth.data.LoginRequestDto;
import sesac.bookmanager.auth.data.LoginResponseDto;
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
        System.out.println(request.getPassword());
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
}
