package sesac.bookmanager.user.data;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserStatusDto {
    private int todayNewUserCount;
    private int totalUserCount;
    private int totalWithdrawUserCount;
}
