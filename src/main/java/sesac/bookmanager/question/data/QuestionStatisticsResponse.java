package sesac.bookmanager.question.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionStatisticsResponse {
    private int totalQuestions;
    private int totalReports;

    private int totalUnsolvedQuestions;
    private int totalUnsolvedReports;

}
