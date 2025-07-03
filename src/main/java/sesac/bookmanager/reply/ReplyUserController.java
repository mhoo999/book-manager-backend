package sesac.bookmanager.reply;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.reply.data.ReplyCreateRequest;
import sesac.bookmanager.reply.data.ReplyResponse;
import sesac.bookmanager.reply.data.ReplyUpdateRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply")
public class ReplyUserController {

    private final ReplyService replyService;


    @GetMapping("/{questionId}")
    public ResponseEntity<ReplyResponse> getReplyByQuestionId(@PathVariable Integer questionId) {
        return ResponseEntity.ok(replyService.getReplyByQuestionId(questionId));
    }
}
