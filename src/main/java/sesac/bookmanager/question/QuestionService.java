package sesac.bookmanager.question;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac.bookmanager.question.data.*;
import sesac.bookmanager.hjdummy.DummyAdmin;
import sesac.bookmanager.hjdummy.DummyAdminRepository;
import sesac.bookmanager.hjdummy.DummyUserRepository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final DummyUserRepository userRepository;
    private final DummyAdminRepository adminRepository;
    private final ReplyRepository replyRepository;

    public QuestionWithReplyResponse replyToQuestion(ReplyCreateRequest request) {
        DummyAdmin admin = adminRepository.findById(request.getAdminId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Admin : " + request.getAdminId()));
        QuestionAndReport question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Question : " + request.getQuestionId()));

        Reply newReplyToQuestion = Reply.builder()
                .content(request.getContent())
                .admin(admin)
                .question(question)
                .build();

        Reply savedReply = replyRepository.save(newReplyToQuestion);

        return QuestionWithReplyResponse.from(question, savedReply);
    }

    public QuestionWithReplyPageResponse getAllQuestions(QuestionSearchRequest search) {
        Pageable pageable = PageRequest.of(search.getPage(), search.getSize());

        Page<QuestionAndReport> pagedQuestion = questionRepository.findAll(pageable);



        // 작업 메모
        // 굳이 문의사항에 대한 답변을 함께 보내지 않는 방향으로 변경
        // 코드 수정 필요!!




        return null;
    }
}
