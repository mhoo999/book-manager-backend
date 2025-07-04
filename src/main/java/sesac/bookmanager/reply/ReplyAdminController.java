package sesac.bookmanager.reply;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.reply.data.ReplyCreateRequest;
import sesac.bookmanager.reply.data.ReplyResponse;
import sesac.bookmanager.reply.data.ReplyUpdateRequest;
import sesac.bookmanager.security.CustomAdminDetails;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/reply")
public class ReplyAdminController {

    private final ReplyService replyService;

    @PostMapping("/{questionId}/create")
    public String replyToQuestion(@PathVariable Integer questionId, @ModelAttribute ReplyCreateRequest replyRequest,
                                  @AuthenticationPrincipal CustomAdminDetails customAdminDetails) {

        replyRequest.setAdminId(customAdminDetails.getAdmin().getId());

        replyService.replyToQuestion(questionId, replyRequest);

        return "redirect:/admin/question/" + questionId;
    }

    /*
    @GetMapping("/{questionId}")
    public String getReplyByQuestionId(@PathVariable Integer questionId) {
        ReplyResponse response = replyService.getReplyByQuestionId(questionId);
        return "/admin/reply/" + questionId;
    }

     */

    @PostMapping("/{questionId}/update")
    public String updateReply(@PathVariable Integer questionId, @ModelAttribute ReplyUpdateRequest request) {
        replyService.updateReply(questionId, request);
        return "redirect:/admin/question/" + questionId;
    }
}
