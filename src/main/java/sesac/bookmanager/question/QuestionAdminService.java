package sesac.bookmanager.question;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac.bookmanager.question.data.QuestionAndReport;
import sesac.bookmanager.question.data.QuestionWithReplyResponse;
import sesac.bookmanager.question.data.Reply;
import sesac.bookmanager.question.data.ReplyCreateRequest;
import sesac.bookmanager.hjdummy.DummyAdmin;
import sesac.bookmanager.hjdummy.DummyAdminRepository;
import sesac.bookmanager.hjdummy.DummyUserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionAdminService {

    private final QuestionRepository questionRepository;
    private final DummyUserRepository userRepository;
    private final DummyAdminRepository adminRepository;
    private final ReplyRepository replyRepository;

    public QuestionWithReplyResponse replyToQuestion(ReplyCreateRequest request) {
        DummyAdmin admin = adminRepository.findById(request.getAdminId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Admin : " + request.getAdminId()));
        QuestionAndReport question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Question : " + request.getQuestionId()));

        Reply newReplytoQuestion = Reply.builder()
                .content(request.getContent())
                .admin(admin)
                .questionAndReport(question)
                .build();

        Reply savedReply = replyRepository.save(newReplytoQuestion);

        return null; // QuestionWithReplyResponse.from(savedReply);
    }
}
