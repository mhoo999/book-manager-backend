package sesac.bookmanager.question;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac.bookmanager.question.data.*;
import sesac.bookmanager.hjdummy.DummyAdminRepository;
import sesac.bookmanager.hjdummy.DummyUserRepository;
import sesac.bookmanager.reply.ReplyRepository;
import sesac.bookmanager.reply.data.Reply;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final DummyUserRepository userRepository;
    private final DummyAdminRepository adminRepository;
    private final ReplyRepository replyRepository;


    public QuestionResponse createQuestion(QuestionCreateRequest request) {
        Question newQuestion = request.toDomain();
        /// 유저정보 삽입 필요
        newQuestion.setStatus((byte) 0);

        Question savedQuestion = questionRepository.save(newQuestion);

        return QuestionResponse.from(savedQuestion);
    }

    @Transactional(readOnly = true)
    public QuestionPageResponse getAllQuestions(QuestionSearchRequest search) {
        Pageable pageable = PageRequest.of(search.getPage(), search.getSize());

        Page<Question> pagedQuestion = questionRepository.findAll(pageable);

        return QuestionPageResponse.from(pagedQuestion.getContent(), search, pagedQuestion.getTotalElements());
    }


    @Transactional(readOnly = true)
    public QuestionWithReplyResponse getQuestionById(Integer questionId) {
        Question targetQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 문의가 없습니다 : " + questionId));

        Reply targetReply = replyRepository.findByQuestion_QuestionId(questionId).orElse(null);

        return QuestionWithReplyResponse.from(targetQuestion, targetReply);
    }

    public QuestionResponse updateQuestion(Integer questionId, QuestionUpdateRequest request) {
        Question targetQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 문의가 없습니다 : " + questionId));

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
        questionRepository.deleteById(questionId);
    }

}
