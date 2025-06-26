package sesac.bookmanager.admin.question.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sesac.bookmanager.hjdummy.DummyUser;

import java.util.Date;

@Entity
@Table(name = "question")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    // true면 문의게시판, false면 오류신고게시판
    private Boolean questionType;

    private String title;
    private String content;
    private Date createdAt;


    @ManyToOne(fetch = FetchType.LAZY)
    private DummyUser user;
    // TODO: 이후 User 클래스 구성되면 교체할 것

}
