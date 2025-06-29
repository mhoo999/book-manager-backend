package sesac.bookmanager.book.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchBookRequestDto {
    private String title;
    private String author;
    private String publisher;
    private String isbn;

    private String sort;
    private int page = 0;
    private int size = 10;
}
