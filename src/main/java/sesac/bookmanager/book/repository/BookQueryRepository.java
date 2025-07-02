package sesac.bookmanager.book.repository;

import org.springframework.data.domain.Page;
import sesac.bookmanager.book.dto.request.SearchBookRequestDto;
import sesac.bookmanager.book.dto.response.BookResponseDto;

public interface BookQueryRepository {
    Page<BookResponseDto> searchBooks(SearchBookRequestDto request);
}
