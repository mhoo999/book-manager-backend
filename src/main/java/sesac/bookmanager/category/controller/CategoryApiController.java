package sesac.bookmanager.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sesac.bookmanager.category.domain.Category;
import sesac.bookmanager.category.dto.CategoryDto;
import sesac.bookmanager.category.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryApiController {

    private final CategoryService categoryService;

    /**
     * 중분류 조회 API
     * @param largeCode 대분류 코드 (예: "01")
     * @return 중분류 카테고리 리스트
     */
    @GetMapping("/medium/{largeCode}")
    public ResponseEntity<List<CategoryDto>> getMediumCategories(@PathVariable String largeCode) {
        try {
            log.info("중분류 조회 요청: largeCode={}", largeCode);

            List<Category> mediumCategories = categoryService.findMediumCategories(largeCode);
            List<CategoryDto> response = mediumCategories.stream()
                    .map(CategoryDto::from)
                    .collect(Collectors.toList());

            log.info("중분류 조회 성공: largeCode={}, count={}", largeCode, response.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("중분류 조회 실패: largeCode={}, error={}", largeCode, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 소분류 조회 API
     * @param largeCode 대분류 코드 (예: "01")
     * @param mediumCode 중분류 코드 (예: "01")
     * @return 소분류 카테고리 리스트
     */
    @GetMapping("/small/{largeCode}/{mediumCode}")
    public ResponseEntity<List<CategoryDto>> getSmallCategories(
            @PathVariable String largeCode,
            @PathVariable String mediumCode) {
        try {
            log.info("소분류 조회 요청: largeCode={}, mediumCode={}", largeCode, mediumCode);

            List<Category> smallCategories = categoryService.findSmallCategories(largeCode, mediumCode);
            List<CategoryDto> response = smallCategories.stream()
                    .map(CategoryDto::from)
                    .collect(Collectors.toList());

            log.info("소분류 조회 성공: largeCode={}, mediumCode={}, count={}",
                    largeCode, mediumCode, response.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("소분류 조회 실패: largeCode={}, mediumCode={}, error={}",
                    largeCode, mediumCode, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}