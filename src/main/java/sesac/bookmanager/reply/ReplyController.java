package sesac.bookmanager.reply;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.question.data.QuestionWithReplyResponse;
import sesac.bookmanager.reply.data.ReplyCreateRequest;
import sesac.bookmanager.reply.data.ReplyResponse;
import sesac.bookmanager.reply.data.ReplyUpdateRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/reply")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/{questionId}")
    public String replyToQuestion(@PathVariable Integer questionId, @ModelAttribute ReplyCreateRequest replyRequest) {

        replyService.replyToQuestion(questionId, replyRequest);

        return "redirect:/api/question/" + questionId;
    }

    @ResponseBody
    @GetMapping("/{questionId}")
    public ResponseEntity<ReplyResponse> getReplyByQuestionId(@PathVariable Integer questionId) {
        return ResponseEntity.ok(replyService.getReplyByQuestionId(questionId));
    }

    @PutMapping("/{questionId}")
    public String updateReply(@PathVariable Integer questionId, @ModelAttribute ReplyUpdateRequest request) {
        replyService.updateReply(questionId, request);
        return "redirect:/api/question/" + questionId;
    }
}
