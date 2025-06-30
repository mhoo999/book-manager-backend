package sesac.bookmanager.Security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sesac.bookmanager.user.user.User;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // User 엔티티에 role 컬럼이 없다면 최소 ROLE_USER 하드코딩
        // User 만 관리하므로 디폴트로 적용
        return List.of(() -> "ROLE_USER");
    }


    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPwd();
    }


    // ✅ 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true; // 실무에서는 DB에 status 컬럼으로 연결
    }

    // ✅ 잠김 여부
    @Override
    public boolean isAccountNonLocked() {
        return true; // 잠금 컬럼 없으면 true
    }

    // ✅ 비밀번호 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // PW 만료 관리 안하면 true
    }

    // ✅ 활성 여부
    @Override
    public boolean isEnabled() {
        return !user.getIsDeleted();
    }
}
