package sesac.bookmanager.question.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponse {
    private Integer questionId;
    private Boolean type;
    private Byte status;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public static QuestionResponse from(Question question) {
        return new QuestionResponse(
                question.getQuestionId(),
                question.getType(),
                question.getStatus(),
                question.getTitle(),
                question.getContent(),
                question.getCreatedAt()
        );
    }
}
