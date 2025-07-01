package sesac.bookmanager.auth.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponseDto {
    private String email;
    private String name;
    private String phoneNo;
}
