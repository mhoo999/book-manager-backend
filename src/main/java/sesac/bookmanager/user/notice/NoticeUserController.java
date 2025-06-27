package sesac.bookmanager.user.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.admin.notice.data.NoticePageResponse;
import sesac.bookmanager.admin.notice.data.NoticeResponse;
import sesac.bookmanager.admin.notice.data.NoticeSearchRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeUserController {

    private final NoticeUserService noticeUserService;

    @GetMapping
    public ResponseEntity<NoticePageResponse> getNotices(@ModelAttribute NoticeSearchRequest searchRequest) {
        return ResponseEntity.ok(noticeUserService.searchNotice(searchRequest));
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponse> getNoticeById(@PathVariable Integer noticeId) {
        return ResponseEntity.ok(noticeUserService.getNoticeById(noticeId));
    }
}
