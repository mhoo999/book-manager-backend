package sesac.bookmanager.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac.bookmanager.user.data.User;
import sesac.bookmanager.user.data.UserInfoDto;
import sesac.bookmanager.user.data.UserStatusDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final UserRepository userRepository;


    private static final Sort SORT_BY_CREATED_DESC = Sort.by("createdAt").descending();

    @Transactional(readOnly = true)
    public Page<UserInfoDto> getUsersList(int page, int size, String type, String keyword, String status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // Repository에 조건 맞게 호출
        Page<User> result;

        if ("active".equals(status)) {
            result = userRepository.findByIsDeletedFalseWithSearch(type, keyword, pageable);
        } else if ("quit".equals(status)) {
            result = userRepository.findByIsDeletedTrueWithSearch(type, keyword, pageable);
        } else {
            result = userRepository.findAllWithSearch(type, keyword, pageable);
        }

        return result.map(user -> UserInfoDto.builder()
                .userId(user.getId())
                .userName(user.getName())
                .userEmail(user.getEmail())
                .phoneNumber(user.getPhone() != null ? user.getPhone() : "")
                .status(!user.getIsDeleted() ? "정상 회원" : "탈퇴회원")
                .build());
    }

    @Transactional(readOnly = true)
    public Page<UserInfoDto> getQuitUsersList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, SORT_BY_CREATED_DESC);
        return userRepository.findByIsDeletedTrue(pageable)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public Page<UserInfoDto> getActiveUsersList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, SORT_BY_CREATED_DESC);
        return userRepository.findByIsDeletedFalse(pageable)
                .map(this::convertToDto);
    }

    /** 공통 변환 로직 */
    private UserInfoDto convertToDto(User user) {
        return UserInfoDto.builder()
                .userId(user.getId())
                .userName(user.getName())
                .userEmail(user.getEmail())
                .phoneNumber(user.getPhone() != null ? user.getPhone() : "")
                .status(!user.getIsDeleted() ? "정상 회원" : "탈퇴회원")
                .build();
    }


    public UserInfoDto getUserInfo(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("해당 ID의 유저를 찾을 수 없음"));
        return UserInfoDto.builder()
                .userId(user.getId())
                .userName(user.getName())
                .userEmail(user.getEmail())
                .phoneNumber(user.getPhone() != null ? user.getPhone() : "")
                .status(!user.getIsDeleted()? "정상 회원":"탈퇴 회원")
                .build();
    }

    @Transactional
    public void withDrawUserByAdmin(int userId) {
        userRepository.softDeleteById(userId, LocalDateTime.now());
        userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("해당 ID의 유저를 찾을 수 없음"));

    }
    @Transactional
    public void restoreUserByAdmin(int userId) {
        userRepository.restoreById((long) userId);
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
