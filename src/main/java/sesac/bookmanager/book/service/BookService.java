package sesac.bookmanager.book.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final BookItemRepository bookItemRepository;

    public BookIdResponseDto createBook(CreateBookRequestDto request) {

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPublisher(request.getPublisher());
        book.setPublishedAt(request.getPublishedAt());
        book.setLocation(request.getLocation());
        book.setStock(request.getStock());
        book.setCover(request.getCover());
        book.setIsbn(request.getIsbn());
        book.setCategoryCode(request.getCategory());
        book.setDescription(null);

        BookStatus status = Boolean.TRUE.equals(request.getIsAvailable())
                ? BookStatus.RENTABLE : BookStatus.UNRENTABLE;

        if (request.getStock() > 0) {
            int startSequence = getNextSequence(request.getCategory());

            for (int i = 0; i < request.getStock(); i++) {
                BookItem bookItem = new BookItem();
                bookItem.setBookCode(generateBookCode(request.getCategory(), startSequence + i));
                bookItem.setStatus(status);
                book.addBookItem(bookItem);
            }
        }

        Book savedBook = bookRepository.save(book);
        return new BookIdResponseDto(savedBook.getId());
    }

    private int getNextSequence(String category) {
        List<String> latestCodes = bookItemRepository.findBookCodesByCategoryOrdered(category, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "bookCode")));

        if (latestCodes.isEmpty()) {
            return 1;
        }

        String latestCode = latestCodes.get(0);
        String sequenceStr = latestCode.substring(latestCode.length() - 4);
        return Integer.parseInt(sequenceStr) + 1;
    }

    private String generateBookCode(String category, int sequence) {
        // ex. BK0101010001 ⇒ BK + 01(대분류) + 01(중분류) + 01(소분류) + 0001
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
