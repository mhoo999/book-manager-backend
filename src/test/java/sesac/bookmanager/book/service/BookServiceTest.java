package sesac.bookmanager.book.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sesac.bookmanager.book.domain.Book;
import sesac.bookmanager.book.domain.BookItem;
import sesac.bookmanager.book.dto.request.CreateBookRequestDto;
import sesac.bookmanager.book.dto.request.SearchBookRequestDto;
import sesac.bookmanager.book.dto.request.UpdateBookRequestDto;
import sesac.bookmanager.book.dto.response.BookIdResponseDto;
import sesac.bookmanager.book.dto.response.BookResponseDto;
import sesac.bookmanager.book.enums.BookStatus;
import sesac.bookmanager.book.repository.BookItemRepository;
import sesac.bookmanager.book.repository.BookRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookItemRepository bookItemRepository;

    @InjectMocks
    private BookService bookService;

    private Book mockBook;
    private SearchBookRequestDto searchBookRequestDto;
    private UpdateBookRequestDto updateBookRequestDto;

    @BeforeEach
    void setUp() {
        when(bookItemRepository.findBookCodesByCategoryOrdered(any(), any()))
                .thenReturn(Collections.emptyList());

        mockBook = createMockBook();
        searchBookRequestDto = createSearchRequest();
        updateBookRequestDto = createUpdateRequest();
    }

    @Nested
    @DisplayName("도서 생성 테스트")
    class CreateBookTest {
        @Test
        @DisplayName("도서가 '대여가능'으로 등록된 후 도서 아이디를 정상적으로 반환한다")
        void createBook_WhenAvailable_ReturnsBookId() {
            // given
            CreateBookRequestDto request = createCreateBookRequest();
            request.setIsAvailable(true);
            request.setStock(3);

            Book savedBook = new Book();
            savedBook.setId(1L);

            when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

            // when
            BookIdResponseDto result = bookService.createBook(request);

            // then
            assertThat(result.getBookId()).isEqualTo(1L);
            verify(bookRepository).save(argThat(book ->
                    book.getBookItems().size() == 3 &&
                    book.getBookItems().stream().allMatch(item ->
                    item.getStatus() == BookStatus.RENTABLE)));
        }

        @Test
        @DisplayName("도서가 '대여불가'으로 등록된 후 도서 아이템들이 UNRENTABLE 상태로 생성된다")
        void createBook_WhenUnavailable_CreatesUnrentableItems() {
            // given
            CreateBookRequestDto request = createCreateBookRequest();
            request.setIsAvailable(false);
            request.setStock(2);

            Book savedBook = new Book();
            savedBook.setId(2L);

            when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

            // when
            BookIdResponseDto result = bookService.createBook(request);

            // then
            assertThat(result.getBookId()).isEqualTo(2L);
            verify(bookRepository).save(argThat(book ->
                    book.getBookItems().stream().allMatch(item ->
                    item.getStatus() == BookStatus.UNRENTABLE)));
        }

        @Test
        @DisplayName("도서 생성 시 재고 수만큼 고유한 도서 코드가 생성된다")
        void createBook_GeneratesUniqueBookCodes() {
            // given
            CreateBookRequestDto request = createCreateBookRequest();
            request.setCategory("010101");
            request.setStock(2);

            when(bookItemRepository.findBookCodesByCategoryOrdered(eq("010101"), any()))
                    .thenReturn(Arrays.asList("BK0101010005"));

            Book savedBook = new Book();
            savedBook.setId(3L);
            when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

            ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);

            // when
            BookIdResponseDto result = bookService.createBook(request);

            // then
            assertThat(result.getBookId()).isEqualTo(3L);
            verify(bookRepository).save(bookCaptor.capture());
            Book capturedBook = bookCaptor.getValue();

            List<String> codes = capturedBook.getBookItems().stream()
                    .map(BookItem::getBookCode)
                    .collect(Collectors.toList());

            assertThat(codes).containsExactlyInAnyOrder("BK0101010006", "BK0101010007");
        }

        @Test
        @DisplayName("첫 번째 도서 생성 시 순번이 0001부터 시작된다")
        void createBook_FirstBook_StartsFromSequence0001() {

        }

        @Test
        @DisplayName("재고가 0인 경우 BookItem이 생성되지 않는다")
        void createBook_ZeroStock_CreatesNoBookItems() {

        }
    }

    @Nested
    @DisplayName("도서 검색 테스트")
    class SearchBooksTest {
        @Test
        @DisplayName("검색 조건에 맞는 도서 목록을 페이지로 반환한다")
        void searchBooks_WithValidRequest_ReturnsPageBookResponseDto() {

        }

        @Test
        @DisplayName("검색 결과가 없을 때 빈 페이지를 반환한다")
        void searchBooks_NoResults_ReturnsEmptyPage() {

        }
    }

    @Nested
    @DisplayName("도서 단건 조회 테스트")
    class GetBookTest {
        @Test
        @DisplayName("존재하는 도서 ID로 조회 시 도서 정보를 반환한다")
        void getBook_WithValidId_ReturnsBookResponseDto() {

        }

        @Test
        @DisplayName("존재하지 않는 도서 ID로 조회 시 EntityNotFoundException을 발생시킨다")
        void getBook_WithInvalidId_ThrowsEntityNotFoundException() {

        }
    }

    @Nested
    @DisplayName("도서 수정 테스트")
    class UpdateBookTest {
        @Test
        @DisplayName("존재하는 도서를 정상적으로 수정하고 도서 ID를 반환한다")
        void updateBook_WithValidData_ReturnsBookIdResponseDto() {

        }

        @Test
        @DisplayName("존재하지 않는 도서 수정 시 EntityNotFoundException을 발생시킨다")
        void updateBook_WithInvalidId_ThrowsEntityNotFoundException() {

        }
    }

    // ----- 테스트 데이터 생성 메서드들 -----
    private CreateBookRequestDto createCreateBookRequest() {
        CreateBookRequestDto request = new CreateBookRequestDto();
        request.setTitle("Clean Code");
        request.setAuthor("Robert C. Martin");
        request.setPublisher("Prentice Hall");
        request.setPublishedAt(LocalDateTime.of(2008, 8, 1, 0, 0));
        request.setLocation("A-1-001");
        request.setStock(5);
        request.setIsAvailable(true);
        request.setCover("http://example.com/cover.jpg");
        request.setIsbn("9780132350884");
        request.setCategory("010101");
        return request;
    }

    private Book createMockBook() {
        return createMockBook(1L, "Clean Code");
    }

    private Book createMockBook(Long id, String title) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setAuthor("Robert C. Martin");
        book.setPublisher("Prentice Hall");
        book.setPublishedAt(LocalDateTime.of(2008, 8, 1, 0, 0));
        book.setLocation("A-1-001");
        book.setIsbn("9780132350884");
        book.setDescription("A Handbook of Agile Software Craftsmanship");
        book.setCategoryCode("CS");
        book.setStock(10);
        book.setCover("http://example.com/cover.jpg");
        return book;
    }

    private SearchBookRequestDto createSearchRequest() {
        SearchBookRequestDto request = new SearchBookRequestDto();
        request.setTitle("Clean");
        request.setAuthor("Martin");
        request.setPage(0);
        request.setSize(10);
        request.setSort("title,ASC");
        return request;
    }

    private UpdateBookRequestDto createUpdateRequest() {
        UpdateBookRequestDto request = new UpdateBookRequestDto();
        request.setTitle("Updated Clean Code");
        request.setAuthor("Updated Robert C. Martin");
        request.setPublisher("Updated Prentice Hall");
        request.setLocation("B-2-002");
        request.setCover("http://example.com/updated-cover.jpg");
        return request;
    }

}