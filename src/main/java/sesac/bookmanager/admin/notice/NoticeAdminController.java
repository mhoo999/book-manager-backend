package sesac.bookmanager.admin.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.admin.notice.data.*;

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

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponse> getNoticeById(@PathVariable Integer noticeId) {
        return ResponseEntity.ok(noticeAdminService.getNoticeById(noticeId));
    }

    @PutMapping("/{noticeId}")
    public ResponseEntity<NoticeResponse> updateNotice(
            @PathVariable Integer noticeId, @ModelAttribute NoticeUpdateRequest updateRequest) {

        return ResponseEntity.ok(noticeAdminService.updateNotice(noticeId, updateRequest));
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Integer noticeId) {
        noticeAdminService.deleteNotice(noticeId);

        return ResponseEntity.noContent().build();
    }
}
