package sesac.bookmanager.question.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sesac.bookmanager.hjdummy.DummyUser;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "question")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionAndReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questionId;

    // true면 문의게시판, false면 오류신고게시판
    private Boolean type;

    private String title;
    private String content;
    private LocalDateTime createdAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private DummyUser user;
    // TODO: 이후 User 클래스 구성되면 교체할 것

}
