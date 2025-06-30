package sesac.bookmanager.question.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCreateRequest {
    private String content;
    private LocalDateTime createdAt;
    private Integer adminId;
    private Integer questionId;
}
