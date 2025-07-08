package sesac.bookmanager.wish.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishStatisticsResponse {
    private int total;
    private int totalUnsolved;
}
