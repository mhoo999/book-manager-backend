package sesac.bookmanager.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import sesac.bookmanager.user.data.User;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class jwtTokenProvider {


    @Value("${jwt.access-expiration}")
    private Long accessExpiration;
    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    private final Key key;

    public jwtTokenProvider(@Value("${jwt.secret}") String secretString) {
        byte[] keyBytes = secretString.getBytes();
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    /** 토큰 생성 **/
    private String generateToken(User user, long expirationTime) {
        Claims claims = Jwts.claims();
        claims.setSubject(String.valueOf(user.getId()));
        claims.put("email", user.getEmail());
        claims.put("name", user.getName());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime * 1000))
                .signWith(this.key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createAccessToken(CustomUserDetails customUserDetails){
        return generateToken(customUserDetails.getUser(), accessExpiration);
    }

    public String createRefreshToken(CustomUserDetails customUserDetails){
        return generateToken(customUserDetails.getUser(), refreshExpiration);
    }

    /** 토큰 파싱 **/
    ///  검증 로직 포함됨 (parseClaimsJws) - 토큰 자체의 유효성 검증
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(token).getBody();
    }

    // 안씀
//    private  <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = getAllClaimsFromToken(token);
//        return claimsResolver.apply(claims);
//    }
//    public int getUserIdFromToken(String token) {
//        return Integer.parseInt(getClaimFromToken(token,Claims::getSubject));
//    }
//    public String getUserEmailFromToken(String token) {
//        return getClaimFromToken(token,c->c.get("email",String.class));
//    }
//    public Date getExpirationDateFromToken(String token) {
//        return getClaimFromToken(token, Claims::getExpiration);
//    }

    /** 토큰 - 클레임 검증 **/
    /// 요청자와 토큰의 요청자가 같은지 검증
    //DB를 안거쳐감
//    public boolean validateClaims(Claims claims, CustomUserDetails customUserDetails) {
//        String userEmail = claims.get("email", String.class);
//        return userEmail.equals(customUserDetails.getUser().getEmail());
//    }

    public Authentication getAuthenticationFromClaims(Claims claims) {
        String email = claims.get("email", String.class);
        String name = claims.get("name", String.class);
        int userId = Integer.parseInt(claims.getSubject());

        List<GrantedAuthority> authorities = Collections.emptyList();

        CustomUserDetails customUserDetails = new CustomUserDetails(new User(userId, email, name));

        return new UsernamePasswordAuthenticationToken(customUserDetails, "", authorities);
    }
}
