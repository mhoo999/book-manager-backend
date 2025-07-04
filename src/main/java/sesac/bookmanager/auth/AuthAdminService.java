package sesac.bookmanager.auth;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac.bookmanager.admin.data.Admin;
import sesac.bookmanager.admin.AdminRepository;
import sesac.bookmanager.auth.data.AdminLoginFormDto;
import sesac.bookmanager.auth.data.AdminRegisterRequest;

@Service
public class AuthAdminService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AdminRepository adminRepository;

    public AuthAdminService(PasswordEncoder passwordEncoder,
                            @Qualifier("adminAuthenticationManager") AuthenticationManager authenticationManager,
                            AdminRepository adminRepository) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.adminRepository = adminRepository;
    }

    @Transactional(readOnly = true)
    public void login(AdminLoginFormDto adminLoginFormDto) {
        Authentication authentication =  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(adminLoginFormDto.getAccountId(), adminLoginFormDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //CustomAdminDetails customAdminDetails = (CustomAdminDetails) authentication.getPrincipal();
    }

    @Transactional(readOnly = true)
    public void logout() {
        //SecurityContext의 해당 세션 정보 제거 (세션 자체는 아님)
        SecurityContextHolder.clearContext();
    }

    @Transactional(readOnly = true)
    public boolean checkId(String accountId) {
        return adminRepository.existsByAccountId(accountId);
    }

    @Transactional
    public void register(AdminRegisterRequest request) {
        if (adminRepository.existsByAccountId(request.getAdminAccountId())) {
            throw new IllegalStateException("이미 사용중인 아이디입니다.");
        }

        Admin newAdmin = Admin.builder()
                        .accountId(request.getAdminAccountId())
                        .pwd(passwordEncoder.encode(request.getPassword()))
                        .phone(request.getPhoneNo())
                        .name(request.getAdminName())
                        .dept(request.getDept())
                        .position(request.getPosition())
                        .build();
        adminRepository.save(newAdmin);
    }
}
