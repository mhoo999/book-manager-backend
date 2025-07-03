package sesac.bookmanager.user.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserInfoDto {
    private int userId;
    private String userName;
    private String phoneNumber;
    private String userEmail;
    private String status; // 탈퇴 여부에 따른 정상/탈퇴 회원 구분
}