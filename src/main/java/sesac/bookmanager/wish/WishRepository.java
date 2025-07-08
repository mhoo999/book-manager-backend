package sesac.bookmanager.wish;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sesac.bookmanager.wish.data.Wish;
import sesac.bookmanager.wish.data.WishStatus;

import java.util.Collection;
import java.util.List;

@Repository
public interface WishRepository extends JpaRepository<Wish, Integer> {
    List<Wish> findByStatusBetween(WishStatus status, WishStatus status2);
}
