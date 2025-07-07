package sesac.bookmanager.auth.data;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginFormDto {

    @NotBlank(message = "아이디 입력해주세요")
    private String accountId;
    @NotBlank(message = "비밀번호 입력해주세요")
    private String password;
}
