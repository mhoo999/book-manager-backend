package sesac.bookmanager.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sesac.bookmanager.user.data.User;
import sesac.bookmanager.user.UserRepository;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final jwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;



    //for DB-works
    public String createRefreshToken(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String refreshToken = jwtTokenProvider.createRefreshToken(new CustomUserDetails(user));
        refreshTokenRepository.save(userId, refreshToken);
        return refreshToken;

    }
    public void delete(int userId) {

        refreshTokenRepository.deleteByUserId(userId);
    }

    public boolean isValid(int userId, String refreshToken) {
        String inRedisToken = refreshTokenRepository.findByUserId(userId);
        return refreshToken.equals(inRedisToken);
    }



}