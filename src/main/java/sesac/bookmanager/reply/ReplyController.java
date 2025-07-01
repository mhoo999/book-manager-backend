package sesac.bookmanager.reply;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.question.data.QuestionWithReplyResponse;
import sesac.bookmanager.reply.data.ReplyCreateRequest;
import sesac.bookmanager.reply.data.ReplyResponse;
import sesac.bookmanager.reply.data.ReplyUpdateRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply")
public class ReplyController {

    private final ReplyService replyService;

    /*
     * 문의 답변 수정 : `PUT("/{questionId}/reply")`
     */


    @PostMapping("/{questionId}")
    public ResponseEntity<QuestionWithReplyResponse> replyToQuestion(@PathVariable Integer questionId, @ModelAttribute ReplyCreateRequest replyRequest) {
        return ResponseEntity.ok(replyService.replyToQuestion(questionId, replyRequest));
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<ReplyResponse> getReplyByQuestionId(@PathVariable Integer questionId) {
        return ResponseEntity.ok(replyService.getReplyByQuestionId(questionId));
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<ReplyResponse> updateReply(@PathVariable Integer questionId, @ModelAttribute ReplyUpdateRequest request) {
        return ResponseEntity.ok(replyService.updateReply(questionId, request));
    }
}
