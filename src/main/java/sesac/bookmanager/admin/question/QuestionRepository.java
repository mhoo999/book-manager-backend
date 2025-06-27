package sesac.bookmanager.admin.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sesac.bookmanager.admin.question.data.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
