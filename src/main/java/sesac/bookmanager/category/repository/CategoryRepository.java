package sesac.bookmanager.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sesac.bookmanager.category.domain.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByFullCode(String fullCode);

    boolean existsByFullCode(String fullCode);

    // depth로 조회 (정렬 포함) - Integer 타입 사용
    List<Category> findByDepthOrderByLargeCodeAsc(Integer depth);

    // 대분류 코드와 depth로 조회 (중분류용) - Integer 타입 사용
    List<Category> findByLargeCodeAndDepthOrderByMediumCodeAsc(String largeCode, Integer depth);

    // 대분류, 중분류 코드와 depth로 조회 (소분류용) - Integer 타입 사용
    List<Category> findByLargeCodeAndMediumCodeAndDepthOrderBySmallCodeAsc(
            String largeCode, String mediumCode, Integer depth);
}
