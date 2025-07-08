package sesac.bookmanager.book.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sesac.bookmanager.book.domain.Book;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Long>, BookQueryRepository {

    // 추천: 재고 있고 최신 출간 순
    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.bookItems bi WHERE b.stock > 0 ORDER BY b.publishedAt DESC")
    List<Book> findRecommendedBooks(Pageable pageable);

    // 신규: 재고 있고 최근 등록 순 (ID 기준)
    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.bookItems bi WHERE b.stock > 0 ORDER BY b.id DESC")
    List<Book> findNewBooks(Pageable pageable);

}
