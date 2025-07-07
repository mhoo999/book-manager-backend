package sesac.bookmanager.question;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sesac.bookmanager.question.data.*;
import sesac.bookmanager.reply.data.ReplyCreateRequest;
import sesac.bookmanager.reply.data.ReplyUpdateRequest;
import sesac.bookmanager.security.CustomUserDetails;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/question")
public class QuestionAdminController {

    private final QuestionService questionService;



    @GetMapping
    public String getAllQuestions(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {

        QuestionPageResponse pageResponse = null;

        if(page == null || page < 1) {
            pageResponse = questionService.getAllQuestions(new QuestionSearchRequest());
        } else {
            pageResponse = questionService.getAllQuestions(new QuestionSearchRequest(page - 1, 10));
        }

        model.addAttribute("page", pageResponse.getPage() + 1);
        model.addAttribute("totalPages", (pageResponse.getTotalPages() > 0) ? pageResponse.getTotalPages() : 1);
        model.addAttribute("totalCount", pageResponse.getTotalCount());
        model.addAttribute("questions", pageResponse.getQuestions());

        return "/admin/question/list";
    }

    @GetMapping("/{questionId}")
    public String getQuestionById(Model model, @PathVariable Integer questionId) {

        QuestionWithReplyResponse response = questionService.getQuestionById(questionId);

        QuestionStatusUpdateRequest updateRequest = new QuestionStatusUpdateRequest();
        updateRequest.setStatus(response.getQuestion().getStatus());

        model.addAttribute("question", response.getQuestion());
        model.addAttribute("reply", response.getReply());
        model.addAttribute("QuestionStatusUpdateRequest", updateRequest);


        ReplyCreateRequest replyCreateRequest = new ReplyCreateRequest();
        ReplyUpdateRequest replyUpdateRequest = new ReplyUpdateRequest();

        if(response.getReply() != null) {
            replyUpdateRequest.setContent(response.getReply().getContent());
        }

        model.addAttribute("ReplyCreateRequest", replyCreateRequest);
        model.addAttribute("ReplyUpdateRequest", replyUpdateRequest);

        return "/admin/question/one";
    }

    @PostMapping("/{questionId}/progress")
    public String progressEdit(@PathVariable Integer questionId, @ModelAttribute QuestionStatusUpdateRequest request) {

        questionService.updateStatus(questionId, request);
        return "redirect:/admin/question/" + questionId;
    }

    @PostMapping("/{questionId}/delete")
    public String deleteQuestion(@PathVariable Integer questionId) {
        questionService.deleteQuestion(questionId);

        return "redirect:/admin/question";
    }
}
