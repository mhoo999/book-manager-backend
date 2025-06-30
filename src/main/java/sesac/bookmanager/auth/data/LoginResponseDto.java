package sesac.bookmanager.auth.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponseDto {
    private String name;
    private String accessToken;
    private String refreshToken;


    public JwtResponseDto(String name, String accessToken, String refreshToken) {
        this.name = name;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}