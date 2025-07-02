package sesac.bookmanager.notice.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NoticePageResponse {
    private int page;
    private int size;
    private long totalCount;
    private int totalPages;
    private List<NoticeResponse> notices;

    public static NoticePageResponse from(List<NoticeResponse> notices, NoticeSearchRequest searchRequest, long count) {
        int totalPages = (int) Math.ceil((double) count / searchRequest.getSize());

        return new NoticePageResponse(
            searchRequest.getPage(),
                searchRequest.getSize(),
                count,
                totalPages,
                notices
        );
    }
}
