package sesac.bookmanager.wish;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sesac.bookmanager.wish.data.Wish;

@Repository
public interface WishRepository extends JpaRepository<Wish, Integer> {
}
