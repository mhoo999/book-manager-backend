package sesac.bookmanager.Security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RefreshTokenDto {

    private String subject;  // user.getId()가 문자열로 변환된 값 (주체)
    private String email;
    private String name;
    private Date issuedAt;
    private Date expiration;




}