package sesac.bookmanager.book.domain;

import jakarta.persistence.*;
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

    private String title;
    private String author;
    private String publisher;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    private String location;

    @Column(name = "ISBN")
    private String isbn;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "category_code")
    private String categoryCode;

    private int stock;
    private String cover;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookItem> bookItems = new ArrayList<>();

    public void addBookItem(BookItem bookItem) {
        bookItems.add(bookItem);
        bookItem.setBook(this);
    }
}
