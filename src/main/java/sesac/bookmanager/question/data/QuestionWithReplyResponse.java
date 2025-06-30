package sesac.bookmanager.question.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionWithReplyResponse {

    private QuestionAndReportResponse question;
    private ReplyResponse reply;

    public static QuestionWithReplyResponse from(QuestionAndReport question, Reply reply) {
        return QuestionWithReplyResponse.builder()
                .question(QuestionAndReportResponse.from(question))
                .reply(ReplyResponse.from(reply))
                .build();
    }
}
