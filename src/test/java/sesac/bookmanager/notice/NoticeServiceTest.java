package sesac.bookmanager.notice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.*;

import sesac.bookmanager.admin.Admin;
import sesac.bookmanager.admin.AdminRepository;
import sesac.bookmanager.notice.data.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class NoticeServiceTest {

    @InjectMocks
    private NoticeService noticeService;

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private AdminRepository adminRepository;

    private Admin admin;
    private Notice notice;

    @BeforeEach
    void setUp() {
        admin = Admin.builder()
                .id(1)
                .build();

        notice = Notice.builder()
                .noticeId(1)
                .title("공지 제목")
                .content("공지 내용")
                .type(NoticeType.DEFAULT)
                .createdAt(LocalDateTime.now())
                .admin(admin)
                .build();
    }

    @Test
    void 공지사항_생성_성공() {
        NoticeCreateRequest request = new NoticeCreateRequest();
        request.setTitle("공지 제목");
        request.setContent("공지 내용");
        request.setType(NoticeType.DEFAULT);

        when(adminRepository.findById(1)).thenReturn(Optional.of(admin));
        when(noticeRepository.save(any(Notice.class))).thenReturn(notice);

        NoticeResponse response = noticeService.createNotice(request, admin);

        assertEquals("공지 제목", response.getTitle());
        assertEquals(NoticeType.DEFAULT, response.getType());
        verify(adminRepository).findById(1);
        verify(noticeRepository).save(any(Notice.class));
    }

    @Test
    void 공지사항_조회_페이징_성공() {
        NoticeSearchRequest request = new NoticeSearchRequest();
        request.setPage(0);
        request.setSize(10);
        request.setTitle("공지");

        Page<Notice> resultPage = new PageImpl<>(List.of(notice));
        when(noticeRepository.findByTitleContaining(eq("공지"), any(Pageable.class)))
                .thenReturn(resultPage);

        NoticePageResponse response = noticeService.searchNotice(request);

        assertEquals(1, response.getNotices().size());
        assertEquals("공지 제목", response.getNotices().get(0).getTitle());
    }

    @Test
    void 공지사항_ID로_조회_성공() {
        when(noticeRepository.findById(1)).thenReturn(Optional.of(notice));

        NoticeResponse response = noticeService.getNoticeById(1);

        assertEquals("공지 제목", response.getTitle());
        verify(noticeRepository).findById(1);
    }

    @Test
    void 공지사항_수정_성공() {
        NoticeUpdateRequest request = new NoticeUpdateRequest();
        request.setTitle("수정된 제목");
        request.setContent("수정된 내용");
        request.setType(NoticeType.DAYOFF);

        when(noticeRepository.findById(1)).thenReturn(Optional.of(notice));

        NoticeResponse response = noticeService.updateNotice(1, request);

        assertEquals("수정된 제목", response.getTitle());
        assertEquals(NoticeType.DAYOFF, response.getType());
    }

    @Test
    void 공지사항_삭제_성공() {
        doNothing().when(noticeRepository).deleteById(1);

        noticeService.deleteNotice(1);

        verify(noticeRepository).deleteById(1);
    }
}
