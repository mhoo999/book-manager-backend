package sesac.bookmanager.reply;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac.bookmanager.admin.data.Admin;
import sesac.bookmanager.admin.AdminRepository;
import sesac.bookmanager.question.QuestionRepository;
import sesac.bookmanager.question.data.Question;
import sesac.bookmanager.question.data.QuestionWithReplyResponse;
import sesac.bookmanager.reply.data.Reply;
import sesac.bookmanager.reply.data.ReplyCreateRequest;
import sesac.bookmanager.reply.data.ReplyResponse;
import sesac.bookmanager.reply.data.ReplyUpdateRequest;
import sesac.bookmanager.user.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final ReplyRepository replyRepository;


    public QuestionWithReplyResponse replyToQuestion(Integer questionId, /*관리자정보, */ ReplyCreateRequest request) {
        Admin admin = adminRepository.findById(request.getAdminId())
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 관리자가 없습니다 : " + request.getAdminId()));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("ID에 해당하는 문의가 없습니다 : " + questionId));

        Reply newReplyToQuestion = Reply.builder()
                .content(request.getContent())
                .admin(admin)
                .question(question)
                .createdAt(LocalDateTime.now())
                .build();

        Reply savedReply = replyRepository.save(newReplyToQuestion);

        return QuestionWithReplyResponse.from(question, savedReply);
    }

    public ReplyResponse getReplyByQuestionId(Integer questionId) {
        return replyRepository.findByQuestion_QuestionId(questionId)
                .map(ReplyResponse::from)
                .orElseThrow(() -> new EntityNotFoundException("문의 ID에 해당하는 답변이 없습니다 : " + questionId));
    }

    public ReplyResponse updateReply(Integer questionId, ReplyUpdateRequest request) {
        Reply targetReply = replyRepository.findByQuestion_QuestionId(questionId)
                .orElseThrow(() -> new EntityNotFoundException("문의 ID에 해당하는 답변이 없습니다 : " + questionId));

        targetReply.setContent(request.getContent());

        return ReplyResponse.from(targetReply);
    }
}
