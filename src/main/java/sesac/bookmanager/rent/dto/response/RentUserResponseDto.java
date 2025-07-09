package sesac.bookmanager.rent.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentUserResponseDto {
    private int checkedOutBooks;
    private int overdueBooks;
}
