package sesac.bookmanager.user.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequestDto {
    private String currentPassword;  // 기존 비밀번호
    private String newPassword;      // 새 비밀번호
}