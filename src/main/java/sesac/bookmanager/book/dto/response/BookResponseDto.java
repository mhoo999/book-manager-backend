package sesac.bookmanager.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sesac.bookmanager.book.domain.Book;
import sesac.bookmanager.book.enums.BookStatus;

import java.time.LocalDateTime;
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
    private String location;
    private String cover;
    private String description;
    private List<BookItemResponseDto> books;

    // 계산 필드들
    private int totalQuantity;
    private int availableQuantity;
    private int rentedQuantity;

    public static BookResponseDto from(Book book) {
        List<BookItemResponseDto> bookItems = book.getBookItems().stream()
                .map(BookItemResponseDto::from)
                .toList();

        // 상태별 개수 계산
        int availableCount = (int) book.getBookItems().stream()
                .filter(item -> item.getStatus() == BookStatus.RENTABLE)
                .count();

        int rentedCount = (int) book.getBookItems().stream()
                .filter(item -> item.getStatus() == BookStatus.RENTED)
                .count();

        return BookResponseDto.builder()
                .bookId(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .publish_date(book.getPublishedAt())
                .categoryCode(book.getCategoryCode())
                .stock(book.getStock())
                .location(book.getLocation())
                .cover(book.getCover())
                .description(book.getDescription())
                .books(bookItems)
                .totalQuantity(bookItems.size())
                .availableQuantity(availableCount)
                .rentedQuantity(rentedCount)
                .build();
    }
}