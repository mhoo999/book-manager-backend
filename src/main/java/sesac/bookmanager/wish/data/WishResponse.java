package sesac.bookmanager.wish.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishDate;

    private String userEmail;
    private String userName;
    private String userPhone;

    public static WishResponse from(Wish wish) {
        return new WishResponse(
                wish.getWishId(),
                wish.getDueDate(),
                wish.getStatus(),
                wish.getBookName(),
                wish.getAuthor(),
                wish.getPublisher(),
                wish.getPublishDate().toLocalDate(),
                wish.getUser().getEmail(),
                wish.getUser().getName(),
                wish.getUser().getPhone()
        );
    }
}
