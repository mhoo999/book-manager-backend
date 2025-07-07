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
public class AdminRegisterRequest {
    @NotBlank
    private String adminAccountId;
    @NotBlank
    private String password;
    @NotBlank
    private String confirmedPassword;
    @NotBlank
    private String adminName;
    @NotBlank
    private String dept;
    @NotBlank
    private String position;
    @NotBlank
    private String phoneNo;


}
