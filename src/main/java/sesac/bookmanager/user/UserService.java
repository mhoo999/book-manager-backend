package sesac.bookmanager.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac.bookmanager.user.data.ChangePasswordRequestDto;
import sesac.bookmanager.auth.data.UserResponseDto;
import sesac.bookmanager.user.data.User;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponseDto getUserInfo(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return UserResponseDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .phoneNo(user.getPhone())
                .build();
    }


    public void changePassword(int userId, ChangePasswordRequestDto requestBody) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(requestBody.getCurrentPassword(), user.getPwd())) {
            throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다.");
        }
        user.setPwd(passwordEncoder.encode(requestBody.getNewPassword()));
    }

    public void softDeleteUser(int userId) {
        userRepository.softDeleteById(userId, LocalDateTime.now());
    }
}
