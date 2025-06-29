package sesac.bookmanager.book.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sesac.bookmanager.book.domain.BookItem;

import java.util.List;

@Repository
public interface BookItemRepository extends JpaRepository<BookItem, Long> {
    @Query("""
    SELECT bi.bookCode
    FROM BookItem bi
    WHERE bi.bookCode LIKE CONCAT('BK', :category, '%')
    AND LENGTH(bi.bookCode) = LENGTH(CONCAT('BK', :category)) + 4
    ORDER BY bi.bookCode DESC
    """)
    List<String> findBookCodesByCategoryOrdered(@Param("category") String category, Pageable pageable);

}
