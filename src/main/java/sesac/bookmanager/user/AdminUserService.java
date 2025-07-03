package sesac.bookmanager.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sesac.bookmanager.user.data.User;
import sesac.bookmanager.user.data.UserInfoDto;
import sesac.bookmanager.user.data.UserStatusDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final UserRepository userRepository;


    public Page<UserInfoDto> getUsersList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return userRepository.findAll(pageable)
                .map(user -> UserInfoDto.builder()
                        .userId(user.getId())
                        .userName(user.getName())
                        .userEmail(user.getEmail())
                        .phoneNumber(user.getPhone() != null ? user.getPhone() : "")
                        .status(!user.getIsDeleted()? "정상 회원":"탈퇴회원")
                        .build());
    }


    public UserInfoDto getUserInfo(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("해당 ID의 유저를 찾을 수 없음"));
        return UserInfoDto.builder()
                .userName(user.getName())
                .userEmail(user.getEmail())
                .phoneNumber(user.getPhone() != null ? user.getPhone() : "")
                .status(!user.getIsDeleted()? "정상 회원":"탈퇴회원")
                .build();
    }

    public UserInfoDto withDrawUserByAdmin(int userId) {
        userRepository.softDeleteById(userId, LocalDateTime.now());
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("해당 ID의 유저를 찾을 수 없음"));
        return UserInfoDto.builder()
                .userName(user.getName())
                .userEmail(user.getEmail())
                .phoneNumber(user.getPhone() != null ? user.getPhone() : "")
                .status(!user.getIsDeleted()? "정상 회원":"탈퇴회원")
                .build();
    }

    public UserStatusDto getUserStatus() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfToday = today.atStartOfDay();
        LocalDateTime endOfToday = today.plusDays(1).atStartOfDay();

        int todayNewUserCount = userRepository.countByCreatedAtBetween(startOfToday, endOfToday);
        int totalUserCount =  userRepository.countByIsDeletedFalse();
        int totalWithdrawUserCount =  userRepository.countByIsDeletedTrue();

        return UserStatusDto.builder()
                .todayNewUserCount(todayNewUserCount)
                .totalUserCount(totalUserCount)
                .totalWithdrawUserCount(totalWithdrawUserCount)
                .build();
    }
}
