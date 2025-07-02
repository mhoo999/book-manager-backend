package sesac.bookmanager.auth.data;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
