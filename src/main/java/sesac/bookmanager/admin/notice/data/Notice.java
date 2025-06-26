package sesac.bookmanager.admin.notice.data;

import jakarta.persistence.*;
import lombok.*;
import sesac.bookmanager.hjdummy.DummyAdmin;
import sesac.bookmanager.hjdummy.DummyUser;

import java.time.LocalDateTime;

@Entity
@Table(name = "notice")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer noticeId;

    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "bit(2)")
    private NoticeType type;

    private LocalDateTime createdAt;
    private String title;
    private String content;
    private Integer views;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private DummyAdmin admin;
    // TODO: 이후 Admin 클래스 구성되면 교체할 것
}