package sesac.bookmanager.rent.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRentRequestDto {
    private String rentCode;
    private String username;
    private String bookCode;
    private String bookName;
    private String userPhone;
    private String rentStatus;

    // todo: 오래된 요청대기부터 보여야 함
    private String sort = "rentCode,ASC";
    private int page = 0;
    private int size = 10;
}
