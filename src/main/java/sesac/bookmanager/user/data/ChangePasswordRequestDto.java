package sesac.bookmanager.user.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequestDto {
    private String currentPassword;  // 기존 비밀번호
    private String newPassword;      // 새 비밀번호
    private String confirmPassword;
}