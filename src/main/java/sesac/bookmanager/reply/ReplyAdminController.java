package sesac.bookmanager.reply;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.reply.data.ReplyCreateRequest;
import sesac.bookmanager.reply.data.ReplyResponse;
import sesac.bookmanager.reply.data.ReplyUpdateRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/reply")
public class ReplyAdminController {

    private final ReplyService replyService;

    @PostMapping("/{questionId}")
    public String replyToQuestion(@PathVariable Integer questionId, @ModelAttribute ReplyCreateRequest replyRequest) {

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

    @PutMapping("/{questionId}")
    public String updateReply(@PathVariable Integer questionId, @ModelAttribute ReplyUpdateRequest request) {
        replyService.updateReply(questionId, request);
        return "redirect:/admin/question/" + questionId;
    }
}
