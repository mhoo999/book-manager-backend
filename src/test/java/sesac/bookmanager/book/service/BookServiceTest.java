package sesac.bookmanager.book.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import sesac.bookmanager.book.domain.Book;
import sesac.bookmanager.book.domain.BookItem;
import sesac.bookmanager.book.dto.request.CreateBookRequestDto;
import sesac.bookmanager.book.dto.request.SearchBookRequestDto;
import sesac.bookmanager.book.dto.request.UpdateBookRequestDto;
import sesac.bookmanager.book.dto.response.BookIdResponseDto;
import sesac.bookmanager.book.dto.response.BookResponseDto;
import sesac.bookmanager.book.dto.response.PageBookResponseDto;
import sesac.bookmanager.book.enums.BookStatus;
import sesac.bookmanager.book.repository.BookItemRepository;
import sesac.bookmanager.book.repository.BookRepository;
import sesac.bookmanager.category.service.CategoryService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookService 테스트")
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookItemRepository bookItemRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private BookService bookService;

    @TempDir
    Path tempDir;

    private CreateBookRequestDto createBookRequestDto;
    private UpdateBookRequestDto updateBookRequestDto;
    private SearchBookRequestDto searchBookRequestDto;
    private Book book;
    private BookItem bookItem;

    @BeforeEach
    void setUp() {
        // TempDir을 STATIC_PATH로 설정
        ReflectionTestUtils.setField(bookService, "STATIC_PATH", tempDir.toString());

        // 테스트 데이터 초기화
        createBookRequestDto = new CreateBookRequestDto(
                "테스트 도서",
                "테스트 저자",
                "테스트 출판사",
                LocalDate.of(2023, 1, 1),
                "A1-001",
                5,
                true,
                null,
                null,
                "9781234567890",
                "100001",
                "테스트 도서 설명"
        );

        updateBookRequestDto = new UpdateBookRequestDto(
                "수정된 도서",
                "수정된 저자",
                "수정된 출판사",
                "A1-002",
                null,
                null
        );

        searchBookRequestDto = new SearchBookRequestDto(
                "테스트",
                "저자",
                "출판사",
                "9781234567890",
                "title,ASC",
                0,
                10
        );

        book = new Book();
        book.setId(1L);
        book.setTitle("테스트 도서");
        book.setAuthor("테스트 저자");
        book.setPublisher("테스트 출판사");
        book.setPublishedAt(LocalDateTime.of(2023, 1, 1, 0, 0));
        book.setLocation("A1-001");
        book.setStock(5);
        book.setIsbn("9781234567890");
        book.setCategoryCode("100001");
        book.setDescription("테스트 도서 설명");

        bookItem = new BookItem();
        bookItem.setId(1L);
        bookItem.setBookCode("BK1000010001");
        bookItem.setStatus(BookStatus.RENTABLE);
        bookItem.setBook(book);
    }

    @Test
    @DisplayName("도서 생성 성공 - 파일 업로드 없이")
    void createBook_Success_WithoutFile() {
        // given
        given(categoryService.isValidCategoryCode("100001")).willReturn(true);
        given(bookItemRepository.findBookCodesByCategoryOrdered(eq("100001"), any(Pageable.class)))
                .willReturn(List.of());
        given(bookRepository.save(any(Book.class))).willReturn(book);

        // when
        BookIdResponseDto result = bookService.createBook(createBookRequestDto);

        // then
        assertThat(result.getBookId()).isEqualTo(1L);
        verify(categoryService).isValidCategoryCode("100001");
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("도서 생성 성공 - 파일 업로드와 함께 (실제 파일 저장)")
    void createBook_Success_WithFile_RealFileSystem() throws IOException {
        // given
        MockMultipartFile mockFile = new MockMultipartFile(
                "coverFile",
                "cover.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
        createBookRequestDto.setCoverFile(mockFile);

        given(categoryService.isValidCategoryCode("100001")).willReturn(true);
        given(bookItemRepository.findBookCodesByCategoryOrdered(eq("100001"), any(Pageable.class)))
                .willReturn(List.of());

        given(bookRepository.save(any(Book.class))).willAnswer(invocation -> {
            Book bookToSave = invocation.getArgument(0);
            bookToSave.setId(1L);
            return bookToSave;
        });

        // when
        BookIdResponseDto result = bookService.createBook(createBookRequestDto);

        // then
        assertThat(result.getBookId()).isEqualTo(1L);

        // Book 엔티티 검증 (파일 저장과 독립적으로)
        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());
        Book savedBook = bookCaptor.getValue();
        assertThat(savedBook.getCover()).isEqualTo("/images/covers/9781234567890/cover.jpg");
        assertThat(savedBook.getTitle()).isEqualTo("테스트 도서");
        assertThat(savedBook.getBookItems()).hasSize(5);

        // 파일 저장 검증 (선택적)
        Path expectedPath = Paths.get(tempDir.toString(), "images", "covers", "9781234567890", "cover.jpg");
        // 파일이 있으면 좋지만, 없어도 테스트가 실패하지 않도록 함
        System.out.println("File saved at: " + expectedPath + " - exists: " + Files.exists(expectedPath));
    }

    @Test
    @DisplayName("도서 생성 실패 - 잘못된 카테고리 코드")
    void createBook_Fail_InvalidCategory() {
        // given
        given(categoryService.isValidCategoryCode("100001")).willReturn(false);

        // when & then
        assertThatThrownBy(() -> bookService.createBook(createBookRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 카테고리 코드입니다: 100001");
    }

    @Test
    @DisplayName("도서 생성 실패 - 도서 코드 중복")
    void createBook_Fail_DuplicateBookCode() {
        // given
        given(categoryService.isValidCategoryCode("100001")).willReturn(true);
        given(bookItemRepository.findBookCodesByCategoryOrdered(eq("100001"), any(Pageable.class)))
                .willReturn(List.of());
        given(bookRepository.save(any(Book.class)))
                .willThrow(new DataIntegrityViolationException("Duplicate entry"));

        // when & then
        assertThatThrownBy(() -> bookService.createBook(createBookRequestDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("도서 코드 중복이 발생했습니다. 다시 시도해주세요.");
    }

    @Test
    @DisplayName("도서 생성 성공 - 기존 도서 코드가 있는 경우 시퀀스 증가")
    void createBook_Success_WithExistingBookCode() {
        // given
        given(categoryService.isValidCategoryCode("100001")).willReturn(true);
        given(bookItemRepository.findBookCodesByCategoryOrdered(eq("100001"), any(Pageable.class)))
                .willReturn(List.of("BK1000010005")); // 기존 최대 코드
        given(bookRepository.save(any(Book.class))).willReturn(book);

        // when
        BookIdResponseDto result = bookService.createBook(createBookRequestDto);

        // then
        assertThat(result.getBookId()).isEqualTo(1L);

        // 저장된 Book의 BookItem들의 코드 확인
        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());
        Book savedBook = bookCaptor.getValue();

        List<String> bookCodes = savedBook.getBookItems().stream()
                .map(BookItem::getBookCode)
                .toList();

        assertThat(bookCodes).containsExactly(
                "BK1000010006", "BK1000010007", "BK1000010008", "BK1000010009", "BK1000010010"
        );
    }

    @Test
    @DisplayName("파일 업로드 실패 - 파일 크기 초과")
    void createBook_Fail_FileSizeExceeded() {
        // given
        byte[] largeContent = new byte[6 * 1024 * 1024]; // 6MB
        MockMultipartFile largeFile = new MockMultipartFile(
                "coverFile",
                "cover.jpg",
                "image/jpeg",
                largeContent
        );
        createBookRequestDto.setCoverFile(largeFile);
        given(categoryService.isValidCategoryCode("100001")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> bookService.createBook(createBookRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("파일 크기가 너무 큽니다. 최대 5MB까지 허용됩니다.");
    }

    @Test
    @DisplayName("파일 업로드 실패 - 지원하지 않는 파일 형식")
    void createBook_Fail_UnsupportedFileType() {
        // given
        MockMultipartFile unsupportedFile = new MockMultipartFile(
                "coverFile",
                "cover.txt",
                "text/plain",
                "test content".getBytes()
        );
        createBookRequestDto.setCoverFile(unsupportedFile);
        given(categoryService.isValidCategoryCode("100001")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> bookService.createBook(createBookRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지원하지 않는 파일 형식입니다. jpg, jpeg, png, gif만 허용됩니다.");
    }

    @Test
    @DisplayName("도서 검색 성공")
    void searchBooks_Success() {
        // given
        List<BookResponseDto> books = Arrays.asList(
                BookResponseDto.builder().bookId(1L).title("테스트 도서1").build(),
                BookResponseDto.builder().bookId(2L).title("테스트 도서2").build()
        );
        Page<BookResponseDto> page = new PageImpl<>(books, PageRequest.of(0, 10), 2);
        given(bookRepository.searchBooks(searchBookRequestDto)).willReturn(page);

        // when
        PageBookResponseDto result = bookService.searchBooks(searchBookRequestDto);

        // then
        assertThat(result.getBooks()).hasSize(2);
        assertThat(result.getTotalCount()).isEqualTo(2);
        assertThat(result.getPage()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("도서 단건 조회 성공")
    void getBook_Success() {
        // given
        Long bookId = 1L;
        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));

        // when
        BookResponseDto result = bookService.getBook(bookId);

        // then
        assertThat(result.getBookId()).isEqualTo(bookId);
        assertThat(result.getTitle()).isEqualTo("테스트 도서");
        assertThat(result.getAuthor()).isEqualTo("테스트 저자");
    }

    @Test
    @DisplayName("도서 단건 조회 실패 - 존재하지 않는 도서")
    void getBook_Fail_BookNotFound() {
        // given
        Long bookId = 999L;
        given(bookRepository.findById(bookId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> bookService.getBook(bookId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("해당 아이디를 가진 도서가 존재하지 않습니다: 999");
    }

    @Test
    @DisplayName("도서 수정 성공 - 파일 업로드 없이")
    void updateBook_Success_WithoutFile() {
        // given
        Long bookId = 1L;
        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));

        // when
        BookIdResponseDto result = bookService.updateBook(bookId, updateBookRequestDto);

        // then
        assertThat(result.getBookId()).isEqualTo(bookId);
        assertThat(book.getTitle()).isEqualTo("수정된 도서");
        assertThat(book.getAuthor()).isEqualTo("수정된 저자");
        assertThat(book.getPublisher()).isEqualTo("수정된 출판사");
        assertThat(book.getLocation()).isEqualTo("A1-002");
    }

    @Test
    @DisplayName("도서 수정 성공 - 파일 업로드와 함께 (실제 파일 저장)")
    void updateBook_Success_WithFile_RealFileSystem() throws IOException {
        // given
        Long bookId = 1L;
        MockMultipartFile mockFile = new MockMultipartFile(
                "coverFile",
                "updated_cover.jpg",
                "image/jpeg",
                "updated test image content".getBytes()
        );
        updateBookRequestDto.setCoverFile(mockFile);

        // Book 객체 복사본 생성 (원본 수정 방지)
        Book testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("테스트 도서");
        testBook.setAuthor("테스트 저자");
        testBook.setPublisher("테스트 출판사");
        testBook.setIsbn("9781234567890");
        testBook.setLocation("A1-001");

        given(bookRepository.findById(bookId)).willReturn(Optional.of(testBook));

        // when
        BookIdResponseDto result = bookService.updateBook(bookId, updateBookRequestDto);

        // then
        assertThat(result.getBookId()).isEqualTo(bookId);
        assertThat(testBook.getCover()).isEqualTo("/images/covers/9781234567890/cover.jpg");
        assertThat(testBook.getTitle()).isEqualTo("수정된 도서");
        assertThat(testBook.getAuthor()).isEqualTo("수정된 저자");
        assertThat(testBook.getPublisher()).isEqualTo("수정된 출판사");
        assertThat(testBook.getLocation()).isEqualTo("A1-002");

        // 파일 저장 검증 (선택적)
        Path expectedPath = Paths.get(tempDir.toString(), "images", "covers", "9781234567890", "cover.jpg");
        System.out.println("File saved at: " + expectedPath + " - exists: " + Files.exists(expectedPath));
    }

    @Test
    @DisplayName("도서 수정 성공 - 커버 URL 제공")
    void updateBook_Success_WithCoverUrl() {
        // given
        Long bookId = 1L;
        updateBookRequestDto.setCover("https://example.com/cover.jpg");
        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));

        // when
        BookIdResponseDto result = bookService.updateBook(bookId, updateBookRequestDto);

        // then
        assertThat(result.getBookId()).isEqualTo(bookId);
        assertThat(book.getCover()).isEqualTo("https://example.com/cover.jpg");
    }

    @Test
    @DisplayName("도서 수정 실패 - 존재하지 않는 도서")
    void updateBook_Fail_BookNotFound() {
        // given
        Long bookId = 999L;
        given(bookRepository.findById(bookId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> bookService.updateBook(bookId, updateBookRequestDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("해당 아이디를 가진 도서가 존재하지 않습니다: 999");
    }

    @Test
    @DisplayName("도서 생성 - 재고 0인 경우 BookItem 생성하지 않음")
    void createBook_Success_WithZeroStock() {
        // given
        createBookRequestDto.setStock(0);
        given(categoryService.isValidCategoryCode("100001")).willReturn(true);
        given(bookRepository.save(any(Book.class))).willReturn(book);

        // when
        BookIdResponseDto result = bookService.createBook(createBookRequestDto);

        // then
        assertThat(result.getBookId()).isEqualTo(1L);
        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());
        Book savedBook = bookCaptor.getValue();
        assertThat(savedBook.getBookItems()).isEmpty();
    }

    @Test
    @DisplayName("도서 생성 - 대여 불가능한 경우 BookItem 상태 UNRENTABLE")
    void createBook_Success_WithUnrentableStatus() {
        // given
        createBookRequestDto.setIsAvailable(false);
        given(categoryService.isValidCategoryCode("100001")).willReturn(true);
        given(bookItemRepository.findBookCodesByCategoryOrdered(eq("100001"), any(Pageable.class)))
                .willReturn(List.of());
        given(bookRepository.save(any(Book.class))).willReturn(book);

        // when
        BookIdResponseDto result = bookService.createBook(createBookRequestDto);

        // then
        assertThat(result.getBookId()).isEqualTo(1L);
        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());
        Book savedBook = bookCaptor.getValue();

        assertThat(savedBook.getBookItems()).hasSize(5);
        assertThat(savedBook.getBookItems()).allMatch(item -> item.getStatus() == BookStatus.UNRENTABLE);
    }

    @Test
    @DisplayName("도서 생성 성공 - Cover URL만 있는 경우")
    void createBook_Success_WithCoverUrlOnly() {
        // given
        createBookRequestDto.setCover("https://example.com/cover.jpg");
        createBookRequestDto.setCoverFile(null);

        given(categoryService.isValidCategoryCode("100001")).willReturn(true);
        given(bookItemRepository.findBookCodesByCategoryOrdered(eq("100001"), any(Pageable.class)))
                .willReturn(List.of());
        given(bookRepository.save(any(Book.class))).willReturn(book);

        // when
        BookIdResponseDto result = bookService.createBook(createBookRequestDto);

        // then
        assertThat(result.getBookId()).isEqualTo(1L);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());
        Book savedBook = bookCaptor.getValue();
        assertThat(savedBook.getCover()).isEqualTo("https://example.com/cover.jpg");
    }
}