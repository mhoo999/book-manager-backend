package sesac.bookmanager.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.notice.data.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/admin/notice")
public class NoticeAdminController {

    private final NoticeService noticeService;

    @PostMapping("/create")
    public String create(@ModelAttribute NoticeCreateRequest request) {
        NoticeResponse newNotice = noticeService.createNotice(request);
        return "redirect:/api/admin/notice/" + newNotice.getNoticeId();
    }

    @GetMapping
    public String getNotices(NoticeSearchRequest searchRequest) {

        NoticePageResponse pageResponse = noticeService.searchNotice(searchRequest);
        return "redirect:/api/admin/notice";
    }

    @GetMapping("/api/notice/{noticeId}")
    public String getNoticeById(@PathVariable Integer noticeId) {
        NoticeResponse response = noticeService.getNoticeById(noticeId);
        return "redirect:/api/admin/notice/" + response.getNoticeId();
    }

    @PutMapping("/{noticeId}")
    public String updateNotice(
            @PathVariable Integer noticeId, @ModelAttribute NoticeUpdateRequest updateRequest) {

        noticeService.updateNotice(noticeId, updateRequest);
        return "redirect:/api/admin/notice/" + noticeId;
    }

    @DeleteMapping("/{noticeId}")
    public String deleteNotice(@PathVariable Integer noticeId) {
        noticeService.deleteNotice(noticeId);

        return "redirect:/api/admin/notice";
    }
}
