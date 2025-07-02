package sesac.bookmanager.wish.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WishPageResponse {
    private int page;
    private int size;
    private long totalCount;
    private int totalPages;
    private List<WishResponse> wishes;

    public static WishPageResponse from(List<WishResponse> wishes, WishSearchRequest search, Long count) {
        int totalPages = (int) Math.ceil((double) count / search.getSize());

        return new WishPageResponse(
                search.getPage(),
                search.getSize(),
                count,
                totalPages,
                wishes
        );
    }
}
