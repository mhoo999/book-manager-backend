package sesac.bookmanager.question;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.question.data.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/create")
    public ResponseEntity<QuestionResponse> createQuestion(@ModelAttribute QuestionCreateRequest request) {
        return ResponseEntity.ok(questionService.createQuestion(request));
        /// 유저정보 추가 필요
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
    public ResponseEntity<QuestionResponse> updateQuestion(@PathVariable Integer questionId, @ModelAttribute QuestionUpdateRequest request) {
        return ResponseEntity.ok(questionService.updateQuestion(questionId, request));
        /// 유저정보 추가 필요
    }

    @PutMapping("/{questionId}/progress")
    public ResponseEntity<QuestionResponse> progressEdit(@PathVariable Integer questionId, @ModelAttribute QuestionStatusUpdateRequest request) {
        return ResponseEntity.ok(questionService.updateStatus(questionId, request));
    }


    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Integer questionId) {
        questionService.deleteQuestion(questionId);

        return ResponseEntity.noContent().build();
    }
}
