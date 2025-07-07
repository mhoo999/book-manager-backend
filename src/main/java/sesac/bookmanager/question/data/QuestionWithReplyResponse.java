package sesac.bookmanager.question.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sesac.bookmanager.reply.data.Reply;
import sesac.bookmanager.reply.data.ReplyResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionWithReplyResponse {

    private QuestionResponse question;
    private ReplyResponse reply;

    public static QuestionWithReplyResponse from(Question question, Reply reply) {
        return QuestionWithReplyResponse.builder()
                .question(QuestionResponse.from(question))
                .reply((reply != null) ? ReplyResponse.from(reply) : null)
                .build();
    }
}
