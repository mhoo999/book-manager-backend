package sesac.bookmanager.question;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac.bookmanager.admin.AdminRepository;
import sesac.bookmanager.question.data.*;
import sesac.bookmanager.reply.ReplyRepository;
import sesac.bookmanager.reply.data.Reply;
import sesac.bookmanager.security.CustomUserDetails;
import sesac.bookmanager.user.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final ReplyRepository replyRepository;


    public QuestionResponse createQuestion(QuestionCreateRequest request, CustomUserDetails customUserDetails) {
        Question newQuestion = request.toDomain();

        newQuestion.setStatus((byte) 0);
        newQuestion.setUser(customUserDetails.getUser());
        newQuestion.setCreatedAt(LocalDateTime.now());

        Question savedQuestion = questionRepository.save(newQuestion);

        return QuestionResponse.from(savedQuestion);
    }

    @Transactional(readOnly = true)
    public QuestionPageResponse getAllQuestions(QuestionSearchRequest search) {
        Pageable pageable = PageRequest.of(search.getPage(), search.getSize(), Sort.by(Sort.Direction.DESC, "questionId"));

        Page<QuestionResponse> pagedQuestion = questionRepository.findAll(pageable)
                .map(QuestionResponse::from);

        return QuestionPageResponse.from(pagedQuestion.getContent(), search, pagedQuestion.getTotalElements());
    }


    @Transactional(readOnly = true)
    public QuestionWithReplyResponse getQuestionById(Integer questionId) {
        Question targetQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 문의가 없습니다 : " + questionId));

        Reply targetReply = replyRepository.findByQuestion_QuestionId(questionId).orElse(null);

        return QuestionWithReplyResponse.from(targetQuestion, targetReply);
    }

    public QuestionResponse updateQuestion(Integer questionId, QuestionUpdateRequest request, CustomUserDetails customUserDetails) {
        Question targetQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 문의가 없습니다 : " + questionId));

        if(!targetQuestion.getUser().getEmail().equals(customUserDetails.getUser().getEmail())) {
            throw new RuntimeException("잘못된 유저의 접근입니다");
        }

        targetQuestion.setTitle(request.getTitle());
        targetQuestion.setContent(request.getContent());

        return QuestionResponse.from(targetQuestion);
    }

    public QuestionResponse updateStatus(Integer questionId, QuestionStatusUpdateRequest request) {
        Question targetQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 문의가 없습니다 : " + questionId));

        targetQuestion.setStatus(request.getStatus());

        return QuestionResponse.from(targetQuestion);
    }

    public void deleteQuestion(Integer questionId) {
        Question targetQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 문의가 없습니다 : " + questionId));

        questionRepository.deleteById(questionId);
    }


    public QuestionStatisticsResponse getStatistics() {
        QuestionStatisticsResponse response = new QuestionStatisticsResponse();

        response.setTotalQuestions(questionRepository.findByQuestionType(true).size());
        response.setTotalReports(questionRepository.findByQuestionType(false).size());
        response.setTotalUnsolvedQuestions(questionRepository.findByQuestionTypeAndStatusIsLessThan(true, 2).size());
        response.setTotalUnsolvedReports(questionRepository.findByQuestionTypeAndStatusIsLessThan(false, 2).size());

        return response;
    }
}
