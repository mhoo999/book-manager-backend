package sesac.bookmanager.reply.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCreateRequest {
    private String content;
    private Integer adminId;

    public Reply toDomain() {
        Reply reply = new Reply();
        reply.setContent(this.content);

        return reply;
    }
}
