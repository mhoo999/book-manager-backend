package sesac.bookmanager.question.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QuestionPageResponse {
    private int page;
    private int size;
    private long totalCount;
    private int totalPages;
    private List<Question> questions;

    public static QuestionPageResponse from(List<Question> questions, QuestionSearchRequest search, Long count) {
        int totalPages = (int) Math.ceil((double) count / search.getSize());
        return new QuestionPageResponse(
                search.getPage(),
                search.getSize(),
                count,
                totalPages,
                questions
        );
    }
}
