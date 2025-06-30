package sesac.bookmanager.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sesac.bookmanager.question.data.QuestionAndReport;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionAndReport, Integer> {
}
