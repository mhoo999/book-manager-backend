package sesac.bookmanager.admin.wish.data;

import jakarta.persistence.*;
import lombok.*;
import sesac.bookmanager.hjdummy.DummyUser;

import java.util.Date;

@Entity
@Table(name = "wish")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishId;

    private Date dueDate;

    private WishStatus status;

    private String bookName;
    private String author;
    private String publisher;
    private Date publishDate;


    @ManyToOne(fetch = FetchType.LAZY)
    private DummyUser user;
    // TODO: 이후 User 클래스 구성되면 교체할 것
}


@Getter
@RequiredArgsConstructor
enum WishStatus {
    EXAMINING(1),
    APPROVED(2),
    PURCHASE(3),
    STOCK(4),
    REJECTED(0);

    private final int code;

    public static WishStatus fromCode(int code) {
        for (WishStatus wishStatus : WishStatus.values()) {
            if (wishStatus.code == code) {
                return wishStatus;
            }
        }

        throw new IllegalArgumentException("희망도서 상태코드가 지정된 범위(0~4)를 벗어났습니다 : " + code);
    }
}


@Converter(autoApply = true)
class WishStatusConverter implements AttributeConverter<WishStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(WishStatus wishStatus) {
        return wishStatus != null ? wishStatus.getCode() : null;
    }

    @Override
    public WishStatus convertToEntityAttribute(Integer dbData) {
        return dbData != null ? WishStatus.fromCode(dbData) : null;
    }
}