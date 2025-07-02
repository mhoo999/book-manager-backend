package sesac.bookmanager.question;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.question.data.*;
import sesac.bookmanager.security.CustomUserDetails;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;

    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<QuestionResponse> createQuestion(@RequestBody QuestionCreateRequest request,
                                                           @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(questionService.createQuestion(request, customUserDetails));
    }

    @ResponseBody
    @GetMapping
    public ResponseEntity<QuestionPageResponse> getAllQuestions(QuestionSearchRequest search) {
        return ResponseEntity.ok(questionService.getAllQuestions(search));
    }

    @ResponseBody
    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionWithReplyResponse> getQuestionById(@PathVariable Integer questionId) {
        return ResponseEntity.ok(questionService.getQuestionById(questionId));
    }

    @ResponseBody
    @PutMapping("/{questionId}/edit")
    public ResponseEntity<QuestionResponse> updateQuestion(@PathVariable Integer questionId, @RequestBody QuestionUpdateRequest request,
                                                           @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(questionService.updateQuestion(questionId, request, customUserDetails));
    }

    @PutMapping("/{questionId}/progress")
    public String progressEdit(@PathVariable Integer questionId, @ModelAttribute QuestionStatusUpdateRequest request) {

        questionService.updateStatus(questionId, request);
        return "redirect:/api/question/" + questionId;
    }

    @DeleteMapping("/{questionId}")
    public String deleteQuestion(@PathVariable Integer questionId,
                                               @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        questionService.deleteQuestion(questionId, customUserDetails);

        return "redirect:/api/question";
    }
}
