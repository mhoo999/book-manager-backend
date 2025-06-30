package sesac.bookmanager.question.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionWithReplyResponse {

    private Integer questionId;
    private Boolean type;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Integer userId;
    private List<ReplyResponse> replies = new ArrayList<>();

    public static QuestionWithReplyResponse from(QuestionAndReport question) {
        return QuestionWithReplyResponse.builder()
                .questionId(question.getQuestionId())
                .type(question.getType())
                .title(question.getTitle())
                .content(question.getContent())
                .createdAt(question.getCreatedAt())
                .userId(question.getUser().getUserId())
                .build();
    }
}
