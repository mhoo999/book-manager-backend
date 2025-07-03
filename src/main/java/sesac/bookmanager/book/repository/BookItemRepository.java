package sesac.bookmanager.book.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sesac.bookmanager.book.domain.BookItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookItemRepository extends JpaRepository<BookItem, Long> {
    /**
     * 특정 카테고리의 도서 코드들을 정렬하여 조회
     * @param category 카테고리 코드
     * @param pageable 페이징 정보 (정렬 포함)
     * @return 도서 코드 리스트
     */
    @Query("SELECT bi.bookCode FROM BookItem bi " +
            "JOIN bi.book b " +
            "WHERE b.categoryCode = :category " +
            "ORDER BY bi.bookCode DESC")
    List<String> findBookCodesByCategoryOrdered(@Param("category") String category, Pageable pageable);

    Optional<BookItem> findByBookCode(String bookCode);
}
