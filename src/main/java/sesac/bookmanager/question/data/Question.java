package sesac.bookmanager.question.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sesac.bookmanager.user.data.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "question")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questionId;

    // true면 문의게시판, false면 오류신고게시판
    private Boolean questionType;

    // 0이면 접수대기, 1이면 처리중, 2면 처리완료
    private Byte status;

    private String title;
    private String content;
    private LocalDateTime createdAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
