package sesac.bookmanager.book.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sesac.bookmanager.book.domain.Book;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookRequestDto {
    private String title;
    private String author;
    private String publisher;
    private String location;
    private String cover;

    public Book toDomain() {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setLocation(location);
        book.setCover(cover);

        return book;
    }
}
