package sesac.bookmanager.question.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionUpdateRequest {
    private String title;
    private String content;

    public Question toDomain() {
        Question question = new Question();
        question.setTitle(this.title);
        question.setContent(this.content);

        return question;
    }
}
