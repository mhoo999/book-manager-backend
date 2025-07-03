package sesac.bookmanager.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.notice.data.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeUserController {

    private final NoticeService noticeService;


    @GetMapping
    public ResponseEntity<NoticePageResponse> getNotices(NoticeSearchRequest searchRequest) {
        return ResponseEntity.ok(noticeService.searchNotice(searchRequest));
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponse> getNoticeById(@PathVariable Integer noticeId) {
        return ResponseEntity.ok(noticeService.getNoticeById(noticeId));
    }
}
