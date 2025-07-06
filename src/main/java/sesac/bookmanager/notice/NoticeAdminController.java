package sesac.bookmanager.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.notice.data.*;
import sesac.bookmanager.security.CustomAdminDetails;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/notice")
public class NoticeAdminController {

    private final NoticeService noticeService;


    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("NoticeCreateRequest", new NoticeCreateRequest());
        return "/admin/notice/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute NoticeCreateRequest request, @AuthenticationPrincipal CustomAdminDetails customAdminDetails) {
        NoticeResponse newNotice = noticeService.createNotice(request, customAdminDetails.getAdmin());
        return "redirect:/admin/notice/" + newNotice.getNoticeId();
    }

    @GetMapping
    public String getNotices(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {

        NoticePageResponse pageResponse = null;

        if(page == null || page < 1) {
            pageResponse = noticeService.searchNotice(new NoticeSearchRequest());
        } else {
            pageResponse = noticeService.searchNotice(new NoticeSearchRequest("", page - 1, 10));
        }


        model.addAttribute("page", pageResponse.getPage() + 1);
        model.addAttribute("totalPages", (pageResponse.getTotalPages() > 0) ? pageResponse.getTotalPages() : 1);
        model.addAttribute("totalCount", pageResponse.getTotalCount());
        model.addAttribute("notices", pageResponse.getNotices());

        model.addAttribute("noticeTypes", NoticeType.values());

        return "/admin/notice/list";
    }

    @GetMapping("/{noticeId}")
    public String getNoticeById(@PathVariable Integer noticeId, Model model) {
        NoticeResponse response = noticeService.getNoticeByIdWithViewCounting(noticeId);
        model.addAttribute("notice", response);
        model.addAttribute("noticeTypes", NoticeType.values());
        return "/admin/notice/one";
    }

    @GetMapping("/{noticeId}/update")
    public String updateNoticeForm(Model model, @PathVariable Integer noticeId) {
        NoticeResponse target = noticeService.getNoticeById(noticeId);
        NoticeUpdateRequest updateRequest = new NoticeUpdateRequest();

        updateRequest.setType(target.getType());
        updateRequest.setTitle(target.getTitle());
        updateRequest.setContent(target.getContent());

        model.addAttribute("noticeId", noticeId);
        model.addAttribute("NoticeUpdateRequest", updateRequest);
        model.addAttribute("noticeTypes", NoticeType.values());
        return "/admin/notice/update";
    }

    @PostMapping("/{noticeId}/update")
    public String updateNotice(
            @PathVariable Integer noticeId, @ModelAttribute NoticeUpdateRequest updateRequest) {

        noticeService.updateNotice(noticeId, updateRequest);
        return "redirect:/admin/notice/" + noticeId;
    }

    @PostMapping("/{noticeId}/delete")
    public String deleteNotice(@PathVariable Integer noticeId) {
        noticeService.deleteNotice(noticeId);

        return "redirect:/admin/notice";
    }
}
