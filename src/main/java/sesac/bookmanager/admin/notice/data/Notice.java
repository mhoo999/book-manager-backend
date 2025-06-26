package sesac.bookmanager.admin.notice.data;

import jakarta.persistence.*;
import lombok.*;
import sesac.bookmanager.hjdummy.DummyUser;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "notice")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    private NoticeType type;
    private LocalDateTime createdAt;
    private String title;
    private String content;
    private Long views;


    @ManyToOne(fetch = FetchType.LAZY)
    private DummyUser user;
    // TODO: 이후 Admin 클래스 구성되면 교체할 것
}

@Getter
@RequiredArgsConstructor
enum NoticeType {
    DEFAULT(1),
    DAYOFF(2),
    SCHEDULE(3),
    RECRUIT(4);

    private final int code;

    public static NoticeType fromCode(int code) {
        for (NoticeType noticeType : NoticeType.values()) {
            if (noticeType.code == code) {
                return noticeType;
            }
        }

        throw new IllegalArgumentException("공지사항 타입코드가 지정된 범위(0~4)를 벗어났습니다 : " + code);
    }
}

@Converter(autoApply = true)
class NoticeTypeConverter implements AttributeConverter<NoticeType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(NoticeType noticeType) {
        return noticeType != null ? noticeType.getCode() : null;
    }

    @Override
    public NoticeType convertToEntityAttribute(Integer dbData) {
        return dbData != null ? NoticeType.fromCode(dbData) : null;
    }
}