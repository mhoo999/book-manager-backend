package sesac.bookmanager.wish.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sesac.bookmanager.hjdummy.DummyUser;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishResponse {
    private Integer wishId;

    private LocalDateTime dueDate;
    private WishStatus status;

    private String bookName;
    private String author;
    private String publisher;
    private LocalDateTime publishDate;
    private DummyUser user;

    public static WishResponse from(Wish wish) {
        return new WishResponse(
                wish.getWishId(),
                wish.getDueDate(),
                wish.getStatus(),
                wish.getBookName(),
                wish.getAuthor(),
                wish.getPublisher(),
                wish.getPublishDate(),
                wish.getUser()
        );
    }
}
