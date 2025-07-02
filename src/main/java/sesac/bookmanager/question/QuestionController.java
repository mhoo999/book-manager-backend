package sesac.bookmanager.question;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.question.data.*;
import sesac.bookmanager.security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/create")
    public ResponseEntity<QuestionResponse> createQuestion(@RequestBody QuestionCreateRequest request,
                                                           @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(questionService.createQuestion(request, customUserDetails));
    }


    @GetMapping
    public ResponseEntity<QuestionPageResponse> getAllQuestions(QuestionSearchRequest search) {
        return ResponseEntity.ok(questionService.getAllQuestions(search));
    }


    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionWithReplyResponse> getQuestionById(@PathVariable Integer questionId) {
        return ResponseEntity.ok(questionService.getQuestionById(questionId));
    }


    @PutMapping("/{questionId}/edit")
    public ResponseEntity<QuestionResponse> updateQuestion(@PathVariable Integer questionId, @RequestBody QuestionUpdateRequest request,
                                                           @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(questionService.updateQuestion(questionId, request, customUserDetails));
    }

    @PutMapping("/{questionId}/progress")
    public ResponseEntity<QuestionResponse> progressEdit(@PathVariable Integer questionId, @ModelAttribute QuestionStatusUpdateRequest request) {
        return ResponseEntity.ok(questionService.updateStatus(questionId, request));
    }


    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Integer questionId,
                                               @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        questionService.deleteQuestion(questionId, customUserDetails);

        return ResponseEntity.noContent().build();
    }
}
