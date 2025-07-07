package sesac.bookmanager.reply;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sesac.bookmanager.admin.AdminRepository;
import sesac.bookmanager.admin.data.Admin;
import sesac.bookmanager.question.QuestionRepository;
import sesac.bookmanager.question.data.Question;
import sesac.bookmanager.question.data.QuestionWithReplyResponse;
import sesac.bookmanager.reply.data.*;
import sesac.bookmanager.user.UserRepository;
import sesac.bookmanager.user.data.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReplyServiceTest {


    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private ReplyRepository replyRepository;


    @InjectMocks
    private ReplyService replyService;

    private Admin admin;
    private User user;
    private Question question;
    private Reply reply;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        admin = Admin.builder()
                .id(1)
                .build();

        user = User.builder()
                .id(1)
                .build();

        question = Question.builder()
                .questionId(1)
                .user(user)
                .title("질문").build();

        reply = Reply.builder()
                .replyId(1)
                .content("답변입니다")
                .admin(admin)
                .question(question)
                .build();
    }

    @Test
    void 답변하기_성공() {
        // Given
        ReplyCreateRequest request = new ReplyCreateRequest("답변입니다", admin.getId());

        when(adminRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(questionRepository.findById(question.getQuestionId())).thenReturn(Optional.of(question));

        // When
        when(replyRepository.save(any(Reply.class))).thenReturn(reply);

        QuestionWithReplyResponse response = replyService.replyToQuestion(question.getQuestionId(), request);

        // Then
        assertEquals("답변입니다", response.getReply().getContent());
        verify(replyRepository).save(any(Reply.class));
    }

    @Test
    void 답변단독조회_성공() {
        // Given
        when(replyRepository.findByQuestion_QuestionId(question.getQuestionId())).thenReturn(Optional.of(reply));

        // When
        ReplyResponse response = replyService.getReplyByQuestionId(question.getQuestionId());

        // Then
        assertNotNull(response);
        assertEquals("답변입니다", response.getContent());
        verify(replyRepository).findByQuestion_QuestionId(question.getQuestionId());
    }

    @Test
    void 답변수정_성공() {
        // Given
        when(replyRepository.findByQuestion_QuestionId(question.getQuestionId())).thenReturn(Optional.of(reply));

        ReplyUpdateRequest request = new ReplyUpdateRequest("수정된 답변");

        // When
        ReplyResponse response = replyService.updateReply(question.getQuestionId(), request);

        // Then
        assertEquals("수정된 답변", reply.getContent());
        assertEquals("수정된 답변", response.getContent());
        verify(replyRepository).findByQuestion_QuestionId(question.getQuestionId());
    }
}
