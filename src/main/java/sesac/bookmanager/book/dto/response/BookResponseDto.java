package sesac.bookmanager.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sesac.bookmanager.book.domain.Book;
import sesac.bookmanager.book.domain.BookItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDto {
    private long bookId;
	private String isbn;
    private String title;
    private String author;
    private String publisher;
    private LocalDateTime publish_date;
    private String categoryCode;
    private int stock;
    private List<BookItemResponseDto> books;

    public static BookResponseDto from(Book book) {
        List<BookItemResponseDto> books = book.getBookItems().stream()
                .map(BookItemResponseDto::from)
                .toList();

        return new BookResponseDto(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getPublishedAt(),
                book.getCategoryCode(),
                book.getStock(),
                books
        );
    }
}
