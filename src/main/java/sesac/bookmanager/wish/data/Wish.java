package sesac.bookmanager.wish.data;

import jakarta.persistence.*;
import lombok.*;
import sesac.bookmanager.user.data.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "wish")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer wishId;

    private LocalDateTime dueDate;

    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "bit(3)")
    private WishStatus status;

    private String bookName;
    private String author;
    private String publisher;
    private LocalDateTime publishDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    // TODO: 이후 User 클래스 구성되면 교체할 것
}