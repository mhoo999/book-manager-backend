package sesac.bookmanager.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.notice.data.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping("/create")
    public String create(@ModelAttribute NoticeCreateRequest request) {
        NoticeResponse newNotice = noticeService.createNotice(request);
        return "redirect:/api/notice" + newNotice.getNoticeId();
    }

    @ResponseBody
    @GetMapping
    public ResponseEntity<NoticePageResponse> getNotices(@RequestBody NoticeSearchRequest searchRequest) {
        return ResponseEntity.ok(noticeService.searchNotice(searchRequest));
    }

    @ResponseBody
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponse> getNoticeById(@PathVariable Integer noticeId) {
        return ResponseEntity.ok(noticeService.getNoticeById(noticeId));
    }

    @PutMapping("/{noticeId}")
    public String updateNotice(
            @PathVariable Integer noticeId, @ModelAttribute NoticeUpdateRequest updateRequest) {

        noticeService.updateNotice(noticeId, updateRequest);
        return "redirect:/api/notice/" + noticeId;
    }

    @DeleteMapping("/{noticeId}")
    public String deleteNotice(@PathVariable Integer noticeId) {
        noticeService.deleteNotice(noticeId);

        return "redirect:/api/notice";
    }
}
