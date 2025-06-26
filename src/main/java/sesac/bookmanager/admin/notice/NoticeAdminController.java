package sesac.bookmanager.admin.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.admin.notice.data.NoticeCreateRequest;
import sesac.bookmanager.admin.notice.data.NoticePageResponse;
import sesac.bookmanager.admin.notice.data.NoticeResponse;
import sesac.bookmanager.admin.notice.data.NoticeSearchRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/notice")
public class NoticeAdminController {

    private final NoticeAdminService noticeAdminService;

    @PostMapping
    public ResponseEntity<NoticeResponse> create(@ModelAttribute NoticeCreateRequest request) {
        return ResponseEntity.ok(noticeAdminService.createNotice(request));
    }

    @GetMapping
    public ResponseEntity<NoticePageResponse> getNotices(@ModelAttribute NoticeSearchRequest searchRequest) {
        return ResponseEntity.ok(noticeAdminService.searchNotice(searchRequest));
    }
}
