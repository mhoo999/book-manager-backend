package sesac.bookmanager.admin.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sesac.bookmanager.admin.notice.data.NoticeCreateRequest;
import sesac.bookmanager.admin.notice.data.NoticeResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/notice")
public class NoticeAdminController {

    private final NoticeAdminService noticeAdminService;

    @PostMapping
    public ResponseEntity<NoticeResponse> create(@RequestBody NoticeCreateRequest request) {
        return ResponseEntity.ok(noticeAdminService.createNotice(request));
    }
}
