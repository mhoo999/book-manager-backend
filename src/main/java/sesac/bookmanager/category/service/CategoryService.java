package sesac.bookmanager.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac.bookmanager.category.domain.Category;
import sesac.bookmanager.category.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 코드로 조회
     */
    public Category findByCode(String fullCode) {
        return categoryRepository.findByFullCode(fullCode)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 코드입니다: " + fullCode));
    }

    /**
     * 카테고리 코드 유효성 검증
     */
    public boolean isValidCategoryCode(String fullCode) {
        if (fullCode == null || fullCode.length() != 6) {
            return false;
        }
        return categoryRepository.existsByFullCode(fullCode);
    }

    /**
     * 대분류 조회 (depth = 1)
     */
    public List<Category> findLargeCategories() {
        try {
            List<Category> categories = categoryRepository.findByDepthOrderByLargeCodeAsc(1);
            log.info("대분류 조회 완료: {} 개", categories.size());

            // 디버그용 로그
            for (Category category : categories) {
                log.debug("대분류: code={}, name={}, depth={}",
                        category.getLargeCode(), category.getName(), category.getDepth());
            }

            return categories;
        } catch (Exception e) {
            log.error("대분류 조회 실패", e);
            throw new RuntimeException("대분류 조회 중 오류가 발생했습니다", e);
        }
    }

    /**
     * 중분류 조회 (depth = 2)
     */
    public List<Category> findMediumCategories(String largeCode) {
        try {
            if (largeCode == null || largeCode.length() != 2) {
                throw new IllegalArgumentException("대분류 코드는 2자리여야 합니다: " + largeCode);
            }

            List<Category> categories = categoryRepository.findByLargeCodeAndDepthOrderByMediumCodeAsc(largeCode, 2);
            log.info("중분류 조회 완료: largeCode={}, count={}", largeCode, categories.size());

            // 디버그용 로그
            for (Category category : categories) {
                log.debug("중분류: largeCode={}, mediumCode={}, name={}, depth={}",
                        category.getLargeCode(), category.getMediumCode(), category.getName(), category.getDepth());
            }

            return categories;
        } catch (Exception e) {
            log.error("중분류 조회 실패: largeCode={}", largeCode, e);
            throw new RuntimeException("중분류 조회 중 오류가 발생했습니다", e);
        }
    }

    /**
     * 소분류 조회 (depth = 3)
     */
    public List<Category> findSmallCategories(String largeCode, String mediumCode) {
        try {
            if (largeCode == null || largeCode.length() != 2) {
                throw new IllegalArgumentException("대분류 코드는 2자리여야 합니다: " + largeCode);
            }
            if (mediumCode == null || mediumCode.length() != 2) {
                throw new IllegalArgumentException("중분류 코드는 2자리여야 합니다: " + mediumCode);
            }

            List<Category> categories = categoryRepository.findByLargeCodeAndMediumCodeAndDepthOrderBySmallCodeAsc(
                    largeCode, mediumCode, 3);
            log.info("소분류 조회 완료: largeCode={}, mediumCode={}, count={}", largeCode, mediumCode, categories.size());

            // 디버그용 로그
            for (Category category : categories) {
                log.debug("소분류: largeCode={}, mediumCode={}, smallCode={}, name={}, fullCode={}",
                        category.getLargeCode(), category.getMediumCode(), category.getSmallCode(),
                        category.getName(), category.getFullCode());
            }

            return categories;
        } catch (Exception e) {
            log.error("소분류 조회 실패: largeCode={}, mediumCode={}", largeCode, mediumCode, e);
            throw new RuntimeException("소분류 조회 중 오류가 발생했습니다", e);
        }
    }

    /**
     * 카테고리 이름으로 풀 경로 조회
     */
    public String getCategoryPath(String fullCode) {
        try {
            Category category = findByCode(fullCode);

            if (category.getDepth() == 1) {
                return category.getName();
            } else if (category.getDepth() == 2) {
                String largeCategoryCode = category.getLargeCode() + "0000";
                Category large = categoryRepository.findByFullCode(largeCategoryCode).orElse(null);
                return (large != null ? large.getName() + " > " : "") + category.getName();
            } else if (category.getDepth() == 3) {
                String largeCategoryCode = category.getLargeCode() + "0000";
                String mediumCategoryCode = category.getLargeCode() + category.getMediumCode() + "00";

                Category large = categoryRepository.findByFullCode(largeCategoryCode).orElse(null);
                Category medium = categoryRepository.findByFullCode(mediumCategoryCode).orElse(null);

                StringBuilder path = new StringBuilder();
                if (large != null) path.append(large.getName()).append(" > ");
                if (medium != null) path.append(medium.getName()).append(" > ");
                path.append(category.getName());

                return path.toString();
            } else {
                return category.getName();
            }
        } catch (Exception e) {
            log.error("카테고리 경로 조회 실패: fullCode={}", fullCode, e);
            return "알 수 없는 카테고리";
        }
    }

    /**
     * 모든 카테고리 조회 (테스트용)
     */
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * 특정 depth의 모든 카테고리 조회 (테스트용)
     */
    public List<Category> findByDepth(Integer depth) {
        return categoryRepository.findByDepthOrderByLargeCodeAsc(depth);
    }
}