package sesac.bookmanager.notice.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeUpdateRequest {
    private String title;
    private String content;
    private NoticeType type;

    public Notice toDomain() {
        Notice notice = new Notice();
        notice.setTitle(title);
        notice.setContent(content);
        notice.setType(type);

        return notice;
    }
}
