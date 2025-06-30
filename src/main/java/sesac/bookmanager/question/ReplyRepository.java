package sesac.bookmanager.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sesac.bookmanager.question.data.Reply;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer> {
}
