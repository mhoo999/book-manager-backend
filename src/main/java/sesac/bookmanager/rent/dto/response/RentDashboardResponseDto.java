package sesac.bookmanager.rent.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentDashboardResponseDto {
    int todayRentCount;
    int totalRentCount;
    int overdueRentCount;
}
