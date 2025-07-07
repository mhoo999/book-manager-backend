package sesac.bookmanager.category.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sesac.bookmanager.category.domain.Category;
import sesac.bookmanager.category.repository.CategoryRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryService 테스트")
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category largeCategory;
    private Category mediumCategory;
    private Category smallCategory;

    @BeforeEach
    void setUp() {
        // 대분류 카테고리 (depth = 1)
        largeCategory = new Category();
        largeCategory.setId(1);
        largeCategory.setName("문학");
        largeCategory.setDepth(1);
        largeCategory.setLargeCode("10");
        largeCategory.setMediumCode("00");
        largeCategory.setSmallCode("00");
        largeCategory.setFullCode("100000");

        // 중분류 카테고리 (depth = 2)
        mediumCategory = new Category();
        mediumCategory.setId(2);
        mediumCategory.setName("한국문학");
        mediumCategory.setDepth(2);
        mediumCategory.setLargeCode("10");
        mediumCategory.setMediumCode("01");
        mediumCategory.setSmallCode("00");
        mediumCategory.setFullCode("100100");

        // 소분류 카테고리 (depth = 3)
        smallCategory = new Category();
        smallCategory.setId(3);
        smallCategory.setName("현대소설");
        smallCategory.setDepth(3);
        smallCategory.setLargeCode("10");
        smallCategory.setMediumCode("01");
        smallCategory.setSmallCode("01");
        smallCategory.setFullCode("100101");
    }

    @Test
    @DisplayName("카테고리 코드로 조회 성공")
    void findByCode_Success() {
        // given
        String fullCode = "100101";
        given(categoryRepository.findByFullCode(fullCode)).willReturn(Optional.of(smallCategory));

        // when
        Category result = categoryService.findByCode(fullCode);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getFullCode()).isEqualTo(fullCode);
        assertThat(result.getName()).isEqualTo("현대소설");
        assertThat(result.getDepth()).isEqualTo(3);
    }

    @Test
    @DisplayName("카테고리 코드로 조회 실패 - 존재하지 않는 코드")
    void findByCode_Fail_NotFound() {
        // given
        String fullCode = "999999";
        given(categoryRepository.findByFullCode(fullCode)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> categoryService.findByCode(fullCode))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 카테고리 코드입니다: 999999");
    }

    @Test
    @DisplayName("카테고리 코드 유효성 검증 성공")
    void isValidCategoryCode_Success() {
        // given
        String fullCode = "100101";
        given(categoryRepository.existsByFullCode(fullCode)).willReturn(true);

        // when
        boolean result = categoryService.isValidCategoryCode(fullCode);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("카테고리 코드 유효성 검증 실패 - null 코드")
    void isValidCategoryCode_Fail_NullCode() {
        // when
        boolean result = categoryService.isValidCategoryCode(null);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("카테고리 코드 유효성 검증 실패 - 잘못된 길이")
    void isValidCategoryCode_Fail_InvalidLength() {
        // when
        boolean result1 = categoryService.isValidCategoryCode("12345");  // 5자리
        boolean result2 = categoryService.isValidCategoryCode("1234567"); // 7자리

        // then
        assertThat(result1).isFalse();
        assertThat(result2).isFalse();
    }

    @Test
    @DisplayName("카테고리 코드 유효성 검증 실패 - 존재하지 않는 코드")
    void isValidCategoryCode_Fail_NotExists() {
        // given
        String fullCode = "999999";
        given(categoryRepository.existsByFullCode(fullCode)).willReturn(false);

        // when
        boolean result = categoryService.isValidCategoryCode(fullCode);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("대분류 조회 성공")
    void findLargeCategories_Success() {
        // given
        Category literature = createCategory(1, "문학", 1, "10", "00", "00", "100000");
        Category science = createCategory(2, "과학", 1, "20", "00", "00", "200000");
        Category history = createCategory(3, "역사", 1, "30", "00", "00", "300000");

        List<Category> categories = Arrays.asList(literature, science, history);
        given(categoryRepository.findByDepthOrderByLargeCodeAsc(1)).willReturn(categories);

        // when
        List<Category> result = categoryService.findLargeCategories();

        // then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getName()).isEqualTo("문학");
        assertThat(result.get(1).getName()).isEqualTo("과학");
        assertThat(result.get(2).getName()).isEqualTo("역사");
        verify(categoryRepository).findByDepthOrderByLargeCodeAsc(1);
    }

    @Test
    @DisplayName("대분류 조회 실패 - 데이터베이스 오류")
    void findLargeCategories_Fail_DatabaseError() {
        // given
        given(categoryRepository.findByDepthOrderByLargeCodeAsc(1))
                .willThrow(new RuntimeException("Database connection failed"));

        // when & then
        assertThatThrownBy(() -> categoryService.findLargeCategories())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("대분류 조회 중 오류가 발생했습니다");
    }

    @Test
    @DisplayName("중분류 조회 성공")
    void findMediumCategories_Success() {
        // given
        String largeCode = "10";
        Category korean = createCategory(1, "한국문학", 2, "10", "01", "00", "100100");
        Category foreign = createCategory(2, "외국문학", 2, "10", "02", "00", "100200");

        List<Category> categories = Arrays.asList(korean, foreign);
        given(categoryRepository.findByLargeCodeAndDepthOrderByMediumCodeAsc(largeCode, 2))
                .willReturn(categories);

        // when
        List<Category> result = categoryService.findMediumCategories(largeCode);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("한국문학");
        assertThat(result.get(1).getName()).isEqualTo("외국문학");
        verify(categoryRepository).findByLargeCodeAndDepthOrderByMediumCodeAsc(largeCode, 2);
    }

    @Test
    @DisplayName("중분류 조회 실패 - 잘못된 대분류 코드")
    void findMediumCategories_Fail_InvalidLargeCode() {
        // when & then - 서비스에서 try-catch로 RuntimeException으로 래핑됨
        assertThatThrownBy(() -> categoryService.findMediumCategories("1"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("중분류 조회 중 오류가 발생했습니다")
                .hasCauseInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> categoryService.findMediumCategories("123"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("중분류 조회 중 오류가 발생했습니다")
                .hasCauseInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> categoryService.findMediumCategories(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("중분류 조회 중 오류가 발생했습니다")
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("소분류 조회 성공")
    void findSmallCategories_Success() {
        // given
        String largeCode = "10";
        String mediumCode = "01";
        Category novel = createCategory(1, "소설", 3, "10", "01", "01", "100101");
        Category poetry = createCategory(2, "시", 3, "10", "01", "02", "100102");

        List<Category> categories = Arrays.asList(novel, poetry);
        given(categoryRepository.findByLargeCodeAndMediumCodeAndDepthOrderBySmallCodeAsc(largeCode, mediumCode, 3))
                .willReturn(categories);

        // when
        List<Category> result = categoryService.findSmallCategories(largeCode, mediumCode);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("소설");
        assertThat(result.get(1).getName()).isEqualTo("시");
        verify(categoryRepository).findByLargeCodeAndMediumCodeAndDepthOrderBySmallCodeAsc(largeCode, mediumCode, 3);
    }

    @Test
    @DisplayName("소분류 조회 실패 - 잘못된 코드")
    void findSmallCategories_Fail_InvalidCodes() {
        // when & then - 서비스에서 try-catch로 RuntimeException으로 래핑됨
        assertThatThrownBy(() -> categoryService.findSmallCategories("1", "01"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("소분류 조회 중 오류가 발생했습니다")
                .hasCauseInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> categoryService.findSmallCategories("10", "1"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("소분류 조회 중 오류가 발생했습니다")
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("카테고리 경로 조회 - 대분류")
    void getCategoryPath_LargeCategory() {
        // given
        String fullCode = "100000";
        given(categoryRepository.findByFullCode(fullCode)).willReturn(Optional.of(largeCategory));

        // when
        String result = categoryService.getCategoryPath(fullCode);

        // then
        assertThat(result).isEqualTo("문학");
    }

    @Test
    @DisplayName("카테고리 경로 조회 - 중분류")
    void getCategoryPath_MediumCategory() {
        // given
        String fullCode = "100100";
        given(categoryRepository.findByFullCode(fullCode)).willReturn(Optional.of(mediumCategory));
        given(categoryRepository.findByFullCode("100000")).willReturn(Optional.of(largeCategory));

        // when
        String result = categoryService.getCategoryPath(fullCode);

        // then
        assertThat(result).isEqualTo("문학 > 한국문학");
    }

    @Test
    @DisplayName("카테고리 경로 조회 - 소분류")
    void getCategoryPath_SmallCategory() {
        // given
        String fullCode = "100101";
        given(categoryRepository.findByFullCode(fullCode)).willReturn(Optional.of(smallCategory));
        given(categoryRepository.findByFullCode("100000")).willReturn(Optional.of(largeCategory));
        given(categoryRepository.findByFullCode("100100")).willReturn(Optional.of(mediumCategory));

        // when
        String result = categoryService.getCategoryPath(fullCode);

        // then
        assertThat(result).isEqualTo("문학 > 한국문학 > 현대소설");
    }

    @Test
    @DisplayName("카테고리 경로 조회 - 상위 카테고리 없는 경우")
    void getCategoryPath_WithoutParentCategory() {
        // given
        String fullCode = "100100";
        given(categoryRepository.findByFullCode(fullCode)).willReturn(Optional.of(mediumCategory));
        given(categoryRepository.findByFullCode("100000")).willReturn(Optional.empty());

        // when
        String result = categoryService.getCategoryPath(fullCode);

        // then
        assertThat(result).isEqualTo("한국문학");
    }

    @Test
    @DisplayName("카테고리 경로 조회 실패 - 예외 발생 시 기본값 반환")
    void getCategoryPath_Exception_ReturnDefault() {
        // given
        String fullCode = "100101";
        given(categoryRepository.findByFullCode(fullCode))
                .willThrow(new RuntimeException("Database error"));

        // when
        String result = categoryService.getCategoryPath(fullCode);

        // then
        assertThat(result).isEqualTo("알 수 없는 카테고리");
    }

    @Test
    @DisplayName("모든 카테고리 조회")
    void findAllCategories_Success() {
        // given
        List<Category> allCategories = Arrays.asList(largeCategory, mediumCategory, smallCategory);
        given(categoryRepository.findAll()).willReturn(allCategories);

        // when
        List<Category> result = categoryService.findAllCategories();

        // then
        assertThat(result).hasSize(3);
        assertThat(result).containsExactly(largeCategory, mediumCategory, smallCategory);
    }

    @Test
    @DisplayName("특정 depth 카테고리 조회")
    void findByDepth_Success() {
        // given
        Integer depth = 2;
        List<Category> mediumCategories = Arrays.asList(mediumCategory);
        given(categoryRepository.findByDepthOrderByLargeCodeAsc(depth)).willReturn(mediumCategories);

        // when
        List<Category> result = categoryService.findByDepth(depth);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDepth()).isEqualTo(2);
        assertThat(result.get(0).getName()).isEqualTo("한국문학");
    }

    @Test
    @DisplayName("중분류 조회 실패 - 데이터베이스 오류")
    void findMediumCategories_Fail_DatabaseError() {
        // given
        String largeCode = "10";
        given(categoryRepository.findByLargeCodeAndDepthOrderByMediumCodeAsc(largeCode, 2))
                .willThrow(new RuntimeException("Database connection failed"));

        // when & then
        assertThatThrownBy(() -> categoryService.findMediumCategories(largeCode))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("중분류 조회 중 오류가 발생했습니다");
    }

    @Test
    @DisplayName("소분류 조회 실패 - 데이터베이스 오류")
    void findSmallCategories_Fail_DatabaseError() {
        // given
        String largeCode = "10";
        String mediumCode = "01";
        given(categoryRepository.findByLargeCodeAndMediumCodeAndDepthOrderBySmallCodeAsc(largeCode, mediumCode, 3))
                .willThrow(new RuntimeException("Database connection failed"));

        // when & then
        assertThatThrownBy(() -> categoryService.findSmallCategories(largeCode, mediumCode))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("소분류 조회 중 오류가 발생했습니다");
    }

    // 헬퍼 메서드
    private Category createCategory(Integer id, String name, Integer depth,
                                    String largeCode, String mediumCode, String smallCode, String fullCode) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setDepth(depth);
        category.setLargeCode(largeCode);
        category.setMediumCode(mediumCode);
        category.setSmallCode(smallCode);
        category.setFullCode(fullCode);
        return category;
    }
}