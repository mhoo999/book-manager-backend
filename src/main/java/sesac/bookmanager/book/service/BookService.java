package sesac.bookmanager.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sesac.bookmanager.book.domain.Book;
import sesac.bookmanager.book.domain.BookItem;
import sesac.bookmanager.book.dto.request.CreateBookRequestDto;
import sesac.bookmanager.book.dto.request.SearchBookRequestDto;
import sesac.bookmanager.book.dto.request.UpdateBookRequestDto;
import sesac.bookmanager.book.dto.response.BookCodeResponseDto;
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

    public BookCodeResponseDto createBook(CreateBookRequestDto request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPublisher(request.getPublisher());
        book.setPublishedAt(request.getPublishedAt());
        book.setIsbn(request.getISBN());
        book.setDescription(null);
        book.setCategoryCode(request.getCategory());
        book.setStock(request.getStock());
        book.setCover(request.getCover());

        BookStatus status = Boolean.TRUE.equals(request.getIsAvailable())
                ? BookStatus.RENTABLE : BookStatus.UNRENTABLE;

        for (int i = 0; i < request.getStock(); i++) {
            BookItem bookItem = new BookItem();
            bookItem.setBookCode(generateBookCode(request.getCategory()));
            bookItem.setStatus(status);
            book.addBookItem(bookItem);
        }

        Book savedBook = bookRepository.save(book);

        return new BookCodeResponseDto(savedBook.getId());
    }

    private String generateBookCode(String category) {
        // ex. BK0101010001 ⇒ BK + 01(대분류) + 01(중분류) + 01(소분류) + 0001
        String prefix = "BK" + category;

        List<String> latestCodes = bookItemRepository.findBookCodesByCategoryOrdered(category, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "BookCode")));

        int nextSequence = 1;

        if (!latestCodes.isEmpty()) {
            String latestCode = latestCodes.get(0);
            String sequenceStr = latestCode.substring(latestCode.length() - 4);
            nextSequence = Integer.parseInt(sequenceStr) + 1;
        }

        String sequenceFormatted = String.format("%04d", nextSequence);
        return prefix + sequenceFormatted;
    }

    @Transactional(readOnly = true)
    public PageBookResponseDto getAllBooks() {
    }

    @Transactional(readOnly = true)
    public PageBookResponseDto searchBooks(SearchBookRequestDto request) {
    }

    @Transactional(readOnly = true)
    public BookResponseDto getBook(long bookId) {
    }

    public BookCodeResponseDto updateBook(Long bookId, UpdateBookRequestDto request) {
    }
}
