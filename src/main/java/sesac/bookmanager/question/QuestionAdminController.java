package sesac.bookmanager.question;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sesac.bookmanager.question.data.QuestionWithReplyResponse;
import sesac.bookmanager.question.data.ReplyCreateRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/question")
public class QuestionAdminController {

    private final QuestionAdminService questionAdminService;

    @PostMapping("/{questionId}/reply")
    public ResponseEntity<QuestionWithReplyResponse> replyToQuestion(@ModelAttribute ReplyCreateRequest replyRequest) {
        return ResponseEntity.ok(questionAdminService.replyToQuestion(replyRequest));
    }
}
