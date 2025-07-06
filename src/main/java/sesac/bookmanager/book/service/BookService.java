package sesac.bookmanager.book.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final BookItemRepository bookItemRepository;
    private final CategoryService categoryService;

    public BookIdResponseDto createBook(CreateBookRequestDto request) {
        try {
            // 카테고리 유효성 검증
            if (!categoryService.isValidCategoryCode(request.getCategory())) {
                throw new IllegalArgumentException("존재하지 않는 카테고리 코드입니다: " + request.getCategory());
            }

            Book book = new Book();
            book.setTitle(request.getTitle());
            book.setAuthor(request.getAuthor());
            book.setPublisher(request.getPublisher());
            book.setPublishedAt(request.getPublishedAt());
            book.setLocation(request.getLocation());
            book.setStock(request.getStock());
            book.setCover(request.getCover());
            book.setIsbn(request.getIsbn());
            book.setCategoryCode(request.getCategory()); // 6자리 전체 카테고리 코드 저장
            book.setDescription(null);

            BookStatus status = Boolean.TRUE.equals(request.getIsAvailable())
                    ? BookStatus.RENTABLE : BookStatus.UNRENTABLE;

            if (request.getStock() > 0) {
                // 해당 카테고리의 다음 시퀀스 번호 가져오기
                int startSequence = getNextSequence(request.getCategory());

                for (int i = 0; i < request.getStock(); i++) {
                    BookItem bookItem = new BookItem();
                    // 도서 코드 생성: BK + 6자리카테고리코드 + 4자리순번
                    bookItem.setBookCode(generateBookCode(request.getCategory(), startSequence + i));
                    bookItem.setStatus(status);
                    book.addBookItem(bookItem);
                }
            }

            Book savedBook = bookRepository.save(book);
            return new BookIdResponseDto(savedBook.getId());
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                throw new RuntimeException("도서 코드 중복이 발생했습니다. 다시 시도해주세요.");
            }
            throw e;
        }
    }

    /**
     * 해당 카테고리의 다음 시퀀스 번호를 반환
     * @param category 6자리 카테고리 코드 (예: "010101")
     * @return 다음 시퀀스 번호
     */
    private synchronized int getNextSequence(String category) {
        List<String> latestCodes = bookItemRepository.findBookCodesByCategoryOrdered(
                category,
                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "bookCode"))
        );

        if (latestCodes.isEmpty()) {
            return 1;
        }

        String latestCode = latestCodes.get(0);
        // 마지막 4자리 시퀀스 추출
        String sequenceStr = latestCode.substring(latestCode.length() - 4);
        return Integer.parseInt(sequenceStr) + 1;
    }

    /**
     * 도서 코드 생성
     * @param category 6자리 카테고리 코드 (예: "010101")
     * @param sequence 시퀀스 번호
     * @return 생성된 도서 코드 (예: "BK0101010001")
     */
    private String generateBookCode(String category, int sequence) {
        // 형식: BK + 6자리카테고리코드 + 4자리시퀀스
        // 예시: BK010101 + 0001 = BK0101010001
        String prefix = "BK" + category;
        String sequenceFormatted = String.format("%04d", sequence);
        return prefix + sequenceFormatted;
    }

    @Transactional(readOnly = true)
    public PageBookResponseDto searchBooks(SearchBookRequestDto request) {
        Page<BookResponseDto> page = bookRepository.searchBooks(request);
        return PageBookResponseDto.from(page.getContent(), request, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public BookResponseDto getBook(long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디를 가진 도서가 존재하지 않습니다: " + bookId));
        return BookResponseDto.from(book);
    }

    public BookIdResponseDto updateBook(Long bookId, UpdateBookRequestDto request) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디를 가진 도서가 존재하지 않습니다: " + bookId));

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPublisher(request.getPublisher());
        book.setLocation(request.getLocation());
        book.setCover(request.getCover());

        return new BookIdResponseDto(book.getId());
    }
}
