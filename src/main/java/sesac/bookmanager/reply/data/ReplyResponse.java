package sesac.bookmanager.reply.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyResponse {
    private Integer replyId;
    private String content;
    private LocalDateTime createdAt;
    private Integer adminId;
    private Integer questionId;

    public static ReplyResponse from(Reply reply) {
        return new ReplyResponse(
                reply.getReplyId(),
                reply.getContent(),
                reply.getCreatedAt(),
                reply.getAdmin().getAdminId(),
                reply.getQuestion().getQuestionId()
        );
    }
}
