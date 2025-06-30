package sesac.bookmanager.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.notice.data.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping("/create")
    public ResponseEntity<NoticeResponse> create(@ModelAttribute NoticeCreateRequest request) {
        return ResponseEntity.ok(noticeService.createNotice(request));
    }

    @GetMapping
    public ResponseEntity<NoticePageResponse> getNotices(@ModelAttribute NoticeSearchRequest searchRequest) {
        return ResponseEntity.ok(noticeService.searchNotice(searchRequest));
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponse> getNoticeById(@PathVariable Integer noticeId) {
        return ResponseEntity.ok(noticeService.getNoticeById(noticeId));
    }

    @PutMapping("/{noticeId}")
    public ResponseEntity<NoticeResponse> updateNotice(
            @PathVariable Integer noticeId, @ModelAttribute NoticeUpdateRequest updateRequest) {

        return ResponseEntity.ok(noticeService.updateNotice(noticeId, updateRequest));
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Integer noticeId) {
        noticeService.deleteNotice(noticeId);

        return ResponseEntity.noContent().build();
    }
}
