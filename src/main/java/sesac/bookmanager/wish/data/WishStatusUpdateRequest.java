package sesac.bookmanager.wish.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishStatusUpdateRequest {
    private WishStatus status;

    public Wish toDomain() {
        Wish wish = new Wish();
        wish.setStatus(status);
        return wish;
    }
}
