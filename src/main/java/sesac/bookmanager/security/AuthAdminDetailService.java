package sesac.bookmanager.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sesac.bookmanager.admin.data.Admin;
import sesac.bookmanager.admin.AdminRepository;

@Service
@RequiredArgsConstructor
public class AuthAdminDetailService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String userAccountId) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByAccountId(userAccountId)
                .orElseThrow(() -> new UsernameNotFoundException("관리자를 찾을 수 없습니다: " + userAccountId));



        return new CustomAdminDetails(admin);
    }
}
