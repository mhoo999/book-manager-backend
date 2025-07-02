package sesac.bookmanager.book.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book")
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @NotBlank(message = "도서 제목은 필수입니다.")
    @Size(max = 255, message = "제목은 255자를 초과할 수 없습니다.")
    private String title;

    @NotBlank(message = "저자는 필수입니다.")
    @Size(max = 100, message = "저자명은 100자를 초과할 수 없습니다.")
    private String author;

    @Size(max = 255, message = "출판사명은 100자를 초과할 수 없습니다.")
    private String publisher;

    @Column(name = "published_at")
    @PastOrPresent(message = "출간일은 미래 날짜일 수 없습니다.")
    private LocalDateTime publishedAt;

    private String location;

    @Column(name = "ISBN", unique = true)
    @NotBlank(message = "ISBN은 필수입니다.")
    @Pattern(regexp = "^\\d{10}$|^\\d{13}$", message = "ISBN은 10자리 또는 13자리 숫자여야 합니다")
    private String isbn;

    @Column(columnDefinition = "LONGTEXT")
    @Size(max = 5000, message = "설명은 5000자를 초과할 수 없습니다.")
    private String description;

    @Column(name = "category_code")
    @NotBlank(message = "카테고리 코드는 필수입니다.")
    private String categoryCode;

    @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
    @Max(value = 9999, message = "재고는 9999를 초과할 수 없습니다.")
    private int stock;

    @Size(max = 500, message = "표지 URL은 500자를 초과할 수 없습니다.")
    private String cover;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookItem> bookItems = new ArrayList<>();

    public void addBookItem(BookItem bookItem) {
        bookItems.add(bookItem);
        bookItem.setBook(this);
    }
}
