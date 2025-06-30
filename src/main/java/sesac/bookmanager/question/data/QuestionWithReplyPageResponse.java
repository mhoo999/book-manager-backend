package sesac.bookmanager.question.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionWithReplyPageResponse {
    private int page;
    private int size;
    private long totalCount;
    private int totalPages;
    private List<QuestionWithReplyResponse> questionsWithReplies;

    public static QuestionWithReplyPageResponse from(List<QuestionWithReplyResponse> questionsWithReplies, int page, int size, long count) {
        int totalPages = (int) Math.ceil((double) count / size);

        return new QuestionWithReplyPageResponse(
                page,
                size,
                count,
                totalPages,
                questionsWithReplies
        );
    }
}
