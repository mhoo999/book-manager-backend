package sesac.bookmanager.admin.notice.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeSearchRequest {
    private String title;

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 5;
}
