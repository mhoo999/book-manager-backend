package sesac.bookmanager.question;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.question.data.QuestionSearchRequest;
import sesac.bookmanager.question.data.QuestionWithReplyPageResponse;
import sesac.bookmanager.question.data.QuestionWithReplyResponse;
import sesac.bookmanager.question.data.ReplyCreateRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/{questionId}/reply")
    public ResponseEntity<QuestionWithReplyResponse> replyToQuestion(@ModelAttribute ReplyCreateRequest replyRequest) {
        return ResponseEntity.ok(questionService.replyToQuestion(replyRequest));
    }

    // * 문의목록 : `GET`
    @GetMapping
    public ResponseEntity<QuestionWithReplyPageResponse> getAllQuestions(QuestionSearchRequest search) {
        return ResponseEntity.ok(questionService.getAllQuestions(search));
    }

    //* 문의열람 : `GET("/{questionId})`
    //* 문의수정(유저용) : `PUT("/{questionId}/edit")`
    //* 문의 답변 작성(관리자용) : `POST("/{questionId}/reply")`
    //* 문의삭제 : `DELETE("/{questionId}")`
}
