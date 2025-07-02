package sesac.bookmanager.wish.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishCreateRequest {
    private String bookName;
    private String author;
    private String publisher;
    private LocalDateTime publishDate;

    public Wish toDomain() {
        Wish wish = new Wish();
        wish.setBookName(bookName);
        wish.setAuthor(author);
        wish.setPublisher(publisher);
        wish.setPublishDate(publishDate);

        return wish;
    }
}
