package sesac.bookmanager.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sesac.bookmanager.book.domain.BookItem;
import sesac.bookmanager.book.enums.BookStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookItemResponseDto {
    private String bookCode;
    private String location;
    private BookStatus status;

    public static BookItemResponseDto from(BookItem bookItem) {
        return new BookItemResponseDto(
                bookItem.getBookCode(),
                bookItem.getBook().getLocation(),
                bookItem.getStatus()
        );
    }
}
