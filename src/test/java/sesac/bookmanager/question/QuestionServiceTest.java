package sesac.bookmanager.question;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import sesac.bookmanager.admin.data.Admin;
import sesac.bookmanager.admin.AdminRepository;
import sesac.bookmanager.question.data.*;
import sesac.bookmanager.reply.ReplyRepository;
import sesac.bookmanager.reply.data.Reply;
import sesac.bookmanager.security.CustomUserDetails;
import sesac.bookmanager.user.UserRepository;
import sesac.bookmanager.user.data.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class QuestionServiceTest {

    @Mock private QuestionRepository questionRepository;
    @Mock private UserRepository userRepository;
    @Mock private AdminRepository adminRepository;
    @Mock private ReplyRepository replyRepository;

    @InjectMocks private QuestionService questionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 문의사항_작성_성공() {
        // given
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        User user = new User();
        when(userDetails.getUser()).thenReturn(user);

        QuestionCreateRequest request = new QuestionCreateRequest();
        Question question = new Question();
        question.setUser(user);

        when(questionRepository.save(any())).thenReturn(question);

        // when
        QuestionResponse result = questionService.createQuestion(request, userDetails);

        // then
        assertNotNull(result);
        verify(questionRepository).save(any());
    }

    @Test
    void 문의목록_열람_성공() {
        // given
        Question question = new Question();
        question.setUser(new User(1, "mock@test.com", "testName"));
        Page<Question> page = new PageImpl<>(List.of(question));
        when(questionRepository.findAll(any(Pageable.class))).thenReturn(page);

        QuestionSearchRequest request = new QuestionSearchRequest();
        request.setPage(0);
        request.setSize(10);

        // when
        QuestionPageResponse response = questionService.getAllQuestions(request);

        // then
        assertNotNull(response);
        assertEquals(1, response.getQuestions().size());
    }

    @Test
    void 문의_열람_성공() {
        // given
        Question question = new Question();
        question.setQuestionId(1);
        question.setUser(new User(1, "mock@test.com", "testName"));

        Reply reply = new Reply();
        reply.setContent("reply");
        reply.setQuestion(question);
        reply.setAdmin(new Admin(1, "testAdmin", "secret", "010-1111-2222", "테스트어드민", "테스트부서", "테스터"));

        when(questionRepository.findById(1)).thenReturn(Optional.of(question));
        when(replyRepository.findByQuestion_QuestionId(1)).thenReturn(Optional.of(reply));

        // when
        QuestionWithReplyResponse response = questionService.getQuestionById(1);

        // then
        assertNotNull(response);
        assertEquals("reply", response.getReply().getContent());
    }

    @Test
    void 문의_수정_성공() {
        // given
        Question question = new Question();
        question.setTitle("old");
        question.setContent("old");
        User user = new User();
        user.setEmail("test@email.com");
        question.setUser(user);

        QuestionUpdateRequest updateRequest = new QuestionUpdateRequest();
        updateRequest.setTitle("new");
        updateRequest.setContent("new");

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getUser()).thenReturn(user);

        when(questionRepository.findById(1)).thenReturn(Optional.of(question));

        // when
        QuestionResponse response = questionService.updateQuestion(1, updateRequest, userDetails);

        // then
        assertEquals("new", response.getTitle());
    }

    @Test
    void 문의_처리상태_변경_성공() {
        // given
        Question question = new Question();
        question.setStatus((byte) 0);
        question.setUser(new User(1, "mock@test.com", "testName"));

        QuestionStatusUpdateRequest request = new QuestionStatusUpdateRequest();
        request.setStatus((byte) 1);

        when(questionRepository.findById(1)).thenReturn(Optional.of(question));

        // when
        QuestionResponse response = questionService.updateStatus(1, request);

        // then
        assertEquals((byte) 1, response.getStatus());
    }

    @Test
    void 문의_삭제_성공() {
        // given
        Question question = new Question();
        User user = new User();
        user.setEmail("me@email.com");
        question.setUser(user);

        // when
        questionService.deleteQuestion(1);

        // then
        verify(questionRepository).deleteById(1);
    }
}
