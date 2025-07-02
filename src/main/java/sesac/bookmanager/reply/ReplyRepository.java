package sesac.bookmanager.reply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sesac.bookmanager.reply.data.Reply;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer> {
    List<Reply> findByQuestion_QuestionIdIn(List<Integer> questionIds);

    Optional<Reply> findByQuestion_QuestionId(Integer questionId);
}
