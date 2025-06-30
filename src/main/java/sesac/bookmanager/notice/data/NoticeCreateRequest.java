package sesac.bookmanager.notice.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeCreateRequest {
    private String title;
    private String content;
    private NoticeType type;
    private Integer adminId;
}
