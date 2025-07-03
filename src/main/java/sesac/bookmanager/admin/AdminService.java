package sesac.bookmanager.admin;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public Page<AdminInfoDto> getAdminsList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return adminRepository.findAll(pageable)
                .map(admin -> AdminInfoDto.builder()
                        .adminId(admin.getId())
                        .adminAccountId(admin.getAccountId())
                        .adminName(admin.getName())
                        .dept(admin.getDept())
                        .position(admin.getPosition())
                        .phoneNumber(admin.getPhone())
                        .build());
    }

    public AdminInfoDto getAdminInfo(int adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 관리자를 찾을 수 없음"));
        return AdminInfoDto.builder()
                .adminAccountId(admin.getAccountId())
                .adminName(admin.getName())
                .dept(admin.getDept())
                .position(admin.getPosition())
                .phoneNumber(admin.getPhone())
                .build();
    }

    public Long getCount() {
        return adminRepository.count();
    }
}
