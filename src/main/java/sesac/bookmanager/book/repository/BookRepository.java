package sesac.bookmanager.book.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sesac.bookmanager.book.domain.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
}
