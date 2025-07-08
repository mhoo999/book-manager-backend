package sesac.bookmanager.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sesac.bookmanager.question.data.Question;

import java.util.Collection;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByQuestionType(Boolean questionType);


    List<Question> findByQuestionTypeAndStatusIsLessThan(boolean b, int i);
}
