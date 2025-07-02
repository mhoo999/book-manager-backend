package sesac.bookmanager.auth.data;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {

    @NotBlank
    private String userEmail;
    @NotBlank
    private String password;
    @NotBlank
    private String userName;
    @NotBlank
    private String phoneNo;
}
