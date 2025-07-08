package sesac.bookmanager.wish.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishStatisticsResponse {
    private int totalWish;
    private int totalUnsolvedWish;
}
