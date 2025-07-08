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
        NoticePageResponse pageResponse = null;

        if(searchRequest.getPage() < 1) {
            pageResponse = noticeService.searchNotice(new NoticeSearchRequest());
        } else {
            pageResponse = noticeService.searchNotice(new NoticeSearchRequest("", searchRequest.getPage() - 1, 10));
        }

        pageResponse.setPage(searchRequest.getPage());

        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponse> getNoticeById(@PathVariable Integer noticeId) {
        return ResponseEntity.ok(noticeService.getNoticeById(noticeId));
    }
}
