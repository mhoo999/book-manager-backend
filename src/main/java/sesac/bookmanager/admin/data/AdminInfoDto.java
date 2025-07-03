package sesac.bookmanager.admin.data;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminInfoDto {
    private int adminId;
    private String adminName;
    private String adminAccountId;
    private String dept;
    private String position;
    private String phoneNumber;
}
