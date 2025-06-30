package sesac.bookmanager.question.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAndReportResponse {
    private Integer questionId;
    private Boolean type;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public static QuestionAndReportResponse from(QuestionAndReport question) {
        return new QuestionAndReportResponse(
                question.getQuestionId(),
                question.getType(),
                question.getTitle(),
                question.getContent(),
                question.getCreatedAt()
        );
    }
}
