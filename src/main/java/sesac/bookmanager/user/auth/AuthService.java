package sesac.bookmanager.user.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac.bookmanager.Security.AuthUserDetailService;
import sesac.bookmanager.user.auth.data.LoginRequestDto;
import sesac.bookmanager.user.auth.data.LoginResponseDto;
import sesac.bookmanager.user.user.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthUserDetailService authUserDetailService;

    @Transactional
    public LoginResponseDto login(LoginRequestDto request) {
        Authentication authentication =  authenticationManager.authenticate.

        return new LoginResponseDto(userName, accessToken,refreshToken);
    }

    public Object logout(HttpServletRequest request) {
    }
}
