package sesac.bookmanager.question;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.question.data.*;
import sesac.bookmanager.security.CustomUserDetails;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/question")
public class QuestionAdminController {

    private final QuestionService questionService;

    @PostMapping("/create")
    public String createQuestion(@RequestBody QuestionCreateRequest request,
                                                           @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        QuestionResponse newQuestion = questionService.createQuestion(request, customUserDetails);
        return "redirect:/admin/question/" + newQuestion.getQuestionId();
    }

    @GetMapping
    public String getAllQuestions(QuestionSearchRequest search) {

        QuestionPageResponse pageResponse = questionService.getAllQuestions(search);
        return "/admin/question";
    }

    @GetMapping("/{questionId}")
    public String getQuestionById(@PathVariable Integer questionId) {

        QuestionWithReplyResponse response = questionService.getQuestionById(questionId);
        return "/admin/question/" + questionId;
    }

    @PutMapping("/{questionId}/progress")
    public String progressEdit(@PathVariable Integer questionId, @ModelAttribute QuestionStatusUpdateRequest request) {

        questionService.updateStatus(questionId, request);
        return "redirect:/admin/question/" + questionId;
    }

    @DeleteMapping("/{questionId}")
    public String deleteQuestion(@PathVariable Integer questionId) {
        questionService.deleteQuestion(questionId);

        return "redirect:/api/question";
    }
}
