package sesac.bookmanager.question.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionStatusUpdateRequest {
    private Byte status;

    public Question toDomain() {
        Question question = new Question();
        question.setStatus(status);

        return question;
    }
}
