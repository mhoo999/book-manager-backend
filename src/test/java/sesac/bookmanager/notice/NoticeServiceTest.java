package sesac.bookmanager.notice;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import sesac.bookmanager.admin.notice.NoticeAdminRepository;
import sesac.bookmanager.admin.notice.NoticeAdminService;
import sesac.bookmanager.admin.notice.data.*;
import sesac.bookmanager.hjdummy.DummyAdmin;
import sesac.bookmanager.hjdummy.DummyAdminRepository;
import sesac.bookmanager.hjdummy.DummyUser;
import sesac.bookmanager.hjdummy.DummyUserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {

    private NoticeAdminRepository noticeAdminRepository;

    private DummyAdminRepository adminRepository;

    private NoticeAdminService noticeAdminService;

    @BeforeEach
    void setUp() {
        noticeAdminRepository = mock(NoticeAdminRepository.class);
        adminRepository = mock(DummyAdminRepository.class);
        noticeAdminService = new NoticeAdminService(noticeAdminRepository, adminRepository);
    }

    @Test
    void 관리자_createNotice_성공_케이스() {
        // given
        DummyAdmin admin = new DummyAdmin();
        admin.setAdminId(1);
        NoticeCreateRequest request = new NoticeCreateRequest();
        request.setAdminId(1);
        request.setTitle("Test");
        request.setContent("Content");
        request.setType(NoticeType.DEFAULT);

        when(adminRepository.findById(1)).thenReturn(Optional.of(admin));

        Notice savedNotice = Notice.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .admin(admin)
                .createdAt(LocalDateTime.now())
                .build();

        when(noticeAdminRepository.save(any(Notice.class)))
                .thenReturn(savedNotice);

        // when
        NoticeResponse result = noticeAdminService.createNotice(request);

        // then
        assertEquals("Test", result.getTitle());
        assertEquals("Content", result.getContent());
        verify(adminRepository).findById(1);
        verify(noticeAdminRepository).save(any(Notice.class));
    }

    @Test
    void 관리자_createNotice_예외_케이스() {
        // given
        NoticeCreateRequest request = new NoticeCreateRequest();
        request.setAdminId(99);

        when(adminRepository.findById(99)).thenReturn(Optional.empty());

        // when / then
        assertThrows(EntityNotFoundException.class,
                () -> noticeAdminService.createNotice(request));
    }

    @Test
    void 관리자_searchNotice_성공_케이스() {
        // given
        Notice notice = Notice.builder()
                .title("Test Notice")
                .content("Notice Content")
                .createdAt(LocalDateTime.now())
                .build();

        List<Notice> noticeList = List.of(notice);
        Page<Notice> page = new PageImpl<>(noticeList);

        NoticeSearchRequest searchRequest = new NoticeSearchRequest();
        searchRequest.setPage(0);
        searchRequest.setSize(10);
        searchRequest.setTitle("Test");

        when(noticeAdminRepository.findByTitleContaining(eq("Test"), any(Pageable.class)))
                .thenReturn(page);

        // when
        NoticePageResponse result = noticeAdminService.searchNotice(searchRequest);

        // then
        assertEquals(1, result.getNotices().size());
        assertEquals("Test Notice", result.getNotices().get(0).getTitle());
        verify(noticeAdminRepository).findByTitleContaining(eq("Test"), any(Pageable.class));
    }
}