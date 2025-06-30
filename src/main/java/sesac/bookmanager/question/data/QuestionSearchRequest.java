package sesac.bookmanager.question.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionSearchRequest {
    private int page = 0;
    private int size = 5;
}
