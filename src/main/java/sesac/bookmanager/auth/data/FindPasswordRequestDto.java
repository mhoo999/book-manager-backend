package sesac.bookmanager.auth.data;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindPasswordRequestDto {

    @NotBlank
    private String userName;
    @NotBlank
    private String userEmail;
}
