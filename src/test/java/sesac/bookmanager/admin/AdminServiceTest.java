package sesac.bookmanager.admin;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import sesac.bookmanager.admin.data.Admin;
import sesac.bookmanager.admin.data.AdminInfoDto;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class AdminServiceTest {

    private final AdminRepository adminRepository = mock(AdminRepository.class);
    private final AdminService adminService = new AdminService(adminRepository);

    @Test
    @DisplayName("관리자 목록 페이징 조회 성공")
    void getAdminsList() {
        // given
        Admin admin1 = new Admin(1, "admin1","admin1234", "010-1234-5678","홍길동", "개발팀", "팀장" );
        Admin admin2 = new Admin(2, "admin2","admin1235", "010-9876-5432","김철수", "마케팅팀", "사원");
        List<Admin> admins = Arrays.asList(admin1, admin2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<Admin> page = new PageImpl<>(admins, pageable, admins.size());

        given(adminRepository.findAll(pageable)).willReturn(page);

        // when
        Page<AdminInfoDto> result = adminService.getAdminsList(0, 10);

        // then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getAdminName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("관리자 상세 조회 성공")
    void getAdminInfo() {
        // given
        Admin admin = new Admin(1, "admin1","admin1234", "010-1234-5678","홍길동", "개발팀", "팀장" );
        given(adminRepository.findById(1)).willReturn(Optional.of(admin));

        // when
        AdminInfoDto dto = adminService.getAdminInfo(1);

        // then
        assertThat(dto.getAdminName()).isEqualTo("홍길동");
        assertThat(dto.getPhoneNumber()).isEqualTo("010-1234-5678");
    }

    @Test
    @DisplayName("관리자 상세 조회 실패 - 없는 ID")
    void getAdminInfoNotFound() {
        // given
        given(adminRepository.findById(999)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> adminService.getAdminInfo(999))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("관리자");
    }

    @Test
    @DisplayName("관리자 전체 개수 조회")
    void getCount() {
        // given
        given(adminRepository.count()).willReturn(5L);

        // when
        Long count = adminService.getCount();

        // then
        assertThat(count).isEqualTo(5L);
    }

    @Test
    @DisplayName("관리자 삭제")
    void deleteAdmin() {
        // given
        int adminId = 1;

        // when
        adminService.deleteAdmin(adminId);

        // then
        verify(adminRepository).deleteById(adminId);
    }
}